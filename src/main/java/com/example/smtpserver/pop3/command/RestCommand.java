package com.example.smtpserver.pop3.command;

import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.pop3.server.Pop3Data;
import com.example.smtpserver.utils.ReplyBuilder;

public class RestCommand extends BaseCommand {

  public RestCommand() {
    super("rest");
  }

  @Override
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {
    if (pop3Data.getState() != 3) {
      throw new CommandOrderException();
    }
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 1) {
      pop3Data.reply(ReplyBuilder.buildRestErrReply());
      throw new ArgsErrorException();
    }
    // 清除即可，或者直接赋值一个新set也行
    pop3Data.getDeleteList().clear();
    pop3Data.reply(ReplyBuilder.buildRestOkReply());
  }
}
