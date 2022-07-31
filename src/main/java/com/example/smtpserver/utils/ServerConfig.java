package com.example.smtpserver.utils;

import com.example.smtpserver.entity.Server;
import com.example.smtpserver.mapper.ServerMapper;
import com.example.smtpserver.pop3.server.Pop3Server;
import com.example.smtpserver.smtp.server.SmtpServer;
import lombok.Data;
import org.springframework.stereotype.Component;

/** 配置信息常量 */
@Component
@Data
public class ServerConfig {

  // 服务器参数
  public static Server serverParams;

  public static Thread smtpThread;

  public static Thread pop3Thread;

  public static void initServerParams() {
    ServerMapper serverMapper =
        ApplicationContextHolder.getBean("serverMapper", ServerMapper.class);
    serverParams = serverMapper.selectById(1);
  }

  public static void updateServerParams() {
    initServerParams();
  }

  public static int startSmtp() {
    if (smtpThread == null) {
      smtpThread = new Thread(SmtpServer::startSmtpServer);
    }
    if (!smtpThread.isAlive()) {
      smtpThread.start();
      return 1;
    }
    return 0;
  }

  public static int stopSmtp() {
    if (!smtpThread.isInterrupted()) {
      SmtpServer.shutDownSmtpServer();
      smtpThread.interrupt();
      smtpThread = null;
      return 1;
    }
    return 0;
  }

  public static int startPop3() {
    if (pop3Thread == null) {
      pop3Thread = new Thread(Pop3Server::startPop3Server);
    }
    if (!pop3Thread.isAlive()) {
      pop3Thread.start();
      return 1;
    }
    return 0;
  }

  public static int stopPop3() {
    if (!pop3Thread.isInterrupted()) {
      Pop3Server.shutDownPop3Server();
      pop3Thread.interrupt();
      pop3Thread = null;
      return 1;
    }
    return 0;
  }

  public static boolean getSmtpState() {
    if (smtpThread != null && smtpThread.isAlive()) {
      return true;
    }
    return false;
  }

  public static boolean getPop3State() {
    if (pop3Thread != null && pop3Thread.isAlive()) {
      return true;
    }
    return false;
  }
}
