package com.example.smtpserver.smtp.command;

import com.example.smtpserver.smtp.server.SmtpData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 命令的基类 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseCommand {

  // 命令的前四位
  private String commandName;

  // 处理函数，需要基类自定义实现
  public abstract void handle(SmtpData smtpData, String commandString) throws Exception;

  // 下面可以定义一些公共方法

  /** 解析参数 */
  public String[] getParsedArgs(String commandString) throws Exception {
    return commandString.split(" ");
  }
}
