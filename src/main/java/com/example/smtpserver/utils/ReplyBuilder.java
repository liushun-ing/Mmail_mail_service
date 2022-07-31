package com.example.smtpserver.utils;

import com.example.smtpserver.entity.EmailBox;
import com.example.smtpserver.pojo.StatEmailBox;

import java.util.List;

/** 构建pop3的返回数据 */
public class ReplyBuilder {

  private static final String OkPrefix = "+OK";
  private static final String ErrPrefix = "-ERR";

  // 建立连接
  public static String buildReadyOkReply() {
    return OkPrefix + " pop3 server ready\r\n";
  }

  public static String buildCommandInvalidReply() {
    return ErrPrefix + " invalid command\r\n";
  }

  public static String buildCommandOrderErrReply() {
    return ErrPrefix + " command order error\r\n";
  }

  public static String buildUnknownErrReply() {
    return ErrPrefix + " unknown error\r\n";
  }

  // 下标超出了
  public static String buildIndexExceedErrReply() {
    return ErrPrefix + " index exceed\r\n";
  }

  // noop指令
  public static String buildNoopOkReply() {
    return OkPrefix + " noop success\r\n";
  }

  public static String buildNoopErrReply() {
    return ErrPrefix + " syntax: noop\r\n";
  }

  // user指令
  public static String buildUserOkReply() {
    return OkPrefix + " user success\r\n";
  }

  public static String buildUserErrReply() {
    return ErrPrefix + " syntax: user <account>\r\n";
  }

  // pass指令
  public static String buildPassOkReply() {
    return OkPrefix + " pass success, authorized\r\n";
  }

  public static String buildPassErrReply() {
    return ErrPrefix + " syntax: pass <password>\r\n";
  }

  // 账号密码认证通过
  public static String buildAuthErrReply() {
    return ErrPrefix + " authentication error, user <account> expected\r\n";
  }

  // quit指令
  public static String buildQuitOkReply() {
    return OkPrefix + " quit success, bye\r\n";
  }

  public static String buildQuitErrReply() {
    return ErrPrefix + " syntax: quit\r\n";
  }

  // stat指令
  public static String buildStatOkReply(StatEmailBox statEmailBox) {
    return OkPrefix
        + " "
        + statEmailBox.getCount()
        + " "
        + statEmailBox.getTotalSize()
        + "\r\n";
  }

  public static String buildStatErrReply() {
    return ErrPrefix + " syntax: stat\r\n";
  }

  // uidl指令
  public static String buildUidlOkReply(String emailId) {
    return OkPrefix + "\r\n" + emailId + "\r\n";
  }

  public static String buildUidlErrReply() {
    return ErrPrefix + " syntax: uidl msg#\r\n";
  }

  // list指令 index肯定小于emailBoxList的长度
  public static String buildListOkReply(List<EmailBox> emailBoxList, int index) {
    if (index != 0) {
      // 第二个参数有,只返回这一个
      return OkPrefix + "\r\n" + emailBoxList.get(index - 1).getSize() + "\r\n";
    }
    String returnString = "";
    // 展示全部
    for (int i = 0; i < emailBoxList.size(); i++) {
      returnString += (i + 1) + " " + emailBoxList.get(i).getSize() + "\r\n";
    }
    return OkPrefix + "\r\n" + returnString;
  }

  public static String buildListErrReply() {
    return ErrPrefix + " syntax: list [msg#]\r\n";
  }

  // retr指令
  public static String buildRetrOkReply(EmailBox emailBox) {
    return OkPrefix
        + " fromEmail:"
        + emailBox.getFromEmail()
        + " sendTime:"
        + emailBox.getSendTime().getTime()
        + " size:"
        + emailBox.getSize()
        + " data:"
        + Utils.getBase64FromUtf8(emailBox.getData())
        + "\r\n";
  }

  public static String buildRetrErrReply() {
    return ErrPrefix + " syntax: retr msg#\r\n";
  }

  // dele指令
  public static String buildDeleOkReply() {
    return OkPrefix + " tag success\r\n";
  }

  public static String buildDeleErrReply() {
    return ErrPrefix + " syntax: dele msg#\r\n";
  }

  // rest指令
  public static String buildRestOkReply() {
    return OkPrefix + " rest success, all tag cleared\r\n";
  }

  public static String buildRestErrReply() {
    return ErrPrefix + " syntax: rest\r\n";
  }

  // top指令
  public static String buildTopOkReply(String data, int n) {
    String[] lines = data.split("\r\n");
    if (n > lines.length) {
      n = lines.length;
    }
    String returnString = "";
    for (int i = 0; i < n; i++) {
      returnString += (lines[i] + "\r\n");
    }
    return OkPrefix + "\r\n" + returnString;
  }

  public static String buildTopErrReply() {
    return ErrPrefix + " syntax: top msg# n\r\n";
  }
}
