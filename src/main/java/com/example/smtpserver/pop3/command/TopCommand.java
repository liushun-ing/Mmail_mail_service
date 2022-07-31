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

import java.util.List;

public class TopCommand extends BaseCommand {

  EmailBoxMapper emailBoxMapper;

  public TopCommand() {
    super("top");
    emailBoxMapper = ApplicationContextHolder.getBean("emailBoxMapper", EmailBoxMapper.class);
  }

  @Override
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {
    if (pop3Data.getState() != 3) {
      throw new CommandOrderException();
    }
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 3) {
      pop3Data.reply(ReplyBuilder.buildTopErrReply());
      throw new ArgsErrorException();
    }
    // 返回某邮件的前几行内容
    List<EmailBox> emailBoxes =
        emailBoxMapper.selectList(
            new LambdaQueryWrapper<EmailBox>()
                .eq(EmailBox::getToEmail, pop3Data.getUser().getAccount()));
    if (emailBoxes == null) {
      throw new RuntimeException();
    }
    int index = Integer.parseInt(parsedArgs[1]);
    int n = Integer.parseInt(parsedArgs[2]);
    if (index <= emailBoxes.size()) {
      pop3Data.reply(ReplyBuilder.buildTopOkReply(emailBoxes.get(index - 1).getData(), n));
    } else {
      throw new IndexExceedException();
    }
  }
}
