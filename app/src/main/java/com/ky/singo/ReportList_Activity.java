package com.ky.singo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ReportList_Activity extends AppCompatActivity {

  /**
   * String to identity log message
   */
  private final String ID_REPORT_LIST_QUERY = "REPORT_LIST";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(ID_REPORT_LIST_QUERY, "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.reportlist_activity);

    try {
      LinearLayout contentRoot = (LinearLayout) findViewById(R.id.email_login_form);


      //new ReportListRequestTask().execute();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Represents an asynchronous login/registration task used to authenticate
   * the user.
   */
  public class ReportListRequestTask extends AsyncTask<Void, Void, String> {

    ReportListRequestTask() {

    }

    @Override
    protected String doInBackground(Void... not_used) {
      final String url = "https://www.epeople.go.kr/jsp/user/on/mypage/cvreq/UPcMyCvreqList.jsp";
      //final String url = "https://m.epeople.go.kr//mypage/cvpl/cvpl_list.do";
      Boolean isSuccess;
      Web_PostTransaction postTransaction;
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
      postTransaction = new Web_PostTransaction(url);
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
    }

    @Override
    protected void onPostExecute(final String responseBody) {

      Document doc = Jsoup.parse(responseBody);
      Elements title = doc.select("td");

      for (Element e : title) {
        Log.d("##", e.text());
      }


         /*
      WebView web = (WebView)findViewById(R.id.test_web_view);
      web.getSettings().setJavaScriptEnabled(true);
      web.getSettings().setDefaultTextEncodingName("UTF-8");
      web.loadDataWithBaseURL("useless", responseBody, "text/html", "UTF-8", null);
      Log.d("@@", responseBody);
      */
    }

    @Override
    protected void onCancelled() {

    }
  }

}
