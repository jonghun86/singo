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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.singo.transaction.Web_GetTransaction;
import com.ky.singo.transaction.Web_PostTransaction;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/* TODO List
 * 1. Upload/Submit 시 Progress Bar
 * 2. 하드코딩 -> 변수화
 * 3. 로그인 각자 아이디로
 */

/**
 * Created by mmyjh on 2017-05-06.
 */

public class ReportWrite_Activity extends AppCompatActivity {
  final String fileUploadUrl    = "https://www.epeople.go.kr/applex_wdigm/applet/jsp/fileup_cu_real.jsp";
  final String contentUploadUrl = "https://www.epeople.go.kr/jsp/user/pc/cvreq/UPcRecommendOrg.jsp";
  final String summaryUploadUrl = "https://www.epeople.go.kr/onto/ajax/ajax_onto_recommand_req.jsp";
  final String dupInfoUrl       = "http://www.epeople.go.kr/jsp/user/pc/cvreq/UPcCvreqForm.jsp";

  // TODO Remove global member variable
  String fileId;
  private EditText nameEditText;
  private EditText addr1EditText;
  private EditText addr2EditText;
  private EditText emailEditText;
  private EditText singoSubjectEditText;
  private EditText singoContentEditText;
  private EditText vehicleEditText;
  private ProgressBar progressBar;

  private final String ID_REPORT_WRITE_QUERY = "REPORT_WRITE";
  private static final int SELECT_PICTURE = 1;
  private String selectedImagePath;
  byte[] bitMapData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(ID_REPORT_WRITE_QUERY, "onCreate");
    setContentView(R.layout.reportwrite);

    /* Resource init */
    nameEditText = (EditText) findViewById(R.id.name_field);
    addr1EditText = (EditText) findViewById(R.id.Addr1_field);
    addr2EditText = (EditText) findViewById(R.id.addr2_field);
    emailEditText = (EditText) findViewById(R.id.email_field);
    singoSubjectEditText = (EditText) findViewById(R.id.subject_field);
    singoContentEditText = (EditText) findViewById(R.id.contents_field);
    vehicleEditText = (EditText) findViewById(R.id.vehicle_field);
    progressBar = (ProgressBar) findViewById(R.id.progressBar_submit);

