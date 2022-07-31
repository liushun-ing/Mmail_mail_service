package com.example.smtpserver.smtp.command;

import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.smtp.server.SmtpData;
import com.example.smtpserver.utils.ReplyMessage;

import java.util.HashSet;

public class RsetCommand extends BaseCommand {

  public RsetCommand() {
    super("rset");
  }

  @Override
  public void handle(SmtpData smtpData, String commandString) throws Exception {
    // 还是需要判断指令对不对
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 1) {
      smtpData.reply(ReplyMessage.RSET_ARGS_ERROR.getMessage());
      throw new ArgsErrorException();
    }
    // rset指令需要重置所有状态
    smtpData.setLastCommandOrder(0);
    smtpData.setUser(null);
    smtpData.setEmailBox(null);
    smtpData.setIsWaitingData(0);
    smtpData.setState(0);
    smtpData.setRcptList(new HashSet<>());
    // 然后回复一个信息
    smtpData.reply(ReplyMessage.RSET_OK.getMessage());
  }
}
