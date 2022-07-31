package com.example.smtpserver.pop3.command;

import com.example.smtpserver.pojo.StatEmailBox;
import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.mapper.EmailBoxMapper;
import com.example.smtpserver.pop3.server.Pop3Data;
import com.example.smtpserver.utils.ReplyBuilder;
import com.example.smtpserver.utils.ApplicationContextHolder;

public class StatCommand extends BaseCommand {

  EmailBoxMapper emailBoxMapper;

  public StatCommand() {
    super("stat");
    emailBoxMapper = ApplicationContextHolder.getBean("emailBoxMapper", EmailBoxMapper.class);
  }

  @Override
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {
    if (pop3Data.getState() != 3) {
      throw new CommandOrderException();
    }
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 1) {
      pop3Data.reply(ReplyBuilder.buildStatErrReply());
      throw new ArgsErrorException();
    }
    // 获取邮件数目和总大小
    StatEmailBox statEmailBox = emailBoxMapper.getStatEmailBox(pop3Data.getUser().getAccount());
    System.out.println(statEmailBox);
    if (statEmailBox != null) {
      pop3Data.reply(ReplyBuilder.buildStatOkReply(statEmailBox));
    } else {
      throw new RuntimeException();
    }
  }
}
