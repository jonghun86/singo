package com.ky.singo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ReportListActivity extends AppCompatActivity {

  /**
   * String to identity log message
   */
  private final String ID_REPORT_LIST_QUERY = "REPORT_LIST";

  private String cookie;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_report_list);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
      }
    });

    try {
      Intent intent = getIntent();
      String cookie = intent.getStringExtra("cookie");
      new ReportListRequestTask(cookie).execute();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }





  /**
   * Represents an asynchronous login/registration task used to authenticate
   * the user.
   */
  public class ReportListRequestTask extends AsyncTask<Void, Void, HttpResponse> {
    private String cookie;

    ReportListRequestTask(String cookie) {
      this.cookie = cookie;
    }

    // TODO : parameterize
    // Return
    //  - true   : if login request is transmitted to the server
    //  - false  : else
    private HttpResponse requestReportList() {
      HttpResponse responseGet;
      try {
        URL url = new URL("https://www.epeople.go.kr/jsp/user/on/mypage/cvreq/UPcMyCvreqList.jsp");
        HttpClient client = new DefaultHttpClient();

        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("menuGubun", "1"));
        param.add(new BasicNameValuePair("menu1", "1"));
        param.add(new BasicNameValuePair("memYn", "Y"));
        param.add(new BasicNameValuePair("mypetiViewEncFlag", "N"));
        param.add(new BasicNameValuePair("menuCode", "PC"));
        String parameter = URLEncodedUtils.format(param, "UTF-8");
        HttpGet httpGet = new HttpGet(String.valueOf(url) + "?" + parameter);
        httpGet.setHeader("cookie", cookie);
        responseGet = client.execute(httpGet);
      } catch(Exception e){
        e.printStackTrace();
        return null;
      }
      return responseGet;
    }

    @Override
    protected HttpResponse doInBackground(Void... not_used) {
      return requestReportList();
    }

    @Override
    protected void onPostExecute(final HttpResponse httpResponse) {
      try {
        final HttpEntity entity = httpResponse.getEntity();
        if (entity == null) {
          Log.w(ID_REPORT_LIST_QUERY, "Entity error");
          // TODO
          // Add error handling code
        }
        else {
          int status = httpResponse.getStatusLine().getStatusCode();
          // error... y?
          // error... y?
          // error... y?
          // error... y?
          //String responseBody = EntityUtils.toString(entity);

          Header[] headers = httpResponse.getAllHeaders();
          for (Header header : headers) {
            Log.d("TEST", header.getValue());
          }

          BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));

          String test;
          try {

            // error... y?
            // error... y?
            // error... y?
            // error... y?
            while ((test = in.readLine()) != null) {
              Log.d(ID_REPORT_LIST_QUERY, test);
            }
          }
          catch (IOException e) {
            e.printStackTrace();
          }




          // TODO
          // add error handling code when login process is failed,



        }
      } catch (IOException e) {
        e.printStackTrace();
      }

      //EntityUtils.getContentCharSet(entity);

      /*
      if (success) {
        finish();
      } else {
        mPasswordView.setError(getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();
      }
      */
    }

    @Override
    protected void onCancelled() {

    }
  }

}
