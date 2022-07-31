package com.example.smtpserver.smtp.command;

import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.smtp.server.CommandEnum;
import com.example.smtpserver.smtp.server.SmtpData;
import com.example.smtpserver.utils.ReplyMessage;

import java.util.Locale;

public class AuthCommand extends BaseCommand {

  public AuthCommand() {
    super("auth");
  }

  @Override
  public void handle(SmtpData smtpData, String commandString) throws Exception {
    // 判断上一个处理的指令序号是不是当前指令的序号减 1
    if (smtpData.getLastCommandOrder() != CommandEnum.AUTH.getOrder() - 1) {
      throw new CommandOrderException();
    } else {
      String[] parsedArgs = getParsedArgs(commandString);
      if (parsedArgs.length != 2) {
        smtpData.reply(ReplyMessage.AUTH_ARGS_ERROR.getMessage());
        throw new ArgsErrorException();
      } else {
        if (!"login".equals(parsedArgs[1].toLowerCase(Locale.ROOT))) {
          smtpData.reply(ReplyMessage.AUTH_ARGS_ERROR.getMessage());
          throw new ArgsErrorException();
        }
        // 验证成功
        smtpData.setState(1); // 设置此时的状态为初始化登陆状态
        smtpData.setLastCommandOrder(CommandEnum.AUTH.getOrder()); // 设置当前指令序号
        smtpData.reply(ReplyMessage.AUTH_OK.getMessage());
      }
    }
  }
}
