package com.ky.singo.transaction.helper;

import android.os.AsyncTask;
import android.util.Log;

import com.ky.singo.transaction.Web_Param;
import com.ky.singo.transaction.Web_PostTransaction;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * Created by mmyjh on 2017-05-23.
 */



public class PostAsyncTask extends AsyncTask<Web_Param, Void, String> {
  private final String ID_POSTASYNCTASK_QUERY = "SINGO_POSTASYNCTASK";
  String url;
  Web_PostTransaction.TransactionType trType;
  Web_PostTransaction transaction;

  public PostAsyncTask(String url, Web_PostTransaction.TransactionType trType) {
    this.url = url;
    this.trType = trType;
    transaction = new Web_PostTransaction(url);
  }

  @Override
  protected String doInBackground(Web_Param... params) {
    Boolean isSuccess;

    // Todo
    Web_Param param = params[0];
    isSuccess = transaction.send(param, trType);
    if (isSuccess) {
      final HttpEntity entity = transaction.getResponse().getEntity();
      try {
        String responseBody = EntityUtils.toString(entity);
        return responseBody;
      } catch (Exception e) {
        Log.d(ID_POSTASYNCTASK_QUERY, e.toString());
        e.printStackTrace();
      }
    }
    return null;
  }
}