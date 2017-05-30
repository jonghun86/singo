package com.ky.singo.transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mmyjh on 2017-05-23.
 */

public class Web_Param {
  Map<String, String> textData;
  Map<String, byte []> videoData;
  public Web_Param() {
    textData = new HashMap<String, String>();
    videoData = new HashMap<String, byte []>();
  }

  public void add(String key, String value) {
    textData.put(key,	value);
  };

  public void add(String mediaTag, byte [] mediaSource) {
    videoData.put(mediaTag, mediaSource);
  }

  public Set<Map.Entry<String, String>> getTextEntry() { return textData.entrySet(); }

  public Set<Map.Entry<String, byte[]>> getVideoEntry() {
    return videoData.entrySet();
  }
}


