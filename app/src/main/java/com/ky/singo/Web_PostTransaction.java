package com.ky.singo;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmyjh on 2017-04-25.
 */

public class Web_PostTransaction {
  private HttpPost httpPost;
  private HttpResponse response;
  private final String TRANSACTION = "POST-TRANSACTION";

  // constructor
  public Web_PostTransaction(String url) {
    httpPost = new HttpPost(url);
  }

  public boolean sendTest(ArrayList<NameValuePair> param, List<String> mediaPathList, byte [] bitMapData) {
    int status;
    Web_Cookie  cookie = Web_Cookie .getInstance();

    MultipartEntityBuilder builder = MultipartEntityBuilder.create();

    // Attach media files
    for (String path : mediaPathList) {
      // File #1
      //builder.addBinaryBody()
      // File #2
      //builder.addBinaryBody()
    }




    ByteArrayBody bab = new ByteArrayBody(bitMapData, "sample_image.jpg");
    builder.addPart("file1", bab);
    // Include text body
    for (NameValuePair nvp : param) {
      builder.addTextBody(nvp.getName(), nvp.getValue());
    }

    try {
      // Send a packet
      HttpClient httpClient = new DefaultHttpClient();
      httpPost.setEntity(builder.build());
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
      return false;
    }
    return true;
  }

  public boolean send(ArrayList<NameValuePair> param) {
    int status;
    Web_Cookie  cookie = Web_Cookie .getInstance();

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