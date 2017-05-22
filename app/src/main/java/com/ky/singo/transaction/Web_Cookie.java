package com.ky.singo.transaction;

/**
 * Created by mmyjh on 2017-04-25.
 */
public class Web_Cookie {
  private static Web_Cookie  cookieInstance = new Web_Cookie ();
  private String key;

  public static Web_Cookie  getInstance() {
    return cookieInstance;
  }

  // This singletone class ban the use of default constructor
  private Web_Cookie () { key = null; }

  public void setKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
