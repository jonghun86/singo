package com.ky.singo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
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

      // -------------------------------------------------------------------------------------------
      // STEP 1. 신청서 작성 및 유사사례 확인
      // 개인정보 수집 및 이용 안내
      param.add(new BasicNameValuePair("third_person_sup_yn_c",	"Y"));
      // 신청인 본인인증
      //  - 개인 N, 단체 Y, 기업 C
      //  - 신청인 이름
      param.add(new BasicNameValuePair("grp3_peti_yn_c",	"N"));
      param.add(new BasicNameValuePair("userName",	"유종훈"));

      // 신청인 기본정보
      // - 휴대전화
      param.add(new BasicNameValuePair("peter_cel_no_v1",	"010"));
      param.add(new BasicNameValuePair("peter_cel_no_v2",	""));
      param.add(new BasicNameValuePair("peter_cel_no_v3",	""));
      // - email
      param.add(new BasicNameValuePair("peter_email_v1",	""));
      param.add(new BasicNameValuePair("peter_email_v2",	"gmail.com"));
      param.add(new BasicNameValuePair("domain",	"gmail.com"));
      // - 주소
      param.add(new BasicNameValuePair("zipcode_c",	"06762"));
      param.add(new BasicNameValuePair("adr1_v", "서울특별시 서초구 바우뫼로11길 76,"));
      param.add(new BasicNameValuePair("adr2_v", "104호"));

      // - 민원발생 지역
      //    + 주소와 동일한 지역인가?
      //    + 시도
      //    + 시군구
      param.add(new BasicNameValuePair("occurrence_same_addr", "N"));
      param.add(new BasicNameValuePair("subOrg", "6110000"));
      param.add(new BasicNameValuePair("basicOrg",	"3210000"));

      // 나의 민원 확인 방식
      //  - 1 : 간편형
      //  - 2 : 보안형
      param.add(new BasicNameValuePair("mypeti_view_method_c", "1"));

      // 민원 내용
      //  - 제목
      param.add(new BasicNameValuePair("peti_title_v", "테스트 - 제목"));
      param.add(new BasicNameValuePair("getFocusPro1", "1"));
      //  - 내용
      param.add(new BasicNameValuePair("peti_reason_l", "테스트 - 내용"));
      param.add(new BasicNameValuePair("getFocusPro2", "1"));

      // 귀하께서 위 민원과 동일 내용의 민원을 이미 행정기관 등에 제출하여 그 처리결과를 받은 적이 있습니까?
      param.add(new BasicNameValuePair("proc_rcv_yn_c",	"N"));
      // 귀하께서 제출하실 민원에 제보, 고발성 내용을 포함하고 있습니까?
      param.add(new BasicNameValuePair("accuse_yn_c", "N"));
      // 귀하의 민원신청 내용을 공유하는 것에 동의하십니까?
      param.add(new BasicNameValuePair("open_yn_c",	"N"));

      // We don't know usage of hidden value
      param.add(new BasicNameValuePair("flag",	"N"));
      param.add(new BasicNameValuePair("menuGubun", "0"));
      param.add(new BasicNameValuePair("menu1","pc"));
      param.add(new BasicNameValuePair("peti_no_c",""));
      param.add(new BasicNameValuePair("mem_id_v",	"mmyjh86"));
      param.add(new BasicNameValuePair("peti_path_gubun_c",	"00020011"));
      param.add(new BasicNameValuePair("email_v",	"mmyjh86@gmail.com"));
      // Request code
      //
      // - 00570008 : 전자우편,sms,서면
      // - 00570005 : 전자우편, sms
      // - 00570006 : 전자우편, 서면
      // - 00570007 : sms, 서면
      // - 00570002 : 전자우편
      // - 00570004 : sms
      // - 00570001 : 서면
      // - 00570003 : 나머지
      param.add(new BasicNameValuePair("peti_nti_method_c",	"00570003"));
      // WTF?
      param.add(new BasicNameValuePair("ls",	"10"));
      param.add(new BasicNameValuePair("fOpenYn",	"N"));
      // road addr code ??
      param.add(new BasicNameValuePair("scode",	"116504163196"));
      // jibun addr or road addr
      param.add(new BasicNameValuePair("jgubun", "1"));
      // second setting?
      // what is difference between memId and mem_id_v
      param.add(new BasicNameValuePair("memId","mmyjh86"));
      // WTF?
      param.add(new BasicNameValuePair("dupInfo",	"MC0GCCqGSIb3DQIJAyEA9ynrPjBrDb/LVDT8f/lW6XO62LtAufyVn3GupPV0rIk="));

      // WTF?
      // who is peter?
      // Korean name??
      param.add(new BasicNameValuePair("peter_name_v","유종훈"));
      // WTF? - maybe 경기도 code
      param.add(new BasicNameValuePair("juso2Anc_Sub",	"6110000"));
      //code":"3900000","name":"광명시"
      param.add(new BasicNameValuePair("juso2Anc_Basic",	"3210000"));
      param.add(new BasicNameValuePair("ChgSubAnc",	"6110000"));
      param.add(new BasicNameValuePair("ChgBasicAnc",	"3210000"));
      // I don't know what it is. But it seems to be set to same value in all query
      // encoded string for "[민원] 민원 신청" [https://meyerweb.com/eric/tools/decoder/]
      // maybe it is for sns sharing.
      param.add(new BasicNameValuePair("snsTokenMessage",	"%5B%EB%AF%BC%EC%9B%90%5D+%EB%AF%BC%EC%9B%90+%EC%8B%A0%EC%B2%AD"));

      // 고정 값 - 사용이유 모름
      param.add(new BasicNameValuePair("indvdlinfo_view_agre_yn_c",	"Y"));
      param.add(new BasicNameValuePair("mail_attch_yn_c",	"N"));
      param.add(new BasicNameValuePair("cvpl_se_c",	"80030001"));
      //param.add(new BasicNameValuePair("sms",	"on"));



      // empty
      param.add(new BasicNameValuePair("mode",	""));
      param.add(new BasicNameValuePair("jumin_no_c",	""));
      param.add(new BasicNameValuePair("mem_native_yn_c",	""));
      param.add(new BasicNameValuePair("tel_no_v",	""));
      param.add(new BasicNameValuePair("cel_no_v",	""));
      param.add(new BasicNameValuePair("basic_chk",	""));
      param.add(new BasicNameValuePair("evadeTargetFocusChk",	""));
      param.add(new BasicNameValuePair("evadeReasonFocusChk",	""));
      param.add(new BasicNameValuePair("corp_no_c",	""));
      param.add(new BasicNameValuePair("juminNo",	""));
      param.add(new BasicNameValuePair("residentNoCheckYn",	""));
      param.add(new BasicNameValuePair("jumin1",	""));
      param.add(new BasicNameValuePair("jumin2",	""));
      param.add(new BasicNameValuePair("hPersonallykind",	""));
      param.add(new BasicNameValuePair("prv_flag",	""));
      param.add(new BasicNameValuePair("corp_name_v",	""));
      param.add(new BasicNameValuePair("corp_no_c1",	""));
      param.add(new BasicNameValuePair("corp_no_c2",	""));
      param.add(new BasicNameValuePair("corp_no_c3",	""));
      param.add(new BasicNameValuePair("grp_name",	""));
      param.add(new BasicNameValuePair("peter_tel_no_v1",	""));
      param.add(new BasicNameValuePair("peter_tel_no_v2",	""));
      param.add(new BasicNameValuePair("peter_tel_no_v3",	""));
      param.add(new BasicNameValuePair("primary_anc_code_v",	""));
      param.add(new BasicNameValuePair("dmge_aplcnt_nm_v",	""));
      param.add(new BasicNameValuePair("dmge_wrkplc_v",	""));
      param.add(new BasicNameValuePair("dmge_adr1_v",	""));
      param.add(new BasicNameValuePair("dmge_tel_no_v",	""));
      param.add(new BasicNameValuePair("ancCheck",	""));


      // send a packet
      postTransaction = new Web_PostTransaction(url);
      isSuccess = postTransaction.send(param, bitMapData, "file1");
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
      Log.d(ID_REPORT_WRITE_QUERY, "#############");
      new ReportWriteTask_Mid().execute();

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

      // send a packet
      postTransaction = new Web_PostTransaction(url);
      isSuccess = postTransaction.send(param, bitMapData, "item_file[]");
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

  public class ReportWriteTask_Mid extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... not_used) {
      final String url = "https://www.epeople.go.kr/onto/manager/OntoUpAncCode.jsp";
      Boolean isSuccess;
      Web_PostTransaction postTransaction;
      ArrayList<NameValuePair> param;

      Log.d(ID_REPORT_WRITE_QUERY, "start mid report");

      SystemClock.sleep(2000);

      param = new ArrayList<NameValuePair>();
      param.add(new BasicNameValuePair("ADR1_V", "서울특별시 서초구"));
      param.add(new BasicNameValuePair("PETI_TITLE_V", "테스트 - 제목"));
      param.add(new BasicNameValuePair("PETI_REASON_L", "테스트 - 내용"));
      //FIXME
      param.add(new BasicNameValuePair("PETI_DOCUMENT", "13900000;sample_image.jpg;"));
      //
      param.add(new BasicNameValuePair("PM_FLAG", "80030001"));

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
      return "ERROR";
    }
    @Override
    protected void onPostExecute(final String responseBody) {
        Log.d(ID_REPORT_WRITE_QUERY, responseBody);
        Log.d(ID_REPORT_WRITE_QUERY, "#############");
      new ReportWriteTask_Final().execute();
    }
  }

  /* TODO: should be sync */
  public class ReportWriteTask_Final extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... not_used) {
      final String url = "https://www.epeople.go.kr/jsp/user/pc/cvreq/UPcRecommendOrg.jsp";
      Boolean isSuccess;
      Web_PostTransaction postTransaction;
      ArrayList<NameValuePair> param;

      Log.d(ID_REPORT_WRITE_QUERY, "start final report");

      SystemClock.sleep(2000);


      // parameter
      param = new ArrayList<NameValuePair>();
      //param.add(new BasicNameValuePair("third_person_sup_yn_c",	"Y"));
      /* FIXME: conversion byte to string is so dangerous */
      param.add(new BasicNameValuePair("mode", "createPcCvreq"));
      param.add(new BasicNameValuePair("flag", "null"));
      param.add(new BasicNameValuePair("detailView", "Y"));
      param.add(new BasicNameValuePair("menuGubun", "0"));
      param.add(new BasicNameValuePair("menu1"	, "pc"));
      param.add(new BasicNameValuePair("peti_no_c", ""));
      param.add(new BasicNameValuePair("mem_id_v", "mmyjh86"));
      param.add(new BasicNameValuePair("anc_code_v", "1320000"));
      param.add(new BasicNameValuePair("civil_again_c", "00600001"));
      param.add(new BasicNameValuePair("sect_error_yn_c", "Y"));
      param.add(new BasicNameValuePair("p_sect_error_yn_c", "Y"));
      param.add(new BasicNameValuePair("peti_path_gubun_c", "00020011"));
      param.add(new BasicNameValuePair("peter_name_v"	, "유종훈"));
      param.add(new BasicNameValuePair("mem_native_yn_c", ""));
      param.add(new BasicNameValuePair("zipcode_c", "06762"));
      param.add(new BasicNameValuePair("adr1_v", "서울특별시 서초구 바우뫼로11길 76,"));
      param.add(new BasicNameValuePair("adr2_v", "104호"));
      param.add(new BasicNameValuePair("peti_job_c", ""));
      param.add(new BasicNameValuePair("tel_no_v", ""));
      param.add(new BasicNameValuePair("cel_no_v", ""));
      param.add(new BasicNameValuePair("email_v", "mmyjh86@gmail.com"));
      param.add(new BasicNameValuePair("open_yn_c", "N"));
      param.add(new BasicNameValuePair("peti_title_v", "테스트 - 제목"));
      param.add(new BasicNameValuePair("peti_reason_l", "테스트 - 내용"));
      param.add(new BasicNameValuePair("peti_nti_method_c", "00570003"));
      param.add(new BasicNameValuePair("passwd_v", ""));
      param.add(new BasicNameValuePair("civil_rel_name_v", ""));
      param.add(new BasicNameValuePair("civil_rel_tel_no_v", ""));
      param.add(new BasicNameValuePair("civil_rel_zipcode_c", ""));
      param.add(new BasicNameValuePair("civil_rel_addr_v", ""));
      param.add(new BasicNameValuePair("civil_rel_addr1_v", ""));
      param.add(new BasicNameValuePair("file1_name",	"sample_image.jpg"));
      param.add(new BasicNameValuePair("file2_name", ""));
      param.add(new BasicNameValuePair("file3_name", ""));
      param.add(new BasicNameValuePair("file4_name", ""));
      param.add(new BasicNameValuePair("file5_name", ""));
      param.add(new BasicNameValuePair("file1_size", "153240"));
      param.add(new BasicNameValuePair("file2_size", ""));
      param.add(new BasicNameValuePair("file3_size", ""));
      param.add(new BasicNameValuePair("file4_size", ""));
      param.add(new BasicNameValuePair("file5_size", ""));
      //FIXME - 날짜별로 업로드 경로가 다름
      param.add(new BasicNameValuePair("file1", "/attach04/2017/5/20/jsp/user/pc/cvreq/UPcRecommendOrg.jsp/sample_image.jpg"));
      param.add(new BasicNameValuePair("file2"	, ""));
      param.add(new BasicNameValuePair("file3", ""));
      param.add(new BasicNameValuePair("file4", ""));
      param.add(new BasicNameValuePair("file5", ""));
      //FIXME - 날짜별로 업로드 경로가 다름
      param.add(new BasicNameValuePair("file1_dir", "/attach04/2017/5/20/jsp/user/pc/cvreq/UPcRecommendOrg.jsp/"));
      param.add(new BasicNameValuePair("file2_dir", ""));
      param.add(new BasicNameValuePair("file3_dir", ""));
      param.add(new BasicNameValuePair("file4_dir", ""));
      param.add(new BasicNameValuePair("file5_dir", ""));
      //FIXME - 날짜별로 업로드 경로가 다름
      param.add(new BasicNameValuePair("file1_temp_dir", "/attach04/temp/2017/5/20/jsp/user/pc/cvreq/UPcRecommendOrg.jsp/sample_image.jpg"));
      param.add(new BasicNameValuePair("file2_temp_dir", ""));
      param.add(new BasicNameValuePair("file3_temp_dir", ""));
      param.add(new BasicNameValuePair("file4_temp_dir", ""));
      param.add(new BasicNameValuePair("file5_temp_dir", ""));
      param.add(new BasicNameValuePair("open_chk_c", ""));
      param.add(new BasicNameValuePair("grp3_peti_yn_c", "N"));
      param.add(new BasicNameValuePair("grp_name", ""));
      param.add(new BasicNameValuePair("mail_attch_yn_c", "Y"));
      param.add(new BasicNameValuePair("anc_c2", "1140100"));
      param.add(new BasicNameValuePair("auto_anc_v", "1140100"));
      param.add(new BasicNameValuePair("peti_anc_v", "1320000"));
      param.add(new BasicNameValuePair("pre_peti_no_c", ""));
      param.add(new BasicNameValuePair("miss_yn_c", "N"));
      param.add(new BasicNameValuePair("peti_ip_v", "124.53.50.34"));
      param.add(new BasicNameValuePair("anc_name_v", "경찰청"));
      param.add(new BasicNameValuePair("multi_anc_yn_c", "N"));
      param.add(new BasicNameValuePair("org_auto_anc_c", "1140100"));
      param.add(new BasicNameValuePair("multi_status_c", ""));
      param.add(new BasicNameValuePair("ombuds", ""));
      param.add(new BasicNameValuePair("org_civil_again_c", ""));
      param.add(new BasicNameValuePair("manual_acrc_yn_c", "N"));
      param.add(new BasicNameValuePair("manual_yn_c", "Y"));
      param.add(new BasicNameValuePair("auto_yn_c", "N"));
      param.add(new BasicNameValuePair("upAncCode", "1140100"));
      param.add(new BasicNameValuePair("seq_n", ""));
      param.add(new BasicNameValuePair("retFAQ", "0"));
      param.add(new BasicNameValuePair("occurrence_area", ""));
      param.add(new BasicNameValuePair("subOrg", "6110000"));
      param.add(new BasicNameValuePair("basicOrg", "3210000"));
      param.add(new BasicNameValuePair("occurrence_same_addr", "Y"));
      param.add(new BasicNameValuePair("corp_no_c", ""));
      param.add(new BasicNameValuePair("corp_name_v", ""));
      param.add(new BasicNameValuePair("fOpenYn", "N"));
      //FIXME : 파일 아이디 받아오는 부분을 모르겠음
      //바로 전 리퀘스트에 쓰이는 부분임
      param.add(new BasicNameValuePair("file1_id", "13700034"));
      //
      param.add(new BasicNameValuePair("file2_id"	, ""));
      param.add(new BasicNameValuePair("file3_id", ""));
      param.add(new BasicNameValuePair("file4_id", ""));
      param.add(new BasicNameValuePair("file5_id", ""));
      param.add(new BasicNameValuePair("mypeti_view_method_c", "1"));
      param.add(new BasicNameValuePair("scode", "116504163196"));
      param.add(new BasicNameValuePair("jgubun", "1"));
      param.add(new BasicNameValuePair("cvpl_se_c", "80030001"));
      param.add(new BasicNameValuePair("dmge_aplcnt_nm_v", ""));
      param.add(new BasicNameValuePair("dmge_wrkplc_v", ""));
      param.add(new BasicNameValuePair("dmge_adr1_v", ""));
      param.add(new BasicNameValuePair("dmge_tel_no_v", ""));
      param.add(new BasicNameValuePair("jumin_no_c", ""));
      param.add(new BasicNameValuePair("dupInfo", ""));
      param.add(new BasicNameValuePair("third_person_sup_yn_c", "Y"));
      param.add(new BasicNameValuePair("hPersonallykind", ""));
      param.add(new BasicNameValuePair("evadeTargerCode", ""));
      param.add(new BasicNameValuePair("evadeTargerCode1", ""));
      param.add(new BasicNameValuePair("evadeReasonFocusChk", ""));
      param.add(new BasicNameValuePair("primary_anc_code_v", ""));
      param.add(new BasicNameValuePair("proc_rcv_yn_c", "N"));
      param.add(new BasicNameValuePair("prv_flag", ""));
      param.add(new BasicNameValuePair("ancCheck", ""));
      param.add(new BasicNameValuePair("corp_no_c1", ""));
      param.add(new BasicNameValuePair("corp_no_c2", ""));
      param.add(new BasicNameValuePair("corp_no_c3", ""));
      param.add(new BasicNameValuePair("consent_yn", ""));
      param.add(new BasicNameValuePair("onto_use_yn", "0"));
      param.add(new BasicNameValuePair("safe_civil_rate", ""));
      param.add(new BasicNameValuePair("better_civil_rate", ""));
      param.add(new BasicNameValuePair("accuse_yn_c", "N"));
      param.add(new BasicNameValuePair("indvdlinfo_view_agre_yn_c", "Y"));
      param.add(new BasicNameValuePair("snsTokenMessage", "%EC%A0%95%EC%9D%98%ED%95%84%EC%9A%94"));
      param.add(new BasicNameValuePair("chkNotSelectedYn", "N"));
      param.add(new BasicNameValuePair("evade_yn_c", "N"));
      param.add(new BasicNameValuePair("evade_target_v", ""));
      param.add(new BasicNameValuePair("evade_reason_v", ""));
      param.add(new BasicNameValuePair("trafic_violtn_yn_c", ""));
      param.add(new BasicNameValuePair("vhcle_no_v", "TEST Vehicle"));
      param.add(new BasicNameValuePair("police_peti_gubun_c", "100"));
      //?
      param.add(new BasicNameValuePair("add_vhcle_no_v", ""));
      param.add(new BasicNameValuePair("search_vhcle_no_yn", "N"));
      param.add(new BasicNameValuePair("birthdoayMenuOpt", "Y"));



      // send a packet
      postTransaction = new Web_PostTransaction(url);
      isSuccess = postTransaction.send(param, bitMapData, "");
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
      Log.d(ID_REPORT_WRITE_QUERY, "#############");
    }
  }
}