    /* invisible progressbar */
    progressBar.setVisibility(View.INVISIBLE);

    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_image);
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    bitMapData = stream.toByteArray();

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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
      }
    });
  }

  private String getPath(Uri uri) {
    String[] projection = {MediaStore.Images.Media.DATA};
    Cursor cursor = managedQuery(uri, projection, null, null, null);
    startManagingCursor(cursor);
    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    return cursor.getString(columnIndex);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      if (requestCode == SELECT_PICTURE) {
        Uri selectedImageUri = data.getData();
        Log.d(ID_REPORT_WRITE_QUERY, data.getData().getPath());
        selectedImagePath = getPath(selectedImageUri);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_image);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bitMapData = stream.toByteArray();

      }
    }
    else {
      Log.d(ID_REPORT_WRITE_QUERY, "Fail to get images/videos");
      Toast.makeText(getApplicationContext(), "Fail to get images/videos", Toast.LENGTH_SHORT);
    }
  }



  private boolean ReportViolation() {
    ReportUploadPostTask uploadTask;
    ReportUploadGetTask uploadGetTask;
    ArrayList<NameValuePair> param;
    ReportData reportData;
    String responseBody;
    Document doc;
    Element element;

    // 동영상 or 사진 첨부를 누락한 경우
    if (bitMapData == null) {
      Toast.makeText(getApplicationContext(), "사진/동영상이 첨부되지 않았습니다.",
        Toast.LENGTH_SHORT).show();
      return false;
    }
    Log.d(ID_REPORT_WRITE_QUERY, "Upload media files");


    try {
      uploadGetTask = new ReportUploadGetTask(dupInfoUrl);
      param = new ArrayList<NameValuePair>();
      param.add(new BasicNameValuePair("flag", "N"));
      reportData = new ReportData(param, null, null);
      responseBody  = uploadGetTask.execute(reportData).get();

      doc = Jsoup.parse(responseBody);
      element = doc.select("input[name=dupInfo]").first();
      Log.d("##", element.attr("value"));

      /////////////////////////////////////////////////////////////////////////////////////////////
      // Upload Video Files
      uploadTask    = new ReportUploadPostTask(fileUploadUrl);
      param         = getPredefinedParam(PostReqType.MULTIMEDIA_UPLOAD);
      reportData    = new ReportData(param, "item_file[]", bitMapData);
      responseBody  = uploadTask.execute(reportData).get();
      if (responseBody == null) {
        Toast.makeText(getApplicationContext(), "사진/동영상 업로드에 실패했습니다.",
          Toast.LENGTH_SHORT).show();
        return false;
      }

      /////////////////////////////////////////////////////////////////////////////////////////////
      //
      param         = getPredefinedParam(PostReqType.CONTENTS_REQUEST);
      reportData    = new ReportData(param, "file1", bitMapData);

      uploadTask    = new ReportUploadPostTask(contentUploadUrl);
      responseBody  = uploadTask.execute(reportData).get();
      // [HACK] Response is arrived but server sent error because of wrong request. Server sent
      // message "정상적인 접근이 아닙니다" in response body.
      if (responseBody.length() < 500) {
        Log.d(ID_REPORT_WRITE_QUERY, "Abnormal access (CONTENTS_REQUEST)");
        Toast.makeText(getApplicationContext(), "정상적인 접근이 아닙니다.",
          Toast.LENGTH_SHORT).show();
        return false;
      }

      doc = Jsoup.parse(responseBody);
      element = doc.select("input[name=file1_id]").first();
      fileId = element.attr("value");
      Log.d("##", element.attr("value"));

      /////////////////////////////////////////////////////////////////////////////////////////////
      //
      uploadTask    = new ReportUploadPostTask(summaryUploadUrl);
      param         = getPredefinedParam(PostReqType.CONTENTS_SUMMARY);
      reportData    = new ReportData(param, null, null);
      responseBody  = uploadTask.execute(reportData).get();
      if (responseBody == null) {
        Toast.makeText(getApplicationContext(), "------------------.",
          Toast.LENGTH_SHORT).show();
        return false;
      }
      Log.d("----1", responseBody);

      /////////////////////////////////////////////////////////////////////////////////////////////
      //
      uploadTask    = new ReportUploadPostTask(contentUploadUrl);
      param         = getPredefinedParam(PostReqType.CONTENTS_WRITE);
      reportData    = new ReportData(param, null, null);
      responseBody  = uploadTask.execute(reportData).get();
      if (responseBody == null) {
        Toast.makeText(getApplicationContext(), "########",
          Toast.LENGTH_SHORT).show();
        return false;
      }    /* */
      Log.d("----2", responseBody);


      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }


  private class ReportData {
    String mediaTag;
    byte [] mediaSource;
    ArrayList<NameValuePair> param;
    ReportData(ArrayList<NameValuePair> param, String mediaTag, byte [] mediaSource) {
      this.mediaTag = mediaTag;
      this.mediaSource = mediaSource;
      this.param = param;
    }
  }

  public class ReportUploadPostTask extends AsyncTask<ReportData, Void, String> {
    String url;
    Web_PostTransaction transaction;

    ReportUploadPostTask(String url) {
      this.url = url;
      transaction = new Web_PostTransaction(url);
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String doInBackground(ReportData... params) {
      Boolean isSuccess;

      // Todo
      ReportData data = params[0];
      if (data.mediaTag == null | data.mediaSource == null) {
        isSuccess = transaction.send(data.param);
      }
      else {
        isSuccess = transaction.send(data.param, data.mediaSource, data.mediaTag);
      }
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

  public class ReportUploadGetTask extends AsyncTask<ReportData, Void, String> {
    String url;
    Web_GetTransaction transaction;

    ReportUploadGetTask(String url) {
      this.url = url;
      transaction = new Web_GetTransaction(url);
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String doInBackground(ReportData... params) {
      Boolean isSuccess;

      // Todo
      ReportData data = params[0];
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


  public enum PostReqType {
    // FIXME : 이름 바꿀것
    MULTIMEDIA_UPLOAD,
    CONTENTS_REQUEST,
    CONTENTS_SUMMARY,
    CONTENTS_WRITE
  }

  ArrayList<NameValuePair> getPredefinedParam (PostReqType type) {
    ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

    switch (type) {
      case CONTENTS_REQUEST:
        // -------------------------------------------------------------------------------------------
        // STEP 1. 신청서 작성 및 유사사례 확인
        // 개인정보 수집 및 이용 안내
        param.add(new BasicNameValuePair("third_person_sup_yn_c",	"Y"));
        // 신청인 본인인증
        //  - 개인 N, 단체 Y, 기업 C
        //  - 신청인 이름
        param.add(new BasicNameValuePair("grp3_peti_yn_c",	"N"));
        param.add(new BasicNameValuePair("userName",	nameEditText.getText().toString()));
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
        //FIXME - zipcode
        param.add(new BasicNameValuePair("zipcode_c",	"06762"));
        param.add(new BasicNameValuePair("adr1_v", addr1EditText.getText().toString()));
        param.add(new BasicNameValuePair("adr2_v", addr2EditText.getText().toString()));
        // - 민원발생 지역
        //    + 주소와 동일한 지역인가?
        //    + 시도
        //    + 시군구
        //FIXME : 시도/시군구 코드는 받아오는 코드를 짜야 함
        param.add(new BasicNameValuePair("occurrence_same_addr", "N"));
        param.add(new BasicNameValuePair("subOrg", "6110000"));
        param.add(new BasicNameValuePair("basicOrg",	"3210000"));
        // 나의 민원 확인 방식
        //  - 1 : 간편형
        //  - 2 : 보안형
        param.add(new BasicNameValuePair("mypeti_view_method_c", "1"));
        // 민원 내용
        //  - 제목
        param.add(new BasicNameValuePair("peti_title_v", singoSubjectEditText.getText().toString()));
        param.add(new BasicNameValuePair("getFocusPro1", "1"));
        //  - 내용
        param.add(new BasicNameValuePair("peti_reason_l", singoContentEditText.getText().toString()));
        param.add(new BasicNameValuePair("getFocusPro2", "1"));
        // 귀하께서 위 민원과 동일 내용의 민원을 이미 행정기관 등에 제출하여 그 처리결과를 받은 적이 있습니까?
        param.add(new BasicNameValuePair("proc_rcv_yn_c",	"N"));
        // 귀하께서 제출하실 민원에 제보, 고발성 내용을 포함하고 있습니까?
        param.add(new BasicNameValuePair("accuse_yn_c", "N"));
        // 귀하의 민원신청 내용을 공유하는 것에 동의하십니까?
        param.add(new BasicNameValuePair("open_yn_c",	"N"));
        // We don't know usage of hidden values
        param.add(new BasicNameValuePair("flag",	"N"));
        param.add(new BasicNameValuePair("menuGubun", "0"));
        param.add(new BasicNameValuePair("menu1","pc"));
        param.add(new BasicNameValuePair("peti_no_c",""));
        param.add(new BasicNameValuePair("mem_id_v",	GlobalVar.getId()));
        param.add(new BasicNameValuePair("peti_path_gubun_c",	"00020011"));
        param.add(new BasicNameValuePair("email_v", emailEditText.getText().toString()));
        // Request code
        // - 00570008 : 전자우편,sms,서면
        // - 00570005 : 전자우편, sms
        // - 00570006 : 전자우편, 서면
        // - 00570007 : sms, 서면
        // - 00570002 : 전자우편
        // - 00570004 : sms
        // - 00570001 : 서면
        // - 00570003 : 나머지
        param.add(new BasicNameValuePair("peti_nti_method_c",	"00570003"));
        // ??
        param.add(new BasicNameValuePair("ls",	"10"));
        param.add(new BasicNameValuePair("fOpenYn",	"N"));
        // road addr code ??
        // 도로명, 지번주소관한 것들 다 쿼리로 받아와서 코드 넣어야 함
        param.add(new BasicNameValuePair("scode",	"116504163196"));
        // jibun addr or road addr
        param.add(new BasicNameValuePair("jgubun", "1"));
        // second setting?
        // what is difference between memId and mem_id_v
        param.add(new BasicNameValuePair("memId", GlobalVar.getId()));
        // WTF?
        param.add(new BasicNameValuePair("dupInfo",	"MC0GCCqGSIb3DQIJAyEAqpfr2ecwUkZQ1pugdusBdjP8Tb46ylwJMcAY76vPh1Q="));
        param.add(new BasicNameValuePair("peter_name_v", nameEditText.getText().toString()));
        // WTF? - maybe 경기도 code
        //FIXME - 주소 코드 받아와야 함
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
        break;
      case CONTENTS_SUMMARY :
        param.add(new BasicNameValuePair("ADR1_V", addr1EditText.getText().toString()));
        param.add(new BasicNameValuePair("PETI_TITLE_V", singoSubjectEditText.getText().toString()));
        param.add(new BasicNameValuePair("PETI_REASON_L", singoContentEditText.getText().toString()));
        //FIXME - 사진
        param.add(new BasicNameValuePair("PETI_DOCUMENT", "13900000;sample_image.jpg;"));
        param.add(new BasicNameValuePair("PM_FLAG", "80030001"));
        break;
      case CONTENTS_WRITE :
        param.add(new BasicNameValuePair("mode", "createPcCvreq"));
        param.add(new BasicNameValuePair("flag", "null"));
        param.add(new BasicNameValuePair("detailView", "Y"));
        param.add(new BasicNameValuePair("menuGubun", "0"));
        param.add(new BasicNameValuePair("menu1"	, "pc"));
        param.add(new BasicNameValuePair("peti_no_c", ""));
        param.add(new BasicNameValuePair("mem_id_v", GlobalVar.getId()));
        param.add(new BasicNameValuePair("anc_code_v", "1320000"));
        param.add(new BasicNameValuePair("civil_again_c", "00600001"));
        param.add(new BasicNameValuePair("sect_error_yn_c", "Y"));
        param.add(new BasicNameValuePair("p_sect_error_yn_c", "Y"));
        param.add(new BasicNameValuePair("peti_path_gubun_c", "00020011"));
        param.add(new BasicNameValuePair("peter_name_v", nameEditText.getText().toString()));
        param.add(new BasicNameValuePair("mem_native_yn_c", ""));
        param.add(new BasicNameValuePair("zipcode_c", "06762"));
        param.add(new BasicNameValuePair("adr1_v", addr1EditText.getText().toString()));
        param.add(new BasicNameValuePair("adr2_v", addr2EditText.getText().toString()));
        param.add(new BasicNameValuePair("peti_job_c", ""));
        param.add(new BasicNameValuePair("tel_no_v", ""));
        param.add(new BasicNameValuePair("cel_no_v", ""));
        param.add(new BasicNameValuePair("email_v", emailEditText.getText().toString()));
        param.add(new BasicNameValuePair("open_yn_c", "N"));
        param.add(new BasicNameValuePair("peti_title_v", singoSubjectEditText.getText().toString()));
        param.add(new BasicNameValuePair("peti_reason_l", singoContentEditText.getText().toString()));
        param.add(new BasicNameValuePair("peti_nti_method_c", "00570003"));
        param.add(new BasicNameValuePair("passwd_v", ""));
        param.add(new BasicNameValuePair("civil_rel_name_v", ""));
        param.add(new BasicNameValuePair("civil_rel_tel_no_v", ""));
        param.add(new BasicNameValuePair("civil_rel_zipcode_c", ""));
        param.add(new BasicNameValuePair("civil_rel_addr_v", ""));
        param.add(new BasicNameValuePair("civil_rel_addr1_v", ""));
        //FIXME
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
        param.add(new BasicNameValuePair("file1", "/attach04/2017/5/23/jsp/user/pc/cvreq/UPcRecommendOrg.jsp/sample_image.jpg"));
        param.add(new BasicNameValuePair("file2"	, ""));
        param.add(new BasicNameValuePair("file3", ""));
        param.add(new BasicNameValuePair("file4", ""));
        param.add(new BasicNameValuePair("file5", ""));
        //FIXME - 날짜별로 업로드 경로가 다름
        param.add(new BasicNameValuePair("file1_dir", "/attach04/2017/5/23/jsp/user/pc/cvreq/UPcRecommendOrg.jsp/"));
        param.add(new BasicNameValuePair("file2_dir", ""));
        param.add(new BasicNameValuePair("file3_dir", ""));
        param.add(new BasicNameValuePair("file4_dir", ""));
        param.add(new BasicNameValuePair("file5_dir", ""));
        //FIXME - 날짜별로 업로드 경로가 다름
        param.add(new BasicNameValuePair("file1_temp_dir", "/attach04/temp/2017/5/23/jsp/user/pc/cvreq/UPcRecommendOrg.jsp/sample_image.jpg"));
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
        param.add(new BasicNameValuePair("file1_id", fileId));
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
        param.add(new BasicNameValuePair("vhcle_no_v", vehicleEditText.getText().toString()));
        param.add(new BasicNameValuePair("police_peti_gubun_c", "100"));
        //?
        param.add(new BasicNameValuePair("add_vhcle_no_v", ""));
        param.add(new BasicNameValuePair("search_vhcle_no_yn", "N"));
        param.add(new BasicNameValuePair("birthdoayMenuOpt", "Y"));
        break;
      default :
        break;
    }



    return param;
  }
}
