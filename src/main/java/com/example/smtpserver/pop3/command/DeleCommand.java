package com.example.smtpserver.pop3.command;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpserver.entity.EmailBox;
import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.exception.IndexExceedException;
import com.example.smtpserver.mapper.EmailBoxMapper;
import com.example.smtpserver.pop3.server.Pop3Data;
import com.example.smtpserver.utils.ReplyBuilder;
import com.example.smtpserver.utils.ApplicationContextHolder;

public class DeleCommand extends BaseCommand {

  EmailBoxMapper emailBoxMapper;

  public DeleCommand() {
    super("dele");
    emailBoxMapper = ApplicationContextHolder.getBean("emailBoxMapper", EmailBoxMapper.class);
  }

  @Override
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {
    if (pop3Data.getState() != 3) {
      throw new CommandOrderException();
    }
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 2) {
      pop3Data.reply(ReplyBuilder.buildDeleErrReply());
      throw new ArgsErrorException();
    }
    int index = Integer.parseInt(parsedArgs[1]);
    Long sum =
        emailBoxMapper.selectCount(
            new LambdaQueryWrapper<EmailBox>()
                .eq(EmailBox::getToEmail, pop3Data.getUser().getAccount()));
    if (index <= sum.intValue()) {
      pop3Data.getDeleteList().add(index);
      pop3Data.reply(ReplyBuilder.buildDeleOkReply());
    } else {
      throw new IndexExceedException();
    }
  }
}
