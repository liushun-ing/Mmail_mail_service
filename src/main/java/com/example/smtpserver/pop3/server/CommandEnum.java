package com.example.smtpserver.pop3.server;

import com.example.smtpserver.pop3.command.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
/** 指令注册，方便管理，之后使用的时候就不需要新建了，直接从这里拿即可 */
public enum CommandEnum {
  USER(new UserCommand(), "user 账户名称，第一条指令"),
  PASS(new PassCommand(), "pass 账户密码，第二条指令"),
  APOP(new ApopCommand(), "apop 代替user和pass指令，以MD5数字摘要的形式提交账户密码"),
  STAT(new StatCommand(), "stat 查询邮箱中的统计信息，如邮件数量和总邮件占用字节大小"),
  UIDL(new UidlCommand(), "uidl 查询某封邮件的唯一标识符，后面是邮件序号，从1开始编号"),
  LIST(new ListCommand(), "list 列出邮箱中的邮件信息，序号和大小"),
  RETR(new RetrCommand(), "retr 获取某封邮件内容"),
  DELE(new DeleCommand(), "dele 设置删除标志"),
  REST(new RestCommand(), "rest 清除所有邮件的删除标记"),
  TOP(new TopCommand(), "top 获取某封邮件的邮件头和前n行内容"),
  NOOP(new NoopCommand(), "noop 空操作"),
  QUIT(new QuitCommand(), "quit 结束操作，需要删除所有设置了删除标记的邮件");

  private BaseCommand baseCommand;
  private String description;

  public BaseCommand getBaseCommand() {
    return this.baseCommand;
  }

  public String getDescription() {
    return this.description;
  }
}
