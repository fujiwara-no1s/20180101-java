package biz.no1s.fujiwara.premier20180101;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ProductCsvWriter {
  private List<Product> products;
  
  public ProductCsvWriter(List<Product> products) {
    this.products = products;
  }
  
  public void writeCsvFile(String csvFileName) {
    StringBuilder sb = new StringBuilder("");
    for(Product product : products) {
      sb.append("\"" + product.id + "\",");
      sb.append("\"" + product.name + "\",");
      sb.append("\"" + product.price + "\",\n");
    }
    try {
      File file = new File(csvFileName);
      if(file.exists()) {
        file.delete();
      }
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
      bufferedWriter.write(sb.toString());
      bufferedWriter.close();
    } catch(IOException ioe) {
      System.out.println("Error: fail to write csv file");
    }
  }
}
