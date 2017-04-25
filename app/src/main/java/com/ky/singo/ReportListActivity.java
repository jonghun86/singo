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
import android.webkit.WebView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URL;
import java.util.ArrayList;

public class ReportListActivity extends AppCompatActivity {

  /**
   * String to identity log message
   */
  private final String ID_REPORT_LIST_QUERY = "REPORT_LIST";

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
  public class ReportListRequestTask extends AsyncTask<Void, Void, String> {
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
        param.add(new BasicNameValuePair("pageNo", "1"));
        param.add(new BasicNameValuePair("menuGubun", "1"));
        param.add(new BasicNameValuePair("menu1", "pc"));
        param.add(new BasicNameValuePair("jumin_no_c", ""));
        param.add(new BasicNameValuePair("peter_name_v", ""));
        param.add(new BasicNameValuePair("open_yn_c", ""));


        param.add(new BasicNameValuePair("memYn", "Y"));
        param.add(new BasicNameValuePair("mypetiViewEncFlag", "N"));
        param.add(new BasicNameValuePair("mypetiViewEncFlag", "N"));
        param.add(new BasicNameValuePair("srchBoxDetailShowYN", "N"));
        param.add(new BasicNameValuePair("menuCode", "PC"));
        param.add(new BasicNameValuePair("q_status_c", ""));
        param.add(new BasicNameValuePair("reply_confirm_yn", ""));
        param.add(new BasicNameValuePair("satisfy_yn", ""));
        param.add(new BasicNameValuePair("dateoranc_order_by", ""));
        param.add(new BasicNameValuePair("q_reg_d1", "2016-04-20"));
        param.add(new BasicNameValuePair("q_reg_d2", "2017-04-20"));
        param.add(new BasicNameValuePair("q_search_type", ""));
        param.add(new BasicNameValuePair("keyword", ""));



        String parameter = URLEncodedUtils.format(param, "UTF-8");
        HttpGet httpGet = new HttpGet(String.valueOf(url) + "?" + parameter);
        //httpGet.setHeader(new BasicHeader("JSESSIONID", cookie));
        httpGet.setHeader("Cookie", Cookie.getInstance().getKey());


        //httpGet.addHeader("Cookie", " PHPSESSID="+PHPSESSID+"; gc_userid="+gc_user+"; gc_session="+gc);






        responseGet = client.execute(httpGet);
      } catch(Exception e){
        e.printStackTrace();
        return null;
      }
      return responseGet;
    }

    @Override
    protected String doInBackground(Void... not_used) {
      final String url = "https://www.epeople.go.kr/jsp/user/on/mypage/cvreq/UPcMyCvreqList.jsp";
      //final String url = "https://m.epeople.go.kr//mypage/cvpl/cvpl_list.do";
      Boolean isSuccess;
      PostTransaction postTransaction;
      ArrayList<NameValuePair> param;

      // parameter
      param = new ArrayList<NameValuePair>();
      param.add(new BasicNameValuePair("pageNo", "1"));
      param.add(new BasicNameValuePair("mode", "getMyCvreqList"));
      param.add(new BasicNameValuePair("menuGubun", "1"));
      param.add(new BasicNameValuePair("menu1", "mobile"));
      param.add(new BasicNameValuePair("memYn", "Y"));
      param.add(new BasicNameValuePair("mypetiViewEncFlag", "N"));
      param.add(new BasicNameValuePair("srchBoxDetailShowYN", "N"));
      param.add(new BasicNameValuePair("menuCode", "PC"));
      param.add(new BasicNameValuePair("q_reg_d1", "2016-04-20"));
      param.add(new BasicNameValuePair("q_reg_d2", "2017-04-20"));
      param.add(new BasicNameValuePair("q_search_type", "q_search_no_c"));
      param.add(new BasicNameValuePair("dateoranc_order_by", "1"));
      param.add(new BasicNameValuePair("pagingCnt", "50"));
      /*
      for mobile
      param.add(new BasicNameValuePair("nowPage", "1"));
      param.add(new BasicNameValuePair("fromDate", "2016-04-20"));
      param.add(new BasicNameValuePair("toDate", "2017-04-20"));
      param.add(new BasicNameValuePair("searchKind", "txtSearch2"));
      */

      // send a packet
      postTransaction = new PostTransaction(url);
      isSuccess = postTransaction.send(param);
      if (isSuccess) {
        final HttpEntity entity = postTransaction.getResponse().getEntity();
        try {
          return EntityUtils.toString(entity);
        }
        catch (Exception e) {
          Log.d(ID_REPORT_LIST_QUERY, e.toString());
          e.printStackTrace();
        }
      }

      return null;

            /*

      HttpResponse httpResponse = requestReportList();
      final HttpEntity entity = httpResponse.getEntity();
      String responseBody = null;
      if (entity == null) {
        Log.w(ID_REPORT_LIST_QUERY, "Entity error");
        // TODO
        // Add error handling code
      }
      else {

        try {
          responseBody = EntityUtils.toString(entity);
        }
        catch (Exception e) {
          Log.d(ID_REPORT_LIST_QUERY, e.toString());
          e.printStackTrace();
        }
      }
      return responseBody;
      */
    }

    @Override
    protected void onPostExecute(final String responseBody) {

      WebView web = (WebView)findViewById(R.id.test_web_view);
      web.getSettings().setJavaScriptEnabled(true);
      web.getSettings().setDefaultTextEncodingName("UTF-8");
      web.loadDataWithBaseURL("useless", responseBody, "text/html", "UTF-8", null);
      Log.d("@@", responseBody);
    }

    @Override
    protected void onCancelled() {

    }
  }

}
