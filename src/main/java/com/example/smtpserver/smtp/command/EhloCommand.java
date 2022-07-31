package com.example.smtpserver.smtp.command;

import com.example.smtpserver.entity.EmailBox;
import com.example.smtpserver.entity.User;
import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.smtp.server.CommandEnum;
import com.example.smtpserver.smtp.server.SmtpData;
import com.example.smtpserver.utils.ReplyMessage;

public class EhloCommand extends BaseCommand {

  public EhloCommand() {
    super("ehlo");
  }

  @Override
  public void handle(SmtpData smtpData, String commandString) throws Exception {
    // 判断上一个处理的指令序号是不是当前指令的序号减 1
    if (smtpData.getLastCommandOrder() != CommandEnum.EHLO.getOrder() - 1) {
      throw new CommandOrderException();
    } else {
      String[] parsedArgs = getParsedArgs(commandString);
      if (parsedArgs.length != 2) {
        smtpData.reply(ReplyMessage.EHLO_ARGS_ERROR.getMessage());
        throw new ArgsErrorException();
      } else {
        if (!"localhost".equals(parsedArgs[1]) && !ipCheck(parsedArgs[1])) {
          smtpData.reply(ReplyMessage.EHLO_ARGS_ERROR.getMessage());
          throw new ArgsErrorException();
        }
        // 验证成功，初始化数据
        smtpData.setUser(new User()); // 创建一个user
        smtpData.setEmailBox(new EmailBox()); // 创建一个邮件
        smtpData.setState(0); // 设置登陆状态为未开始登录
        // 设置当前处理的指令序号
        smtpData.setLastCommandOrder(CommandEnum.EHLO.getOrder());
        smtpData.reply(ReplyMessage.EHLO_OK.getMessage());
      }
    }
  }

  /** 判断IP地址的合法性，这里采用了正则表达式的方法来判断 return true，合法 */
  public static boolean ipCheck(String ip) {
    if (ip != null && !ip.isEmpty()) {
      // 定义正则表达式
      String regex =
          "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
              + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
              + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
              + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
      // 判断ip地址是否与正则表达式匹配
      if (ip.matches(regex)) {
        return true;
      }
    }
    return false;
  }
}
