package com.example.smtpserver;

import com.example.smtpserver.pop3.server.Pop3Server;
import com.example.smtpserver.smtp.server.SmtpServer;
import com.example.smtpserver.utils.ApplicationContextHolder;
import com.example.smtpserver.utils.ServerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.smtpserver.mapper")
public class SmtpServerApplication {

  public static void main(String[] args) {
    ApplicationContextHolder.context = SpringApplication.run(SmtpServerApplication.class, args);

    /** 获取服务器参数，和开启邮件服务 */
    ServerConfig.initServerParams();
    ServerConfig.startSmtp();
    ServerConfig.startPop3();
  }
}
