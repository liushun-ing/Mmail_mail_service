package com.example.smtpserver.smtp.command;

import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.exception.CommandOrderException;
import com.example.smtpserver.smtp.server.CommandEnum;
import com.example.smtpserver.smtp.server.SmtpData;
import com.example.smtpserver.utils.ReplyMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class QuitCommand extends BaseCommand{
  public QuitCommand() {
    super("quit");
  }

  @Override
  public void handle(SmtpData smtpData, String commandString) throws Exception {
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 1) {
      smtpData.reply(ReplyMessage.QUIT_ARGS_ERROR.getMessage());
      throw new ArgsErrorException();
    }
    // 在回复完消息之后，将通道关闭
    ChannelFuture future = smtpData.getChannel().writeAndFlush(ReplyMessage.QUIT_OK.getMessage());
    // 添加异步回调事件，在回调队列中等待执行
    future.addListener(ChannelFutureListener.CLOSE);
  }
}
