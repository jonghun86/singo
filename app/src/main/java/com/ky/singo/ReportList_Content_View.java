package com.ky.singo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mmyjh on 2017-05-04.
 */

public class ReportList_Content_View extends LinearLayout {
  TextView complaintTitle;
  TextView requestDate;
  TextView processState;
  ImageView processIcon;

  public ReportList_Content_View(Context context) {
    super(context);
    initView();
  }

  public ReportList_Content_View(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
    getAttrs(attrs);
  }

  public ReportList_Content_View(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs);
    initView();
    getAttrs(attrs, defStyle);
  }

  private void initView() {
    String infService = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
    View v = li.inflate(R.layout.reportlist_content_view, this, false);
    addView(v);

    complaintTitle = (TextView) findViewById(R.id.complaintTitle);
    requestDate = (TextView) findViewById(R.id.requestDate);
    processState = (TextView) findViewById(R.id.processState);
    processIcon = (ImageView) findViewById(R.id.processIcon);
  }

  private void getAttrs(AttributeSet attrs) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ReportContent);
    setTypeArray(typedArray);
  }


  private void getAttrs(AttributeSet attrs, int defStyle) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ReportContent,
      defStyle, 0);
    setTypeArray(typedArray);
  }

  private void setTypeArray(TypedArray typedArray) {
    String string;

    string = typedArray.getString(R.styleable.ReportContent_complaintTitle);
    complaintTitle.setText(string);

    string = typedArray.getString(R.styleable.ReportContent_requestDate);
    requestDate.setText(string);

    string = typedArray.getString(R.styleable.ReportContent_processState);
    processState.setText(string);
    typedArray.recycle();
    /*
    int bg_resID = typedArray.getResourceId(R.styleable.ReportContent_processIcon,
      R.drawable.login_naver_bg);
    bg.setBackgroundResource(bg_resID);
     */
  }
}
