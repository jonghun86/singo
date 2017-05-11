package com.ky.singo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by mmyjh on 2017-05-06.
 */

public class ReportWrite_Activity extends AppCompatActivity {
  private final String ID_REPORT_WRITE_QUERY = "REPORT_WRITE";
  private static final int SELECT_PICTURE = 1;
  private String selectedImagePath;
  byte[] bitMapData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(ID_REPORT_WRITE_QUERY, "onCreate");
    setContentView(R.layout.reportwrite);

    Button reportViolationButton = (Button) findViewById(R.id.report_button);
    reportViolationButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ReportViolation();
      }
    });
    Button selectPicturefromGallary = (Button) findViewById(R.id.select_picture);
    selectPicturefromGallary.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SelectPicture();
      }
    });
  }

  private void SelectPicture() {

    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent,
            "Select Picture"), SELECT_PICTURE);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      if (requestCode == SELECT_PICTURE) {
        Uri selectedImageUri = data.getData();
        Log.d(ID_REPORT_WRITE_QUERY, data.getData().getPath());
        selectedImagePath = getPath(selectedImageUri);
        /* TODO: selectedImagePath is null, gallary should be media scanned */
        //Log.d(ID_REPORT_WRITE_QUERY, selectedImagePath);
        //Environment.getExternalStorageDirectory();
        /* TODO: temp image from resource drawable */
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_image);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bitMapData = stream.toByteArray();
        Log.d(ID_REPORT_WRITE_QUERY, "sample image size " + bitMapData.length);

        new UploadFile().execute();
      }
    }
  }
  public String getPath(Uri uri) {
    String[] projection = {MediaStore.Images.Media.DATA};
    Cursor cursor = managedQuery(uri, projection, null, null, null);
    startManagingCursor(cursor);
    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    return cursor.getString(columnIndex);
  }

  private void ReportViolation() {
   new ReportWriteTask().execute();
  }

  /* TODO: should be sync */
  public class ReportWriteTask extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... not_used) {
      final String url = "https://www.epeople.go.kr/jsp/user/pc/cvreq/UPcRecommendOrg.jsp";
      Boolean isSuccess;
      Web_PostTransaction postTransaction;
      ArrayList<NameValuePair> param;

      Log.d(ID_REPORT_WRITE_QUERY, "start report");
      // parameter
      param = new ArrayList<NameValuePair>();
      /* FIXME: conversion byte to string is so dangerous */

      param.add(new BasicNameValuePair("accuse_yn_c", "N"));
      param.add(new BasicNameValuePair("adr1_v", "test addr"));
      param.add(new BasicNameValuePair("adr2_v", "test_addr2"));
      param.add(new BasicNameValuePair("ancCheck", ""));
      param.add(new BasicNameValuePair("basic_chk",""));
      param.add(new BasicNameValuePair("basicOrg",	"3900000"));
      param.add(new BasicNameValuePair("cel_no_v",	"010-2378-3205"));
      param.add(new BasicNameValuePair("ChgBasicAnc",	"3900000"));
      param.add(new BasicNameValuePair("ChgSubAnc",	"6410000"));
      param.add(new BasicNameValuePair("corp_name_v",""));
      param.add(new BasicNameValuePair("corp_no_c",""));
      param.add(new BasicNameValuePair("corp_no_c1",""));
      param.add(new BasicNameValuePair("corp_no_c2",""));
      param.add(new BasicNameValuePair("corp_no_c3",""));
      param.add(new BasicNameValuePair("cvpl_se_c",	"80030001"));
      param.add(new BasicNameValuePair("dmge_adr1_v",""));
      param.add(new BasicNameValuePair("dmge_aplcnt_nm_v",""));
      param.add(new BasicNameValuePair("dmge_tel_no_v",""));
      param.add(new BasicNameValuePair("dmge_wrkplc_v",""));
      param.add(new BasicNameValuePair("domain",	"gmail.com"));
      param.add(new BasicNameValuePair("dupInfo",	"MC0GCCqGSIb3DQIJAyEAqpfr2ecwUkZQ1pugdusBdjP8Tb46ylwJMcAY76vPh1Q="));
      param.add(new BasicNameValuePair("email_v",	"6883205@gmail.com"));
      param.add(new BasicNameValuePair("evadeReasonFocusChk",""));
      param.add(new BasicNameValuePair("evadeTargetFocusChk",""));

      param.add(new BasicNameValuePair("flag",	"N"));
      param.add(new BasicNameValuePair("fOpenYn",	"N"));
      param.add(new BasicNameValuePair("getFocusPro1",	"1"));
      param.add(new BasicNameValuePair("getFocusPro2","1"));
      param.add(new BasicNameValuePair("grp_name",""));
      param.add(new BasicNameValuePair("grp3_peti_yn_c",	"N"));
      param.add(new BasicNameValuePair("hPersonallykind",""));
      param.add(new BasicNameValuePair("indvdlinfo_view_agre_yn_c",	"Y"));
      param.add(new BasicNameValuePair("jgubun", "1"));
      param.add(new BasicNameValuePair("jumin_no_c",""));
      param.add(new BasicNameValuePair("jumin1",""));
      param.add(new BasicNameValuePair("jumin2",""));
      param.add(new BasicNameValuePair("juminNo",""));
      param.add(new BasicNameValuePair("juso2Anc_Basic",	"3900000"));
      param.add(new BasicNameValuePair("juso2Anc_Sub",	"6410000"));
      param.add(new BasicNameValuePair("ls",	"10"));
      param.add(new BasicNameValuePair("mail",	"on"));
      param.add(new BasicNameValuePair("mail_attch_yn_c",	"Y"));
      param.add(new BasicNameValuePair("mail2", "on"));
      param.add(new BasicNameValuePair("mem_id_v",	"ggungnae"));
      param.add(new BasicNameValuePair("mem_native_yn_c",""));
      param.add(new BasicNameValuePair("memId","ggungnae"));
      param.add(new BasicNameValuePair("menu1","pc"));
      param.add(new BasicNameValuePair("menuGubun", "0"));
      param.add(new BasicNameValuePair("mode", ""));
      param.add(new BasicNameValuePair("mypeti_view_method_c", "1"));
      param.add(new BasicNameValuePair("occurrence_same_addr", "Y"));
      param.add(new BasicNameValuePair("open_yn_c",	"N"));
      param.add(new BasicNameValuePair("peter_cel_no_v1",	"010"));
      param.add(new BasicNameValuePair("peter_cel_no_v2",	"2378"));
      param.add(new BasicNameValuePair("peter_cel_no_v3",	"3205"));
      param.add(new BasicNameValuePair("peter_email_v2",	"gmail.com"));
      param.add(new BasicNameValuePair("peter_name_v","test_name"));
      param.add(new BasicNameValuePair("peter_tel_no_v1",""));
      param.add(new BasicNameValuePair("peter_tel_no_v2",""));
      param.add(new BasicNameValuePair("peter_tel_no_v3",""));
      param.add(new BasicNameValuePair("peti_nti_method_c",	"00570005"));
      param.add(new BasicNameValuePair("peti_path_gubun_c",	"00020011"));
      param.add(new BasicNameValuePair("peti_reason_l", "�׽�Ʈ ����(test contents)"));
      param.add(new BasicNameValuePair("peti_title_v","�׽�Ʈ ����(test topic)"));
      param.add(new BasicNameValuePair("primary_anc_code_v", ""));
      param.add(new BasicNameValuePair("proc_rcv_yn_c",	"N"));
      param.add(new BasicNameValuePair("prv_flag", ""));
      param.add(new BasicNameValuePair("residentNoCheckYn", ""));
      param.add(new BasicNameValuePair("scode",	"412103187038"));
      param.add(new BasicNameValuePair("sms",	"on"));
      param.add(new BasicNameValuePair("snsTokenMessage",	"%5B%EB%AF%BC%EC%9B%90%5D+%EB%AF%BC%EC%9B%90+%EC%8B%A0%EC%B2%AD"));
      param.add(new BasicNameValuePair("subOrg", "6410000"));
      param.add(new BasicNameValuePair("tel_no_v", ""));
      param.add(new BasicNameValuePair("third_person_sup_yn_c",	"Y"));
      param.add(new BasicNameValuePair("userName",	"test_name"));
      param.add(new BasicNameValuePair("zipcode_c",	"14225"));

      // send a packet
      postTransaction = new Web_PostTransaction(url);
      isSuccess = postTransaction.sendTest(param, null, bitMapData);
      if (isSuccess) {
        final HttpEntity entity = postTransaction.getResponse().getEntity();
        try {
          return EntityUtils.toString(entity);
        } catch (Exception e) {
          Log.d(ID_REPORT_WRITE_QUERY, e.toString());
          e.printStackTrace();
        }
      }
      return null;
    }
    protected void onPostExecute(final String responseBody) {
      Log.d(ID_REPORT_WRITE_QUERY, "##################################");
      Log.d(ID_REPORT_WRITE_QUERY, responseBody);
    }
  }


  /* TODO: this task should be not async but sync */
  public class UploadFile extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... not_used) {
      final String url = "https://www.epeople.go.kr/applex_wdigm/applet/jsp/fileup_cu_real.jsp";
      Boolean isSuccess;
      Web_PostTransaction postTransaction;
      ArrayList<NameValuePair> param;

      Log.d(ID_REPORT_WRITE_QUERY, "start upload");
      // parameter
      param = new ArrayList<NameValuePair>();
      /* TODO: conversion byte to string is so dangerous */
      param.add(new BasicNameValuePair("Content-Disposition: form-data; name=\"item_file[]\"; filename=\"sample_image.jpg\"\n" +
              "Content-Type: image/jpeg", bitMapData.toString()));

      // send a packet
      postTransaction = new Web_PostTransaction(url);
      isSuccess = postTransaction.send(param);
      if (isSuccess) {
        final HttpEntity entity = postTransaction.getResponse().getEntity();
        try {
          return EntityUtils.toString(entity);
        } catch (Exception e) {
          Log.d(ID_REPORT_WRITE_QUERY, e.toString());
          e.printStackTrace();
        }
      }
      return null;
    }
    protected void onPostExecute(final String responseBody) {
      Log.d(ID_REPORT_WRITE_QUERY, responseBody);
    }
  }
  /* TODO: one more submit (include vehicle number) */
}
