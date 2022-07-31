package com.example.smtpserver.pop3.command;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpserver.entity.EmailBox;
import com.example.smtpserver.entity.Log;
import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.exception.IndexExceedException;
import com.example.smtpserver.mapper.EmailBoxMapper;
import com.example.smtpserver.mapper.LogMapper;
import com.example.smtpserver.pop3.server.Pop3Data;
import com.example.smtpserver.utils.ReplyBuilder;
import com.example.smtpserver.utils.ApplicationContextHolder;

import java.util.Date;
import java.util.List;

public class RetrCommand extends BaseCommand {

  EmailBoxMapper emailBoxMapper;
  LogMapper logMapper;

  public RetrCommand() {
    super("retr");
    emailBoxMapper = ApplicationContextHolder.getBean("emailBoxMapper", EmailBoxMapper.class);
    logMapper = ApplicationContextHolder.getBean("logMapper", LogMapper.class);
  }

  @Override
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {
    if (pop3Data.getState() != 3) {
      throw new CommandOrderException();
    }
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 2) {
      pop3Data.reply(ReplyBuilder.buildRetrErrReply());
      throw new ArgsErrorException();
    }
    // 返回某邮件的所有内容
    List<EmailBox> emailBoxes =
        emailBoxMapper.selectList(
            new LambdaQueryWrapper<EmailBox>()
                .eq(EmailBox::getToEmail, pop3Data.getUser().getAccount()));
    if (emailBoxes == null) {
      throw new RuntimeException();
    }
    int index = Integer.parseInt(parsedArgs[1]);
    if (index <= emailBoxes.size()) {
      // 更新邮件状态，表示该邮件被用户拉取到了本地
      emailBoxes.get(index - 1).setState(1);
      emailBoxMapper.updateById(emailBoxes.get(index - 1));
      pop3Data.reply(ReplyBuilder.buildRetrOkReply(emailBoxes.get(index - 1)));
      // 添加日志
      logMapper.insert(
          Log.builder()
              .type("pop3")
              .logTime(new Date())
              .content(
                  pop3Data.getUser().getAccount()
                      + " get email["
                      + emailBoxes.get(index - 1).getId()
                      + "] from "
                      + emailBoxes.get(index - 1).getFromEmail())
              .build());
    } else {
      throw new IndexExceedException();
    }
  }
}
