package biz.no1s.fujiwara.premier20180101;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyOkHttp {
  private OkHttpClient client;
  private Response response;
  private String body;
  
  MyOkHttp() {
    // to save cookie per domain name
    this.client = new OkHttpClient.Builder()
        .cookieJar(new CookieJar() {
          private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

          @Override
          public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
          }

          @Override
          public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
          }
        })
        //.addNetworkInterceptor(new LoggingInterceptor())
        .build();
  }
  
  public void get(String url) throws Exception {
    // craete get request
    Request request = new Request.Builder().url(url).build();
    /*
    try {
      // get
      response = client.newCall(request).execute();
    } catch (IOException e) {
      throw new Exception("Error: fail to get " + url);
    }
    */
    

    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      isSuccess(response);
      body = response.body().string();
      response.body().close();
    } catch (IOException e) {
      throw new Exception("Error: fail to get " + url);
    }
    
  }

  public void post(String url,Map<String,String> postMap) throws Exception {
    // create post value to key1=value1&key2=value2
    String postBody = postMap
        .entrySet()
        .stream()
        .map(e -> { return e.getKey() + "=" + e.getValue(); })
        .collect( Collectors.joining( "&" ) );
    MediaType MIMEType= MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    RequestBody requestBody = RequestBody.create (MIMEType,postBody);
    
    // create post request
    Request request = new Request.Builder().url(url).post(requestBody).build();
    /*
    try {
      // post
      response = client.newCall(request).execute();
    } catch (IOException e) {
      throw new Exception("Error: fail to post " + url);
    }
    isSuccess();
    */
    
    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      isSuccess(response);
      body = response.body().string();
      response.body().close();
    } catch (IOException e) {
      throw new Exception("Error: fail to get " + url);
    }
  }

  private void isSuccess(Response response) throws Exception {
    if( response.code() != 200 || !response.isSuccessful()) {
      System.out.println(response.body().string());
      throw new Exception("Error: not successfull");
    }
  }
  
  public String getBody() throws IOException {
    // return response body  
    //return response.body().string();
    return body;
  }
  
  public void close() {
    //response.close();
    //response.body().close();    
  }
}
