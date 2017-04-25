package com.ky.singo;

/**
 * Created by mmyjh on 2017-04-25.
 */
public class Cookie {
  private static Cookie cookieInstance = new Cookie();
  private String key;

  public static Cookie getInstance() {
    return cookieInstance;
  }

  // This singletone class ban the use of default constructor
  private Cookie() { key = null; }

  public void setKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
