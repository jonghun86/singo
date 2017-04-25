package com.ky.singo;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;

/**
 * Created by mmyjh on 2017-04-25.
 */

public class PostTransaction {
  private HttpPost httpPost;
  private HttpResponse response;
  private final String TRANSACTION = "POST-TRANSACTION";

  // constructor
  public PostTransaction (String url) {
    httpPost = new HttpPost(url);
  }

  public boolean send(ArrayList<NameValuePair> param) {
    int status;
    Cookie cookie = Cookie.getInstance();

    try {
      // Send a packet
      HttpClient httpClient = new DefaultHttpClient();
      httpPost.setEntity(new UrlEncodedFormEntity(param));
      httpPost.setHeader("Cookie", cookie.getKey());
      response = httpClient.execute(httpPost);

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
      for (NameValuePair pair : param) {
        Log.d(TRANSACTION, pair.getName() + " : " + pair.getValue());
        return false;
      }
    }
    return true;
  }

  public HttpResponse getResponse() { return response; }
}