package com.example.smtpserver.smtp.command;

import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.smtp.server.CommandEnum;
import com.example.smtpserver.smtp.server.SmtpData;
import com.example.smtpserver.utils.ReplyMessage;

public class DataCommand extends BaseCommand {

  public DataCommand() {
    super("data");
  }

  @Override
  public void handle(SmtpData smtpData, String commandString) throws Exception {
    // 判断上一个处理的指令序号是不是当前指令的序号减 1
    if (smtpData.getLastCommandOrder() != CommandEnum.DATA.getOrder() - 1) {
      throw new CommandOrderException();
    } else {
      String[] parsedArgs = getParsedArgs(commandString);
      if (parsedArgs.length != 1) {
        smtpData.reply(ReplyMessage.DATA_ARGS_ERROR.getMessage());
        throw new ArgsErrorException();
      }
      // 验证通过
      smtpData.setIsWaitingData(1); // 接下来将进入数据输入阶段，遇到单行的一个[.]则结束
      smtpData.setLastCommandOrder(CommandEnum.DATA.getOrder());
      smtpData.reply(ReplyMessage.DATA_OK.getMessage());
    }
  }
}
