package com.example.smtpserver.smtp.server;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpserver.entity.EmailBox;
import com.example.smtpserver.entity.FilterAccount;
import com.example.smtpserver.entity.Log;
import com.example.smtpserver.entity.User;
import com.example.smtpserver.exception.*;
import com.example.smtpserver.mapper.EmailBoxMapper;
import com.example.smtpserver.mapper.FilterAccountMapper;
import com.example.smtpserver.mapper.LogMapper;
import com.example.smtpserver.mapper.UserMapper;
import com.example.smtpserver.smtp.command.BaseCommand;
import com.example.smtpserver.utils.ReplyMessage;
import com.example.smtpserver.utils.ApplicationContextHolder;
import com.example.smtpserver.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** 指令处理类 */
public class CommandHandler {

  UserMapper userMapper;
  EmailBoxMapper emailBoxMapper;
  FilterAccountMapper filterAccountMapper;
  LogMapper logMapper;

  // 指令集
  public Map<String, BaseCommand> commandMap;
  // 日志
  Logger logger = Logger.getLogger(CommandHandler.class);

  // 构造函数初始化指令集
  public CommandHandler() {
    commandMap = new HashMap<>();
    for (CommandEnum commandEnum : CommandEnum.values()) {
      commandMap.put(commandEnum.getBaseCommand().getCommandName(), commandEnum.getBaseCommand());
    }
    userMapper = ApplicationContextHolder.getBean("userMapper", UserMapper.class);
    emailBoxMapper = ApplicationContextHolder.getBean("emailBoxMapper", EmailBoxMapper.class);
    logMapper = ApplicationContextHolder.getBean("logMapper", LogMapper.class);
    filterAccountMapper =
        ApplicationContextHolder.getBean("filterAccountMapper", FilterAccountMapper.class);
  }

