package com.example.smtpserver.smtp.server;

import com.example.smtpserver.smtp.command.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
/** 指令注册，方便管理，之后使用的时候就不需要新建了，直接从这里拿即可 */
public enum CommandEnum {
  EHLO(new EhloCommand(), "ehlo 标识用户身份", 1),
  AUTH(new AuthCommand(), "auth 认证连接", 2),
  MAIL(new MailCommand(), "mail from 发件人邮箱", 3),
  RCPT(new RcptCommand(), "rcpt to 收件人邮箱", 4),
  DATA(new DataCommand(), "data 发送邮件内容", 5),
  QUIT(new QuitCommand(), "quit 关闭连接", 6),
  NOOP(new NoopCommand(), "noop 空操作", 7),
  RSET(new RsetCommand(), "rset 传输终止，会话重置", 8);

  private BaseCommand baseCommand;
  private String description;
  private int order;

  public BaseCommand getBaseCommand() {
    return this.baseCommand;
  }

  public String getDescription() {
    return this.description;
  }

  public int getOrder() {
    return this.order;
  }
}
