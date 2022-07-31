package com.example.smtpserver.smtp.command;

import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.smtp.server.SmtpData;
import com.example.smtpserver.utils.ReplyMessage;

public class NoopCommand extends BaseCommand {

  public NoopCommand() {
    super("noop");
  }

  @Override
  public void handle(SmtpData smtpData, String commandString) throws Exception {
    // 还是需要判断指令对不对
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 1) {
      smtpData.reply(ReplyMessage.NOOP_ARGS_ERROR.getMessage());
      throw new ArgsErrorException();
    }
    // 空操作只需要简单回复一句即可
    smtpData.reply(ReplyMessage.NOOP_OK.getMessage());
  }
}
