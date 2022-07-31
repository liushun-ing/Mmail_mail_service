package com.example.smtpserver.pop3.command;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpserver.entity.User;
import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.mapper.UserMapper;
import com.example.smtpserver.pop3.server.Pop3Data;
import com.example.smtpserver.utils.ReplyBuilder;
import com.example.smtpserver.utils.ApplicationContextHolder;

public class PassCommand extends BaseCommand {

  UserMapper userMapper;

  public PassCommand() {
    super("pass");
    userMapper = ApplicationContextHolder.getBean("userMapper", UserMapper.class);
  }

  @Override
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {
    // 只有在 2 状态才能执行pass指令
    if (pop3Data.getState() != 2) {
      throw new CommandOrderException();
    }
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 2) {
      pop3Data.reply(ReplyBuilder.buildPassErrReply());
      throw new ArgsErrorException();
    }
    // 设置一下就好，具体判断等接收到密码之后在判断
    pop3Data.getUser().setPassword(parsedArgs[1]);
    // 查询数据库是否有该用户
    User user =
        userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getAccount, pop3Data.getUser().getAccount())
                .eq(User::getPassword, pop3Data.getUser().getPassword()));
    if (user == null) {
      pop3Data.setState(1); // 重置为等待账号状态
      pop3Data.reply(ReplyBuilder.buildAuthErrReply());
    } else {
      pop3Data.setState(3); // 设置认证成功状态,可以接收其他指令了
      pop3Data.reply(ReplyBuilder.buildPassOkReply());
    }
  }
}
