package com.ky.singo.transaction;

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
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;


/**
 * Created by mmyjh on 2017-04-25.
 */



public class Web_PostTransaction {
  private HttpPost httpPost;
  private HttpResponse response;
  private final String TRANSACTION = "POST-TRANSACTION";

  public enum TransactionType {
    GET,
    POST,
    MULTIPART
  }

  // constructor
  public Web_PostTransaction(String url) {
    httpPost = new HttpPost(url);
  }

  public boolean send(Web_Param param, TransactionType trType) {
    int status;
    Web_Cookie cookie = Web_Cookie .getInstance();

    try {
      switch (trType) {
        case GET :
          break;
        case POST :
          // to be removed
          ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
          for (Map.Entry<String, String> elem : param.getTextEntry()) {
            postParameters.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
          }
          httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
          httpPost.setHeader("Cookie", cookie.getKey());
          break;
        case MULTIPART :
          MultipartEntityBuilder builder = MultipartEntityBuilder.create();

          // include text body
          for (Map.Entry<String, String> elem : param.getTextEntry()) {
            builder.addPart(elem.getKey(), new StringBody(elem.getValue(), Charset.forName("EUC-KR")));
          }
          // attach multimedia body
          for (Map.Entry<String, byte []> elem : param.getVideoEntry()) {
            // "sample_image.jpg"는 구분자로 사용됨
            // reportWrite Activity에서 "file1" ID에 경로 + 파일 구분하기 위한 이름을 명시함
            //  - /attach04/2017/5/23/jsp/user/pc/cvreq/UPcRecommendOrg.jsp/sample_image.jpg
            // 위 경로의 이름과 아래 ByteArrayBody 생성자에서 사용하는 구분자의 이름이 같아야 함
            ByteArrayBody bab = new ByteArrayBody(elem.getValue(), "sample_image.jpg");
            builder.addPart(elem.getKey(), bab);
          }

          httpPost.setHeader("Cookie", cookie.getKey());
          httpPost.setHeader("Cache-Control", "max-age=0");
          httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
          httpPost.setHeader("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
          httpPost.setHeader("Connection", "keep-alive");
          httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
          httpPost.setHeader("Upgrade-Insecure-Requests", "1");
          httpPost.setHeader("Origin", "https://www.epeople.go.kr");
          httpPost.setHeader("Host", "www.epeople.go.kr");
          httpPost.setHeader("Referer", "https://www.epeople.go.kr/jsp/user/pc/cvreq/UPcCvreqForm.jsp?flag=N&");
          httpPost.setEntity(builder.build());
          break;
        default :
          break;
      }



      HttpClient httpClient = new DefaultHttpClient();
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
