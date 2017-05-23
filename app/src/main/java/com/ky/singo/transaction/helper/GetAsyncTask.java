package com.ky.singo.transaction.helper;

/**
 * Created by mmyjh on 2017-05-23.


public class GetAsyncTask extends AsyncTask<ReportWrite_Activity.ReportData, Void, String> {
  String url;
  Web_GetTransaction transaction;

  GetAsyncTask(String url) {
    this.url = url;
    transaction = new Web_GetTransaction(url);
  }

  @Override
  protected String doInBackground(ReportWrite_Activity.ReportData... params) {
    Boolean isSuccess;

    // Todo
    ReportWrite_Activity.ReportData data = params[0];
    isSuccess = transaction.send(data.param);
    if (isSuccess) {
      final HttpEntity entity = transaction.getResponse().getEntity();
      try {
        String responseBody = EntityUtils.toString(entity);
        return responseBody;
      } catch (Exception e) {
        Log.d(ID_REPORT_WRITE_QUERY, e.toString());
        e.printStackTrace();
      }
    }
    return null;
  }
}
 */