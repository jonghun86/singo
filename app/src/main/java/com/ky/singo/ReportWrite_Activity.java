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
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.ky.singo.transaction.Web_GetTransaction;
import com.ky.singo.transaction.Web_Param;
import com.ky.singo.transaction.Web_PostTransaction;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
  final String fileUploadUrl = "https://www.epeople.go.kr/applex_wdigm/applet/jsp/fileup_cu_real.jsp";
  final String contentUploadUrl = "https://www.epeople.go.kr/jsp/user/pc/cvreq/UPcRecommendOrg.jsp";
  final String summaryUploadUrl = "https://www.epeople.go.kr/onto/ajax/ajax_onto_recommand_req.jsp";
  final String userInfoReqUrl = "http://www.epeople.go.kr/jsp/user/pc/cvreq/UPcCvreqForm.jsp";

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

  private final String ID_REPORT_WRITE_QUERY = "SINGO_REPORT_WRITE";
  private static final int SELECT_PICTURE = 1;
  private String selectedImagePath;
  byte[] bitMapData;
  private GoogleMap googleMap;

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
    Web_Param webParam;
    String responseBody;
    Document doc;
    Elements elements;

    // 동영상 or 사진 첨부를 누락한 경우
    if (bitMapData == null) {
      Toast.makeText(getApplicationContext(), "사진/동영상이 첨부되지 않았습니다.",
        Toast.LENGTH_SHORT).show();
      return false;
    }
    Log.d(ID_REPORT_WRITE_QUERY, "Upload media files");


    try {
      uploadGetTask = new ReportUploadGetTask(userInfoReqUrl);
      webParam      = getPredefinedParam(PostReqType.USERDATA_REQUEST);
      responseBody  = uploadGetTask.execute(webParam).get();

      doc = Jsoup.parse(responseBody);
      elements = doc.select("input"); //
      for (Element element : elements) {
        // peter_name_v
        // (userName)       : 민원인 이름
        // memId (mem_id_v) : 민원인 ID
        // juso2Anc_Sub     : 주소 코드1 (경기도)
        // juso2Anc_Basic   : 주소 코드2 (광명시)
        // adr1_v           : 주소 1 경기도 광명시 시청로 139,
        // adr2_v           : 주소 2 우성아파트 101-201
        // peter_cel_no_v2  : 핸드폰 가운데자리
        Log.d("####", element.attr("name") + " : " + element.attr("value"));
      }

      /////////////////////////////////////////////////////////////////////////////////////////////
      // Upload Video Files
      uploadTask    = new ReportUploadPostTask(fileUploadUrl);
      webParam      = getPredefinedParam(PostReqType.MULTIMEDIA_UPLOAD);
      responseBody  = uploadTask.execute(webParam).get();
      if (responseBody == null) {
        Toast.makeText(getApplicationContext(), "사진/동영상 업로드에 실패했습니다.",
          Toast.LENGTH_SHORT).show();
        return false;
      }

      /////////////////////////////////////////////////////////////////////////////////////////////
      //
      uploadTask    = new ReportUploadPostTask(contentUploadUrl);
      webParam      = getPredefinedParam(PostReqType.CONTENTS_REQUEST);
      responseBody  = uploadTask.execute(webParam).get();
      // [HACK] Response is arrived but server sent error because of wrong request. Server sent
      // message "정상적인 접근이 아닙니다" in response body.
      if (responseBody.length() < 500) {
        Log.d(ID_REPORT_WRITE_QUERY, "Abnormal access (CONTENTS_REQUEST)");
        Toast.makeText(getApplicationContext(), "정상적인 접근이 아닙니다.",
          Toast.LENGTH_SHORT).show();
        return false;
      }

      // FIXME
      doc = Jsoup.parse(responseBody);
      Element element = doc.select("input[name=file1_id]").first();
      fileId = element.attr("value");
      Log.d("##", element.attr("value"));

      /////////////////////////////////////////////////////////////////////////////////////////////
      //
      uploadTask    = new ReportUploadPostTask(summaryUploadUrl);
      webParam      = getPredefinedParam(PostReqType.CONTENTS_SUMMARY);
      responseBody  = uploadTask.execute(webParam).get();
      if (responseBody == null) {
        Toast.makeText(getApplicationContext(), "------------------.",
          Toast.LENGTH_SHORT).show();
        return false;
      }
      Log.d("----1", responseBody);

      /////////////////////////////////////////////////////////////////////////////////////////////
      //

      uploadTask    = new ReportUploadPostTask(contentUploadUrl);
      webParam      = getPredefinedParam(PostReqType.CONTENTS_WRITE);
      responseBody  = uploadTask.execute(webParam).get();
      if (responseBody == null) {
        Toast.makeText(getApplicationContext(), "########",
          Toast.LENGTH_SHORT).show();
        return false;
      }
      Log.d("----2", responseBody);


      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }


  public class ReportUploadPostTask extends AsyncTask<Web_Param, Void, String> {
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
          Log.d(ID_REPORT_WRITE_QUERY, e.toString());
          e.printStackTrace();
        }
      }
      return null;
    }
  }

  public class ReportUploadGetTask extends AsyncTask<Web_Param, Void, String> {
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
          Log.d(ID_REPORT_WRITE_QUERY, e.toString());
          e.printStackTrace();
        }
      }
      return null;
    }
  }


  public enum PostReqType {
    // FIXME : 이름 바꿀것
    USERDATA_REQUEST,
    MULTIMEDIA_UPLOAD,
    CONTENTS_REQUEST,
    CONTENTS_SUMMARY,
    CONTENTS_WRITE
  }

  Web_Param getPredefinedParam (PostReqType type) {
    Web_Param param = new Web_Param();

    switch (type) {
      case USERDATA_REQUEST:
        param.add("flag", "N");;
        break;
      case MULTIMEDIA_UPLOAD:
        param.add("item_file[]", bitMapData);
        break;
      case CONTENTS_REQUEST:
        // -------------------------------------------------------------------------------------------
        // STEP 1. 신청서 작성 및 유사사례 확인
        // 개인정보 수집 및 이용 안내
        param.add("third_person_sup_yn_c", "Y");
        // 신청인 본인인증
        //  - 개인 N, 단체 Y, 기업 C
        //  - 신청인 이름
        param.add("grp3_peti_yn_c",	"N");
        param.add("userName",	nameEditText.getText().toString());
        // 신청인 기본정보
        // - 휴대전화
        param.add("peter_cel_no_v1",	"010");
        param.add("peter_cel_no_v2",	"");
        param.add("peter_cel_no_v3",	"");
        // - email
        param.add("peter_email_v1",	"");
        param.add("peter_email_v2",	"gmail.com");
        param.add("domain",	"gmail.com");
        // - 주소
        //FIXME - zipcode
        param.add("zipcode_c",	"06762");
        param.add("adr1_v", addr1EditText.getText().toString());
        param.add("adr2_v", addr2EditText.getText().toString());
        // - 민원발생 지역
        //    + 주소와 동일한 지역인가?
        //    + 시도
        //    + 시군구
        //FIXME : 시도/시군구 코드는 받아오는 코드를 짜야 함
        param.add("occurrence_same_addr", "N");
        param.add("subOrg", "6110000");
        param.add("basicOrg",	"3210000");
        // 나의 민원 확인 방식
        //  - 1 : 간편형
        //  - 2 : 보안형
        param.add("mypeti_view_method_c", "1");
        // 민원 내용
        //  - 제목
        param.add("peti_title_v", singoSubjectEditText.getText().toString());
        param.add("getFocusPro1", "1");
        //  - 내용
        param.add("peti_reason_l", singoContentEditText.getText().toString());
        param.add("getFocusPro2", "1");
        // 귀하께서 위 민원과 동일 내용의 민원을 이미 행정기관 등에 제출하여 그 처리결과를 받은 적이 있습니까?
        param.add("proc_rcv_yn_c",	"N");
        // 귀하께서 제출하실 민원에 제보, 고발성 내용을 포함하고 있습니까?
        param.add("accuse_yn_c", "N");
        // 귀하의 민원신청 내용을 공유하는 것에 동의하십니까?
        param.add("open_yn_c",	"N");
        // We don't know usage of hidden values
        param.add("flag",	"N");
        param.add("menuGubun", "0");
        param.add("menu1","pc");
        param.add("peti_no_c","");
        param.add("mem_id_v",	GlobalVar.getId());
        param.add("peti_path_gubun_c",	"00020011");
        param.add("email_v", emailEditText.getText().toString());
        // Request code
        // - 00570008 : 전자우편,sms,서면
        // - 00570005 : 전자우편, sms
        // - 00570006 : 전자우편, 서면
        // - 00570007 : sms, 서면
        // - 00570002 : 전자우편
        // - 00570004 : sms
        // - 00570001 : 서면
        // - 00570003 : 나머지
        param.add("peti_nti_method_c",	"00570003");
        // ??
        param.add("ls",	"10");
        param.add("fOpenYn",	"N");
        // road addr code ??
        // 도로명, 지번주소관한 것들 다 쿼리로 받아와서 코드 넣어야 함
        param.add("scode",	"116504163196");
        // jibun addr or road addr
        param.add("jgubun", "1");
        // second setting?
        // what is difference between memId and mem_id_v
        param.add("memId", GlobalVar.getId());
        // WTF?
        param.add("dupInfo",	"MC0GCCqGSIb3DQIJAyEAqpfr2ecwUkZQ1pugdusBdjP8Tb46ylwJMcAY76vPh1Q=");
        param.add("peter_name_v", nameEditText.getText().toString());
        // WTF? - maybe 경기도 code
        //FIXME - 주소 코드 받아와야 함
        param.add("juso2Anc_Sub",	"6110000");
        //code":"3900000","name":"광명시"
        param.add("juso2Anc_Basic",	"3210000");
        param.add("ChgSubAnc",	"6110000");
        param.add("ChgBasicAnc",	"3210000");
        // I don't know what it is. But it seems to be set to same value in all query
        // encoded string for "[민원] 민원 신청" [https://meyerweb.com/eric/tools/decoder/]
        // maybe it is for sns sharing.
        param.add("snsTokenMessage",	"%5B%EB%AF%BC%EC%9B%90%5D+%EB%AF%BC%EC%9B%90+%EC%8B%A0%EC%B2%AD");
        // 고정 값 - 사용이유 모름
        param.add("indvdlinfo_view_agre_yn_c",	"Y");
        param.add("mail_attch_yn_c",	"N");
        param.add("cvpl_se_c",	"80030001");

        // FIXME
        // Multimedia data
        param.add("file1", bitMapData);

        // empty
        param.add("mode",	"");
        param.add("jumin_no_c",	"");
        param.add("mem_native_yn_c",	"");
        param.add("tel_no_v",	"");
        param.add("cel_no_v",	"");
        param.add("basic_chk",	"");
        param.add("evadeTargetFocusChk",	"");
        param.add("evadeReasonFocusChk",	"");
        param.add("corp_no_c",	"");
        param.add("juminNo",	"");
        param.add("residentNoCheckYn",	"");
        param.add("jumin1",	"");
        param.add("jumin2",	"");
        param.add("hPersonallykind",	"");
        param.add("prv_flag",	"");
        param.add("corp_name_v",	"");
        param.add("corp_no_c1",	"");
        param.add("corp_no_c2",	"");
        param.add("corp_no_c3",	"");
        param.add("grp_name",	"");
        param.add("peter_tel_no_v1",	"");
        param.add("peter_tel_no_v2",	"");
        param.add("peter_tel_no_v3",	"");
        param.add("primary_anc_code_v",	"");
        param.add("dmge_aplcnt_nm_v",	"");
        param.add("dmge_wrkplc_v",	"");
        param.add("dmge_adr1_v",	"");
        param.add("dmge_tel_no_v",	"");
        param.add("ancCheck",	"");
        break;
      case CONTENTS_SUMMARY :
        param.add("ADR1_V", addr1EditText.getText().toString());
        param.add("PETI_TITLE_V", singoSubjectEditText.getText().toString());
        param.add("PETI_REASON_L", singoContentEditText.getText().toString());
        //FIXME - 사진
        param.add("PETI_DOCUMENT", "13900000;sample_image.jpg;");
        param.add("PM_FLAG", "80030001");
        break;
      case CONTENTS_WRITE :
        param.add("mode", "createPcCvreq");
        param.add("flag", "null");
        param.add("detailView", "Y");
        param.add("menuGubun", "0");
        param.add("menu1"	, "pc");
        param.add("peti_no_c", "");
        param.add("mem_id_v", GlobalVar.getId());
        param.add("anc_code_v", "1320000");
        param.add("civil_again_c", "00600001");
        param.add("sect_error_yn_c", "Y");
        param.add("p_sect_error_yn_c", "Y");
        param.add("peti_path_gubun_c", "00020011");
        param.add("peter_name_v", nameEditText.getText().toString());
        param.add("mem_native_yn_c", "");
        param.add("zipcode_c", "06762");
        param.add("adr1_v", addr1EditText.getText().toString());
        param.add("adr2_v", addr2EditText.getText().toString());
        param.add("peti_job_c", "");
        param.add("tel_no_v", "");
        param.add("cel_no_v", "");
        param.add("email_v", emailEditText.getText().toString());
        param.add("open_yn_c", "N");
        param.add("peti_title_v", singoSubjectEditText.getText().toString());
        param.add("peti_reason_l", singoContentEditText.getText().toString());
        param.add("peti_nti_method_c", "00570003");
        param.add("passwd_v", "");
        param.add("civil_rel_name_v", "");
        param.add("civil_rel_tel_no_v", "");
        param.add("civil_rel_zipcode_c", "");
        param.add("civil_rel_addr_v", "");
        param.add("civil_rel_addr1_v", "");
        //FIXME - 사진 이름
        param.add("file1_name",	"sample_image.jpg");
        param.add("file2_name", "");
        param.add("file3_name", "");
        param.add("file4_name", "");
        param.add("file5_name", "");
        param.add("file1_size", "153240");
        param.add("file2_size", "");
        param.add("file3_size", "");
        param.add("file4_size", "");
        param.add("file5_size", "");
        //FIXME - 날짜별로 업로드 경로가 다름
        param.add("file1", "/attach04/2017/5/23/jsp/user/pc/cvreq/UPcRecommendOrg.jsp/sample_image.jpg");
        param.add("file2"	, "");
        param.add("file3", "");
        param.add("file4", "");
        param.add("file5", "");
        //FIXME - 날짜별로 업로드 경로가 다름
        param.add("file1_dir", "/attach04/2017/5/23/jsp/user/pc/cvreq/UPcRecommendOrg.jsp/");
        param.add("file2_dir", "");
        param.add("file3_dir", "");
        param.add("file4_dir", "");
        param.add("file5_dir", "");
        //FIXME - 날짜별로 업로드 경로가 다름
        param.add("file1_temp_dir", "/attach04/temp/2017/5/23/jsp/user/pc/cvreq/UPcRecommendOrg.jsp/sample_image.jpg");
        param.add("file2_temp_dir", "");
        param.add("file3_temp_dir", "");
        param.add("file4_temp_dir", "");
        param.add("file5_temp_dir", "");
        param.add("open_chk_c", "");
        param.add("grp3_peti_yn_c", "N");
        param.add("grp_name", "");
        param.add("mail_attch_yn_c", "Y");
        param.add("anc_c2", "1140100");
        param.add("auto_anc_v", "1140100");
        param.add("peti_anc_v", "1320000");
        param.add("pre_peti_no_c", "");
        param.add("miss_yn_c", "N");
        param.add("peti_ip_v", "124.53.50.34");
        param.add("anc_name_v", "경찰청");
        param.add("multi_anc_yn_c", "N");
        param.add("org_auto_anc_c", "1140100");
        param.add("multi_status_c", "");
        param.add("ombuds", "");
        param.add("org_civil_again_c", "");
        param.add("manual_acrc_yn_c", "N");
        param.add("manual_yn_c", "Y");
        param.add("auto_yn_c", "N");
        param.add("upAncCode", "1140100");
        param.add("seq_n", "");
        param.add("retFAQ", "0");
        param.add("occurrence_area", "");
        param.add("subOrg", "6110000");
        param.add("basicOrg", "3210000");
        param.add("occurrence_same_addr", "Y");
        param.add("corp_no_c", "");
        param.add("corp_name_v", "");
        param.add("fOpenYn", "N");
        //바로 전 리퀘스트에 쓰이는 부분임
        param.add("file1_id", fileId);
        //
        param.add("file2_id"	, "");
        param.add("file3_id", "");
        param.add("file4_id", "");
        param.add("file5_id", "");
        param.add("mypeti_view_method_c", "1");
        param.add("scode", "116504163196");
        param.add("jgubun", "1");
        param.add("cvpl_se_c", "80030001");
        param.add("dmge_aplcnt_nm_v", "");
        param.add("dmge_wrkplc_v", "");
        param.add("dmge_adr1_v", "");
        param.add("dmge_tel_no_v", "");
        param.add("jumin_no_c", "");
        param.add("dupInfo", "");
        param.add("third_person_sup_yn_c", "Y");
        param.add("hPersonallykind", "");
        param.add("evadeTargerCode", "");
        param.add("evadeTargerCode1", "");
        param.add("evadeReasonFocusChk", "");
        param.add("primary_anc_code_v", "");
        param.add("proc_rcv_yn_c", "N");
        param.add("prv_flag", "");
        param.add("ancCheck", "");
        param.add("corp_no_c1", "");
        param.add("corp_no_c2", "");
        param.add("corp_no_c3", "");
        param.add("consent_yn", "");
        param.add("onto_use_yn", "0");
        param.add("safe_civil_rate", "");
        param.add("better_civil_rate", "");
        param.add("accuse_yn_c", "N");
        param.add("indvdlinfo_view_agre_yn_c", "Y");
        param.add("snsTokenMessage", "%EC%A0%95%EC%9D%98%ED%95%84%EC%9A%94");
        param.add("chkNotSelectedYn", "N");
        param.add("evade_yn_c", "N");
        param.add("evade_target_v", "");
        param.add("evade_reason_v", "");
        param.add("trafic_violtn_yn_c", "");
        param.add("vhcle_no_v", vehicleEditText.getText().toString());
        param.add("police_peti_gubun_c", "100");
        //?
        param.add("add_vhcle_no_v", "");
        param.add("search_vhcle_no_yn", "N");
        param.add("birthdoayMenuOpt", "Y");
        break;
      default :
        break;
    }



    return param;
  }
}
