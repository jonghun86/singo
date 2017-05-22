package com.ky.singo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ky.singo.transaction.Web_PostTransaction;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;


/**
 * A login screen that offers login via email/password.
 */
public class Login_Activity extends AppCompatActivity {
  /**
   * String to identity log message
   */
  private final String ID_USER_LOGIN = "LOGIN";

  /**
   * Keep track of the login task to ensure we can cancel it if requested.
   */
  private SinMunGoLoginTask smgThread = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_activity);

    //
    //TextView IdEditText = (EditText)findViewById(R.id.editText);
    //EditText pwEditText = (EditText)findViewById(R.id.editText);

    // Button click events
    Button signInBtn = (Button) findViewById(R.id.sign_in_button);
    signInBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        attemptLogin();
      }
    });

    //////////////////////////////////////////////////////////////////////////////////
    // To be removed...
    // For test
    smgThread = new SinMunGoLoginTask("mmyjh86", "2qnsrl.dkagh");
    smgThread.execute((Void) null);
    //////////////////////////////////////////////////////////////////////////////////
  }

  /**
   * Attempts to sign in or register the account specified by the login form.
   * If there are form errors (invalid email, missing fields, etc.), the
   * errors are presented and no actual login attempt is made.
   */
  private void attemptLogin() {
    if (smgThread != null) {
      return;
    }
   /*
    // Store values at the time of the login attempt.
    String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();
     */


    boolean cancel = false;

    // it is not empty
    if (!cancel) {

    } else {
      //smgThread = new SinMunGoLoginTask(email, password);
      smgThread.execute((Void) null);
    }
  }




  /**
   * Represents an asynchronous login/registration task used to authenticate
   * the user.
   */
  public class SinMunGoLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String smgId;
    private final String smgPw;
    private final String loginUrl = "https://www.epeople.go.kr/ULogin.do";

    SinMunGoLoginTask(String id, String pw) {
      smgId = id;
      smgPw = pw;
    }

    @Override
    protected Boolean doInBackground(Void... not_used) {
      Boolean isSuccess = false;
      ArrayList<NameValuePair> postParam;
      Web_PostTransaction postTransaction;

      // parameter
      postParam = new ArrayList<NameValuePair>();
      postParam.add(new BasicNameValuePair("memId", smgId));
      postParam.add(new BasicNameValuePair("memPw", smgPw));
      postParam.add(new BasicNameValuePair("execmode", "Y"));
      postParam.add(new BasicNameValuePair("isPeti", "N"));

      // send a packet
      postTransaction = new Web_PostTransaction(loginUrl);
      return postTransaction.send(postParam);
    }

    @Override
    protected void onPostExecute(final Boolean isSuccess) {
      smgThread = null;

      // Login process is successfully done
      if (isSuccess == true) {
        Intent intent = new Intent(Login_Activity.this, ReportList_Activity.class);
        //intent.putExtra(getResource().﻿﻿getString(intent_query_mem_id), userLoginId);
        startActivity(intent);
      }
      else {
        Log.d(ID_USER_LOGIN, "Failed to send transaction");
        Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_SHORT).show();
      }
    }
  }
}

