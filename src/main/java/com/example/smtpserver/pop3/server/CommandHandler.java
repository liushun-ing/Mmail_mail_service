package com.example.smtpserver.pop3.server;

import com.example.smtpserver.exception.*;
import com.example.smtpserver.pop3.command.BaseCommand;
import com.example.smtpserver.utils.ReplyBuilder;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** 指令处理类 */
public class CommandHandler {

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
  }

  /**
   * 处理指令的主要逻辑
   *
   * @param pop3Data 每个连接的pop3数据
   * @param commandLine 指令字符串
   */
  public void handleCommand(Pop3Data pop3Data, String commandLine) {
    try {
      // 由于pop3的指令之间没有顺序要求，所以直接调用command执行即可，
      // 唯一有顺序要求的user和pass在各自指令的执行函数中已经处理了
      getCommand(commandLine).handle(pop3Data, commandLine);
    } catch (CommandInvalidException e) {
      logger.debug(e.toString());
      pop3Data.reply(ReplyBuilder.buildCommandInvalidReply());
      e.printStackTrace();
    } catch (IndexExceedException e) {
      logger.debug(e.toString());
      pop3Data.reply(ReplyBuilder.buildIndexExceedErrReply());
      e.printStackTrace();
    } catch (CommandOrderException e) {
      logger.debug(e.toString());
      pop3Data.reply(ReplyBuilder.buildCommandOrderErrReply());
      e.printStackTrace();
    } catch (ArgsErrorException e) {
      logger.debug(e.toString());
      // 由于每个指令的参数不一致，所以不统一回复，自己回复自己的
      e.printStackTrace();
    } catch (Exception e) {
      logger.debug(e.toString());
      pop3Data.reply(ReplyBuilder.buildUnknownErrReply());
      e.printStackTrace();
    }
  }

  /**
   * 获取指令的前几位，方便区分和进一步处理 需要注意的是top指令只有三位，其余的都是四位
   *
   * @return
   */
  public String getMainCommand(String commandLine) {
    if (commandLine == null) {
      throw new CommandInvalidException();
    }
    if (commandLine.length() < 4) {
      // 如果刚好是top，应该让他去执行top的处理函数,而不是直接说指令invalid
      if (commandLine.length() == 3
          && commandLine
              .toLowerCase(Locale.ROOT)
              .equals(CommandEnum.TOP.getBaseCommand().getCommandName())) {
        return commandLine;
      }
      throw new CommandInvalidException();
    } else {
      if ((commandLine.length() > 4 && commandLine.charAt(4) != ' ')) {
        // 单独处理一下top指令
        if (commandLine.charAt(3) == ' ') {
          // 如果第5位不为空格，但是第四位是空格，就说明可能是top
          String command = commandLine.substring(0, 3).toLowerCase(Locale.ROOT);
          if (commandMap.containsKey(command)) {
            return command;
          }
        }
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
  }

  public BaseCommand getCommand(String commandLine) {
    String mainCommand = getMainCommand(commandLine);
    return commandMap.get(mainCommand);
  }
}
