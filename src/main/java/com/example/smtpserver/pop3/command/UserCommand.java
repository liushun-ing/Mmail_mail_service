package com.example.smtpserver.pop3.command;

import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.pop3.server.Pop3Data;
import com.example.smtpserver.utils.ReplyBuilder;

public class UserCommand extends BaseCommand {

  public UserCommand() {
    super("user");
  }

  @Override
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {
    // 只有在 1 状态才能执行user指令
    if (pop3Data.getState() != 1) {
      throw new CommandOrderException();
    }
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 2) {
      pop3Data.reply(ReplyBuilder.buildUserErrReply());
      throw new ArgsErrorException();
    }
    // 设置一下就好，具体判断等接收到密码之后在判断
    pop3Data.getUser().setAccount(parsedArgs[1]);
    pop3Data.setState(2); // 设置等待密码状态
    pop3Data.reply(ReplyBuilder.buildUserOkReply());
  }
}
