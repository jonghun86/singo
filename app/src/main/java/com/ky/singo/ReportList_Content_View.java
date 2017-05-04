package com.ky.singo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by mmyjh on 2017-05-04.
 */

public class ReportList_Content_View extends LinearLayout {

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
     /*
    bg = (LinearLayout) findViewById(R.id.bg);
    symbol = (ImageView) findViewById(R.id.symbol);

    text = (TextView) findViewById(R.id.text);
    */
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
    /*
    int bg_resID = typedArray.getResourceId(R.styleable.LoginButton_bg, R.drawable.login_naver_bg);
    bg.setBackgroundResource(bg_resID);

    int symbol_resID = typedArray.getResourceId(R.styleable.LoginButton_symbol, R.drawable.login_naver_symbol);
    symbol.setImageResource(symbol_resID);

    int textColor = typedArray.getColor(R.styleable.LoginButton_textColor, 0);
    text.setTextColor(textColor);

    String text_string = typedArray.getString(R.styleable.LoginButton_text);
    text.setText(text_string);


    typedArray.recycle();
     */
  }
}