  /**
   * 处理指令的主要逻辑
   *
   * @param smtpData 每个连接的smtp数据
   * @param commandLine 指令字符串
   */
  @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
  public void handleCommand(SmtpData smtpData, String commandLine) {
    try {
      /**
       * 需要注意的： 1 执行完auth login之后，需要进行账号密码登陆 2 执行完data之后，需要读取邮件的内容，时多行文本本 3
       * 读取完内容之后，遇到单行的[.]符号，就需要终止输入，回复一个完成邮件发送成功消息 4 由于我们在指令执行时，已经处理过顺序问题，所以这里就不需要考虑了
       */
      // 首先是判断是否登陆
      if (smtpData.getState() == 1) {
        // 当他为 1 时，接收到的下一个指令应该是账号了
        try {
          String account = Utils.getUtf8FromBase64(commandLine);
          // 传过来的账号是有邮箱后缀的
          smtpData.getUser().setAccount(account);
          smtpData.setState(2);
          smtpData.reply(ReplyMessage.AUTH_USERNAME_OK.getMessage());
          logger.info("账号：" + account);
          return;
        } catch (Exception e) {
          smtpData.reply(ReplyMessage.AUTH_USERNAME_FAILURE.getMessage());
          logger.debug(e.toString());
          e.printStackTrace();
        } finally {
          return;
        }
      } else if (smtpData.getState() == 2) {
        // 当他为 2 时，下一个就是接收密码了
        try {
          String password = Utils.getUtf8FromBase64(commandLine);
          logger.info("密码：" + password);
          smtpData.getUser().setPassword(password);
          // 接收到之后，需要判断密码是否正确，也就是有没有这个人
          User user =
              userMapper.selectOne(
                  new LambdaQueryWrapper<User>()
                      .eq(User::getAccount, smtpData.getUser().getAccount())
                      .eq(User::getPassword, smtpData.getUser().getPassword()));
          if (user == null) {
            // 验证失败就让他重新输入账号密码
            smtpData.setState(1);
            smtpData.reply(ReplyMessage.AUTH_FAILURE.getMessage());
          } else {
            smtpData.setState(3);
            smtpData.reply(ReplyMessage.AUTH_PASSWORD_OK.getMessage());
          }
          return;
        } catch (Exception e) {
          smtpData.reply(ReplyMessage.AUTH_PASSWORD_FAILURE.getMessage());
          logger.debug(e.toString());
          e.printStackTrace();
        } finally {
          return;
        }
      }

      /** 然后是是否处在读取邮件主体阶段 注意遇到单行的.时，需要停止读取 */
      if (smtpData.getIsWaitingData() == 1) {
        if (".".equals(commandLine)) {
          try {
            // 账号过滤
            FilterAccount filterAccount =
                filterAccountMapper.selectOne(
                    new LambdaQueryWrapper<FilterAccount>()
                        .eq(FilterAccount::getAccount, smtpData.getEmailBox().getFromEmail()));
            if (filterAccount == null) {
              // 需要向数据库中添加记录
              for (String rcpt : smtpData.getRcptList()) {
                emailBoxMapper.insert(
                    EmailBox.builder()
                        .fromEmail(smtpData.getEmailBox().getFromEmail())
                        .toEmail(rcpt)
                        .sendTime(new Date())
                        .state(0)
                        .size(
                            smtpData
                                .getEmailBox()
                                .getData()
                                .getBytes(StandardCharsets.UTF_8)
                                .length)
                        .data(smtpData.getEmailBox().getData())
                        .build());
                // 添加日志
                logMapper.insert(
                    Log.builder()
                        .logTime(new Date())
                        .type("smtp")
                        .content(
                            smtpData.getEmailBox().getFromEmail()
                                + " to "
                                + rcpt
                                + " size: "
                                + smtpData
                                    .getEmailBox()
                                    .getData()
                                    .getBytes(StandardCharsets.UTF_8)
                                    .length)
                        .build());
              }
            }
            logger.info("邮件发送成功");
            smtpData.reply(ReplyMessage.EMAIL_SEND_OK.getMessage());
          } catch (Exception e) {
            logger.debug("邮件发送失败");
            smtpData.reply(ReplyMessage.EMAIL_SEND_ERROR.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e.getMessage());
          }
          // 重置状态
          smtpData.setIsWaitingData(0);
          return;
        } else {
          // 否则将读取到的内容，追加到正文尾部即可
          String data = smtpData.getEmailBox().getData();
          if (data == null) {
            data = "";
          }
          data += (commandLine + "\r\n");
          smtpData.getEmailBox().setData(data);
          logger.info("read email data: " + commandLine);
          return;
        }
      }

      // 只有当不处在需要接收账号密码或者接收邮件主体时，才调用指令执行函数
      getCommand(commandLine).handle(smtpData, commandLine);
    } catch (CommandInvalidException e) {
      logger.debug(e.toString());
      smtpData.reply(ReplyMessage.COMMAND_INVALID.getMessage());
      e.printStackTrace();
    } catch (CommandOrderException e) {
      logger.debug(e.toString());
      smtpData.reply(ReplyMessage.ERROR_ORDER.getMessage());
      e.printStackTrace();
    } catch (ArgsErrorException e) {
      logger.debug(e.toString());
      // 由于每个指令的参数不一致，所以不统一回复，自己回复自己的
      e.printStackTrace();
    } catch (EmailInvalidException e) {
      logger.debug(e.toString());
      // 由于mail和rcpt指令的参数不一致，所以不统一回复，自己回复自己的
      e.printStackTrace();
    } catch (RcptNumberExceedException e) {
      logger.debug(e.toString());
      smtpData.reply(ReplyMessage.RCPT_EXCEED_ERROR.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      logger.debug(e.toString());
      smtpData.reply(ReplyMessage.UNKNOWN_ERROR.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * 获取指令的前四位，方便区分和进一步处理
   *
   * @return
   */
  public String getMainCommand(String commandLine) {
    // 如果指令为空，或者指令长度小于4，或者大于4时，第五位不是空格，或者不是相关指令则报错
    if (commandLine == null
        || commandLine.length() < 4
        || (commandLine.length() > 4 && commandLine.charAt(4) != ' ')) {
      throw new CommandInvalidException();
    } else {
      String command = commandLine.substring(0, 4).toLowerCase(Locale.ROOT);
      if (commandMap.containsKey(command)) {
        return command;
      } else {
        throw new CommandInvalidException();
      }
    }
  }

  public BaseCommand getCommand(String commandLine) {
    String mainCommand = getMainCommand(commandLine);
    return commandMap.get(mainCommand);
  }
}
