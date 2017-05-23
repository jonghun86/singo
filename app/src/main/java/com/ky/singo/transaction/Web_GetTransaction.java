package com.ky.singo.transaction;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.Map;

/**
 * Created by mmyjh on 2017-05-20.
 */

public class Web_GetTransaction {
  private HttpGet httpGet;
  private HttpResponse response;
  private final String TRANSACTION = "GET-TRANSACTION";
  private String baseUrl;

  // constructor
  public Web_GetTransaction(String url) {
    baseUrl = url;
  }

  public boolean send(Web_Param param) {
    int status;
    String urlWithParam = baseUrl;
    Web_Cookie  cookie = Web_Cookie .getInstance();

    try {
      // Send a packet
      HttpClient httpClient = new DefaultHttpClient();

      // include text body
      for (Map.Entry<String, String> elem : param.getTextEntry()) {
        urlWithParam += "?" + elem.getKey() + "=" + elem.getValue() + "&";
      }

      Log.d(TRANSACTION, "Try to send to " + urlWithParam);
      Log.d(TRANSACTION, "Try to send with cookie " + cookie.getKey());

      httpGet = new HttpGet(urlWithParam);
      httpGet.setHeader("Cookie", cookie.getKey());
      response = httpClient.execute(httpGet);
      // Receive a response packet
      status = response.getStatusLine().getStatusCode();
      if (status == HttpStatus.SC_OK) {
        // Extract cookie info.
        Header[] headers = response.getHeaders("Set-Cookie");
        if (headers.length > 1) {
          Log.d(TRANSACTION, "Multiple headers (" + headers.length + ") are received.");
        }
        else if (headers.length != 0) {
          cookie.setKey(headers[0].getValue());
          Log.d(TRANSACTION, "Cookie : " + cookie.getKey());
        }
      }
      else {
        Log.e(TRANSACTION, "Response error (status : " + status + ")");
        throw new Exception();
      }
    } catch (Exception e) {
      // For debug
      for (Map.Entry<String, String> elem : param.getTextEntry()) {
        Log.d(TRANSACTION, "EXCEPTION : " + elem.getKey() + " : " + elem.getValue());
      }
      return false;
    }
    return true;
  }

  public HttpResponse getResponse() { return response; }
}
