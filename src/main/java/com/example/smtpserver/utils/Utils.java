package com.example.smtpserver.utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
  /** 获取当前时间 */
  public static String getCurrentTime() {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(date);
  }

  public static String getBase64FromUtf8(String utf8String) {
    return Base64.encodeBase64String(utf8String.getBytes(StandardCharsets.UTF_8));
  }

  /** 将base64编码转换为utf8编码的字符串 */
  public static String getUtf8FromBase64(String base64String) throws Exception {
    return new String(
        Base64.decodeBase64(base64String.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
  }
}
