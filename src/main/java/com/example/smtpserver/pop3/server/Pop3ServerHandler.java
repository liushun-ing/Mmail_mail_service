package com.example.smtpserver.pop3.server;

import com.example.smtpserver.utils.ReplyBuilder;
import com.example.smtpserver.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.Logger;

public class Pop3ServerHandler extends SimpleChannelInboundHandler<String> {

  // 每个handler都需要有一个自己的SmtpDate
  private Pop3Data pop3Data;
  // 日志
  private Logger logger = Logger.getLogger(Pop3ServerHandler.class);
  // channel对象数组,ChannelGroup是一个线程安全的集合
  private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  // 指令处理对象
  private CommandHandler commandHandler = new CommandHandler();
  /** 新连接建立 */
  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    this.pop3Data = new Pop3Data(channel);
    channelGroup.add(channel);
    // 回复
    this.pop3Data.reply(ReplyBuilder.buildReadyOkReply());
    // 打印日志
    logger.info(
        Utils.getCurrentTime()
            + ": "
            + channel.remoteAddress()
            + "连接，当前连接数："
            + channelGroup.size());
  }

  /** 读取数据，并进行处理 */
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s)
      throws Exception {
    Channel channel = channelHandlerContext.channel();
    logger.info(Utils.getCurrentTime() + ": " + channel.remoteAddress() + "发送: " + s);
    // 交给指令处理函数处理
    commandHandler.handleCommand(this.pop3Data, s);
  }

  /** 连接断开 */
  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    // 打印日志
    logger.info(
        Utils.getCurrentTime()
            + ": "
            + channel.remoteAddress()
            + "断开连接，剩余连接数："
            + channelGroup.size());
  }

  /** 出现异常 */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    Channel channel = ctx.channel();
    channel.close();
    logger.debug("连接出现异常：" + cause.getMessage());
  }
}
