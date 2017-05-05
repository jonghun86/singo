package com.ky.singo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mmyjh on 2017-05-04.
 */

public class ReportList_Content_View extends LinearLayout {
  TextView complaintTitleView;
  TextView complaintSubInfoView;

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

    complaintTitleView = (TextView) findViewById(R.id.complaintTitle);
    complaintSubInfoView = (TextView) findViewById(R.id.complaintSubInfo);
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
    complaintTitleView.setText(string);
    string = typedArray.getString(R.styleable.ReportContent_complaintSubInfo);
    complaintSubInfoView.setText(string);
    typedArray.recycle();
  }

  public void setContents(String complaintTitle, String complaintSubInfo) {
    complaintTitleView.setText(complaintTitle);
    complaintSubInfoView.setText(complaintSubInfo);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return super.onInterceptTouchEvent(ev);
  }
}
