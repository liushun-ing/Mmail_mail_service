package com.example.smtpserver.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDateUtil {
  public static String getFormatDate(Date date) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return simpleDateFormat.format(date);
  }

  public static String getFormatDateTime(Date date) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return simpleDateFormat.format(date);
  }
}
