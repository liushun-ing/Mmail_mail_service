package com.example.smtpserver.smtp.server;

import com.example.smtpserver.utils.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

public class SmtpServer {

  // 接收线程组
  private static EventLoopGroup bossGroup;
  // 工作线程组，用来处理网络操作
  private static EventLoopGroup workerGroup;
  // 通道
  private static ChannelFuture channelFuture;
  // 日志
  private static Logger logger = Logger.getLogger(SmtpServer.class);

  /**
   * 利用netty异步线程，开启smtp服务
   *
   * @throws Exception
   */
  public static void startSmtpServer() {
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
          .childHandler(new SmtpServerInitializer()); // 添加编码解码和处理器
      // 绑定端口smtp25端口，以非阻塞的形式
      channelFuture = serverBootstrap.bind(ServerConfig.serverParams.getSmtpPort()).sync();
      // 向通道增加监听，
      channelFuture.addListener(
          (ChannelFutureListener)
              channelFuture -> {
                logger.info("smtp server is running......");
              });
      // 以阻塞方式关闭通道
      channelFuture.channel().closeFuture().sync();
    } catch (Exception e) {
      logger.debug(e.getMessage());
      e.printStackTrace();
    } finally {
      shutDownSmtpServer();
      logger.info("smtp is shutdown...");
    }
  }

  /** 终止netty相关数据，关闭smtp服务 但是由于服务器一直运行，所以此函数暂时不会调用 */
  public static void shutDownSmtpServer() {
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
      startSmtpServer();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
