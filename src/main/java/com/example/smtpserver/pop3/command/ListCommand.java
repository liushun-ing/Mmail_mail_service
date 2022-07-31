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

public class ListCommand extends BaseCommand {

  EmailBoxMapper emailBoxMapper;

  public ListCommand() {
    super("list");
    emailBoxMapper = ApplicationContextHolder.getBean("emailBoxMapper", EmailBoxMapper.class);
  }

  @Override
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {
    if (pop3Data.getState() != 3) {
      throw new CommandOrderException();
    }
    String[] parsedArgs = getParsedArgs(commandString);
    // 第二个参数是可选的
    if (parsedArgs.length != 1 && parsedArgs.length != 2) {
      pop3Data.reply(ReplyBuilder.buildListErrReply());
      throw new ArgsErrorException();
    }
    // 查询列表
    List<EmailBox> emailBoxes =
        emailBoxMapper.selectList(
            new LambdaQueryWrapper<EmailBox>()
                .eq(EmailBox::getToEmail, pop3Data.getUser().getAccount()));
    if (emailBoxes == null) {
      throw new RuntimeException();
    }
    int index = 0;
    // 是否有第二个参数，解析不对会自动抛出异常
    if (parsedArgs.length == 2) {
      index = Integer.parseInt(parsedArgs[1]);
    }
    // index是否在范围内
    if (index <= emailBoxes.size()) {
      pop3Data.reply(ReplyBuilder.buildListOkReply(emailBoxes, index));
    } else {
      throw new IndexExceedException();
    }
  }
}
