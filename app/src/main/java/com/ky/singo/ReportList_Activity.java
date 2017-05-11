package com.ky.singo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Iterator;

public class ReportList_Activity extends AppCompatActivity {
  private final String ID_REPORT_LIST_QUERY = "REPORT_LIST";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(ID_REPORT_LIST_QUERY, "onCreate");
    setContentView(R.layout.reportlist_activity);

    // Register button event
    findViewById(R.id.reportWriteButton).setOnClickListener(
      new Button.OnClickListener() {
        public void onClick(View v) {
          Intent intent = new Intent(ReportList_Activity.this, ReportWrite_Activity.class);
          startActivity(intent);
        }
      }
    );

    try {
      new ReportListRequestTask().execute();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Represents an asynchronous login/registration task used to authenticate
   * the user.
   */
  public class ReportListRequestTask extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... not_used) {
      final String url = "https://www.epeople.go.kr/jsp/user/on/mypage/cvreq/UPcMyCvreqList.jsp";
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
      Iterator<Element> iterator;
      Element element;
      LinearLayout contentRoot = (LinearLayout) findViewById(R.id.reportlist_content_root);
      ReportList_Content_View view;
      Document doc = Jsoup.parse(responseBody);
      Elements reportListTable = doc.select("td");
      iterator = reportListTable.iterator();
      String complaintTitle;
      String complaintSubInfo;

      while (iterator.hasNext()) {
        // Number
        element = (Element) iterator.next();
        Log.d("##",  element.text());
        // Empty
        iterator.next();
        // Title
        element = (Element) iterator.next();
        complaintTitle = element.text();
        // Division
        iterator.next();
        // Date
        element = (Element) iterator.next();
        complaintSubInfo = element.text();
        // request State
        element = (Element) iterator.next();
        complaintSubInfo += " (";
        complaintSubInfo += element.text();
        complaintSubInfo += ")";
        // Satisfaction State
        iterator.next();

        view = new ReportList_Content_View(getApplicationContext());
        view.setContents(complaintTitle, complaintSubInfo);
        contentRoot.addView(view);
      }
    }
  }

}
