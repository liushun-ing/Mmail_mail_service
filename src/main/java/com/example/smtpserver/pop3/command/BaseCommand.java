package com.example.smtpserver.pop3.command;

import com.example.smtpserver.pop3.server.Pop3Data;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 命令的基类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
abstract public class BaseCommand {

  // 命令的主体
  private String commandName;

  // 处理函数，需要基类自定义实现
  abstract public void handle(Pop3Data pop3Data, String commandString) throws Exception;

  // 下面可以定义一些公共方法

  /**
   * 解析参数
   */
  public String[] getParsedArgs(String commandString) throws Exception {
    return commandString.split(" ");
  }
}
