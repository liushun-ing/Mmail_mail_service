package com.example.smtpserver.service;

import com.example.smtpserver.entity.Server;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.smtpserver.pojo.response.ServerResponseBody;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
public interface IServerService extends IService<Server> {

  Server getServerParams();

  int setMailboxSize(Integer mailboxSize);

  int setSmtpPort(Integer smtpPort);

  int setPop3Port(Integer pop3Port);

  int setDomainName(String domainName);

  int setMaxRcpt(Integer maxRcpt);

  ServerResponseBody.GetSmtpAndPop3StateRes getSmtpAndPop3State();

  int stopSmtpServer();

  int startSmtpServer();

  int stopPop3Server();

  int startPop3Server();
}
