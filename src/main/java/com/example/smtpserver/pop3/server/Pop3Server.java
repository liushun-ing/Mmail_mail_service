package com.example.smtpserver.pop3.server;

import com.example.smtpserver.utils.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

public class Pop3Server {

  // 接收线程组
  private static EventLoopGroup bossGroup;
  // 工作线程组，用来处理网络操作
  private static EventLoopGroup workerGroup;
  // 通道
  private static ChannelFuture channelFuture;
  // 日志
  private static Logger logger = Logger.getLogger(Pop3Server.class);

  /**
   * 利用netty异步线程，开启smtp服务
   *
   * @throws Exception
   */
  public static void startPop3Server() {
    try {
      bossGroup = new NioEventLoopGroup();
      workerGroup = new NioEventLoopGroup();
      // 创建服务器启动助手来配置netty参数
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap
          .group(bossGroup, workerGroup) // 设置两个线程组
          .channel(NioServerSocketChannel.class) // 设置使用NioServerSocketChannel作为服务器通道的实现
          .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列中等待连接的个数
          .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活跃状态
          .childHandler(new Pop3ServerInitializer()); // 添加编码解码和处理器
      // 绑定端口pop3的110端口，以非阻塞的形式
      channelFuture = serverBootstrap.bind(ServerConfig.serverParams.getPop3Port()).sync();
      // 向通道增加监听，
      channelFuture.addListener(
          (ChannelFutureListener)
              channelFuture -> {
                logger.info("pop3 server is running......");
              });
      // 以阻塞方式关闭通道
      channelFuture.channel().closeFuture().sync();
    } catch (Exception e) {
      logger.debug(e.getMessage());
      e.printStackTrace();
    } finally {
      shutDownPop3Server();
      logger.info("pop3 is shutdown...");
    }
  }

  /** 终止netty相关数据，关闭smtp服务 但是由于服务器一直运行，所以此函数暂时不会调用 */
  public static void shutDownPop3Server() {
//    if (channelFuture != null) {
//      channelFuture.channel().closeFuture().syncUninterruptibly();
//    }
    if ((bossGroup != null) && (!bossGroup.isShutdown())) {
      bossGroup.shutdownGracefully();
    }
    if ((workerGroup != null) && (!workerGroup.isShutdown())) {
      workerGroup.shutdownGracefully();
    }
  }

  // 可以用于测试
  public static void main(String[] args) {
    try {
      startPop3Server();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
