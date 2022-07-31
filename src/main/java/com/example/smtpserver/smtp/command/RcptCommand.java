package com.example.smtpserver.smtp.command;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpserver.entity.User;
import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.exception.EmailInvalidException;
import com.example.smtpserver.exception.RcptNumberExceedException;
import com.example.smtpserver.mapper.UserMapper;
import com.example.smtpserver.smtp.server.CommandEnum;
import com.example.smtpserver.smtp.server.SmtpData;
import com.example.smtpserver.utils.ReplyMessage;
import com.example.smtpserver.utils.ApplicationContextHolder;
import com.example.smtpserver.utils.ServerConfig;

import java.util.Locale;

public class RcptCommand extends BaseCommand {

  UserMapper userMapper;

  public RcptCommand() {
    super("rcpt");
    userMapper = ApplicationContextHolder.getBean("userMapper", UserMapper.class);
  }

  @Override
  public void handle(SmtpData smtpData, String commandString) throws Exception {
    // 判断上一个处理的指令序号是不是当前指令的序号减 1
    // 注意收件人可以有很多个，所以如果上一条处理指令也是rcpt也可以
    if (smtpData.getLastCommandOrder() != CommandEnum.RCPT.getOrder() - 1
        && smtpData.getLastCommandOrder() != CommandEnum.RCPT.getOrder()) {
      throw new CommandOrderException();
    } else {
      String[] parsedArgs = getParsedArgs(commandString);
      if (parsedArgs.length != 2) {
        smtpData.reply(ReplyMessage.RCPT_ARGS_ERROR.getMessage());
        throw new ArgsErrorException();
      } else {
        if (!parsedArgs[1].toLowerCase(Locale.ROOT).startsWith("to:")) {
          smtpData.reply(ReplyMessage.RCPT_ARGS_ERROR.getMessage());
          throw new ArgsErrorException();
        }
        // 获取邮箱地址
        // 参数不规范
        int leftIndex = parsedArgs[1].indexOf("<");
        int rightIndex = parsedArgs[1].indexOf(">");
        if (leftIndex == -1 || rightIndex == -1) {
          smtpData.reply(ReplyMessage.RCPT_ARGS_ERROR.getMessage());
          throw new ArgsErrorException();
        }
        // 邮箱格式不规范
        // 由于后缀可以修改，就不需要判断了
        String emailAddress = parsedArgs[1].substring(leftIndex + 1, rightIndex);
//        if (!emailAddress.endsWith("@" + ServerConfig.serverParams.getDomainName())) {
//          smtpData.reply(ReplyMessage.RCPT_ARGS_ERROR.getMessage());
//          throw new EmailInvalidException();
//        }
        // 判断用户表中是否存在该账户
        User user =
            userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, emailAddress));
        // 没有对应用户
        if (user == null) {
          smtpData.reply(ReplyMessage.EMAIL_INVALID.getMessage());
          throw new EmailInvalidException();
        }
        // 超出了接收人数上限
        if (smtpData.getRcptList().size() == ServerConfig.serverParams.getMaxRcpt()) {
          throw new RcptNumberExceedException();
        }
        // 验证成功
        smtpData.getRcptList().add(emailAddress);
        smtpData.setLastCommandOrder(CommandEnum.RCPT.getOrder()); // 设置当前指令序号
        smtpData.reply(ReplyMessage.RCPT_OK.getMessage());
      }
    }
  }
}
