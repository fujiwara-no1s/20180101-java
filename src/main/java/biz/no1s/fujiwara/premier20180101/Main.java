package biz.no1s.fujiwara.premier20180101;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

  private static String URL_START;
  private static String URL_ADMIN;
  private static String EMAIL;
  private static String PASSWORD;
  private static String CSV_FILE;

  public static void main(String[] args) {
    // setting properties
    setProperty();

    try {
      // main process start
      
      // get token at first
      MyOkHttp myOkHttp = new MyOkHttp();
      System.out.println("Getting token...");
      myOkHttp.get(URL_START);
      String html = myOkHttp.getBody();
      myOkHttp.close();
      
      // retrieve token value
      Document document = Jsoup.parse(html);
      String token = document.select("input[name=_csrfToken]").first().attr("value").trim();
      
      // login
      System.out.println("Login...");
      Map<String, String> maps = new TreeMap<String, String>();
      maps.put("email", URLEncoder.encode(EMAIL, "UTF-8"));
      maps.put("password", URLEncoder.encode(PASSWORD, "UTF-8"));
      maps.put("_csrfToken", token);      
      myOkHttp.post(URL_START, maps);
      myOkHttp.close();
      
      // get product page
      System.out.println("Getting products page");
      List<Product> products = new ArrayList<Product>();
      
      for (int i = 1; i <=3; i++) {
        System.out.println("Get " + URL_ADMIN + i + " ...");
        myOkHttp.get(URL_ADMIN + i);
        String productHtml = myOkHttp.getBody();
        Document productDocument = Jsoup.parse(productHtml);
        // tr elements
        Elements elements = productDocument.select("tr:nth-child(n+2)");
        for(Element element : elements) {
          // td elements
          Elements tds = element.children();
          Product product = new Product();
          product.id = tds.get(0).text();
          product.name = tds.get(1).text();
          product.price = tds.get(2).text();
          products.add(product);
        }
        myOkHttp.close();
      }
      
      // write csvfile
      System.out.println("Writing csv file...");
      ProductCsvWriter writer = new ProductCsvWriter(products);
      writer.writeCsvFile(CSV_FILE);
      System.out.println("Finished...");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void setProperty() {
    // Propertyファイルから設定を反映
    URL_START = PropertyUtil.getProperty("URL_START");
    URL_ADMIN = PropertyUtil.getProperty("URL_ADMIN");
    EMAIL = PropertyUtil.getProperty("EMAIL");
    PASSWORD = PropertyUtil.getProperty("PASSWORD");
    CSV_FILE = PropertyUtil.getProperty("CSV_FILE");
  }
}
