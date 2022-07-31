package com.example.smtpserver.utils;

import lombok.AllArgsConstructor;

/** smtp的返回消息体 */
@AllArgsConstructor
public enum ReplyMessage {
  /** 第一位数字为2时表示命令成功，5失败，3没有完成 */
  TELNET("220 " + ServerConfig.serverParams.getDomainName() + " SMTP\r\n"),
  EHLO_OK("250 ehlo OK\r\n"),
  AUTH_OK("334 VXNlcm5hbWU6\r\n"),
  AUTH_USERNAME_OK("334 UGFzc3dvcmQ6\r\n"),
  AUTH_PASSWORD_OK("235 auth successfully\r\n"),
  MAIL_OK("250 mail ok\r\n"),
  RCPT_OK("250 rcpt ok\r\n"),
  NOOP_OK("250 noop ok\r\n"),
  RSET_OK("250 rset ok, please ehlo next\r\n"),
  DATA_OK("354 end data with . \r\n"),
  QUIT_OK("221 Bye\r\n"),
  EMAIL_SEND_OK("250 mail ok queued as.\r\n"),
  EMAIL_SEND_ERROR("554 Error: email send error"),
  EHLO_ARGS_ERROR("501 Syntax: ehlo <hostAddress>\r\n"),
  AUTH_ARGS_ERROR("501 Syntax: auth login\r\n"),
  MAIL_ARGS_ERROR("501 Syntax: mail from:<reverse-path>\r\n"),
  RCPT_ARGS_ERROR("501 Syntax: rcpt to:<forward-path>\r\n"),
  DATA_ARGS_ERROR("501 Syntax: data\r\n"),
  NOOP_ARGS_ERROR("501 Syntax: noop\r\n"),
  RSET_ARGS_ERROR("501 Syntax: rset\r\n"),
  QUIT_ARGS_ERROR("501 Syntax: quit\r\n"),
  RCPT_EXCEED_ERROR("502 Error: rcpt number exceed\r\n"),
  COMMAND_INVALID("502 Error: command invalid\r\n"),
  EMAIL_INVALID("550 Error: email address invalid\r\n"),
  AUTH_USERNAME_FAILURE("501 argument invalid, use Base64 string\r\n"),
  AUTH_PASSWORD_FAILURE("501 argument invalid, use Base64 string\r\n"),
  AUTH_FAILURE("535 Error: authentication failed\r\n"),
  UNKNOWN_ERROR("554 Error: operation failure\r\n"),
  ERROR_ORDER("503 Error: error command order\r\n");

  private String message;

  public String getMessage() {
    return message;
  }
}
