package com.example.smtpserver.pop3.command;

import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.pop3.server.Pop3Data;
import com.example.smtpserver.utils.ReplyBuilder;

public class NoopCommand extends BaseCommand {

  public NoopCommand() {
    super("noop");
  }

  @Override
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {
    // 还是需要判断指令对不对
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 1) {
      pop3Data.reply(ReplyBuilder.buildNoopErrReply());
      throw new ArgsErrorException();
    }
    // 空操作只需要简单回复一句即可
    pop3Data.reply(ReplyBuilder.buildNoopOkReply());
  }
}
