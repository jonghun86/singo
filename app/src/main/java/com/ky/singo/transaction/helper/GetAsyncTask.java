package com.ky.singo.transaction.helper;

import android.os.AsyncTask;
import android.util.Log;

import com.ky.singo.transaction.Web_GetTransaction;
import com.ky.singo.transaction.Web_Param;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * Created by mmyjh on 2017-05-23.
 */
public class GetAsyncTask extends AsyncTask<Web_Param, Void, String> {
  private final String ID_GETASYNCTASK_QUERY = "SINGO_GETASYNCTASK";
  String url;
  Web_GetTransaction transaction;

  GetAsyncTask(String url) {
    this.url = url;
    transaction = new Web_GetTransaction(url);
  }

  @Override
  protected String doInBackground(Web_Param... params) {
    Boolean isSuccess;

    // Todo
    Web_Param param = params[0];
    isSuccess = transaction.send(param);
    if (isSuccess) {
      final HttpEntity entity = transaction.getResponse().getEntity();
      try {
        String responseBody = EntityUtils.toString(entity);
        return responseBody;
      } catch (Exception e) {
        Log.d(ID_GETASYNCTASK_QUERY, e.toString());
        e.printStackTrace();
      }
    }
    return null;
  }
}