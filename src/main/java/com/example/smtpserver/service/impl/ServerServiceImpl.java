package com.example.smtpserver.service.impl;

import com.example.smtpserver.entity.Server;
import com.example.smtpserver.mapper.ServerMapper;
import com.example.smtpserver.pojo.response.ServerResponseBody;
import com.example.smtpserver.service.IServerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smtpserver.utils.ServerConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 服务实现类
 *
 * @author ls
 * @since 2022-04-30
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, Server> implements IServerService {

  @Resource ServerMapper serverMapper;

  @Override
  public Server getServerParams() {
    return serverMapper.selectById(1);
  }

  public int setMailboxSize(Integer mailboxSize) {
    int i = serverMapper.updateById(Server.builder().id(1).mailboxSize(mailboxSize).build());
    ServerConfig.updateServerParams();
    return i;
  }

  @Override
  public int setSmtpPort(Integer smtpPort) {
    int i = serverMapper.updateById(Server.builder().id(1).smtpPort(smtpPort).build());
    ServerConfig.updateServerParams();
    ServerConfig.stopSmtp();
    ServerConfig.startSmtp();
    return i;
  }

  @Override
  public int setPop3Port(Integer pop3Port) {
    int i = serverMapper.updateById(Server.builder().id(1).pop3Port(pop3Port).build());
    ServerConfig.updateServerParams();
    // 还需要重启服务
    ServerConfig.stopPop3();
    ServerConfig.startPop3();
    return i;
  }

  /** 设置域名 */
  @Override
  public int setDomainName(String domainName) {
    if (domainName == null) {
      throw new RuntimeException("参数为空");
    }
    if (!domainName.endsWith(".com")) {
      throw new RuntimeException("参数非法");
    }
    int i = serverMapper.updateById(Server.builder().id(1).domainName(domainName).build());
    ServerConfig.updateServerParams();
    return i;
  }

  @Override
  public int setMaxRcpt(Integer maxRcpt) {
    if (maxRcpt <= 0) {
      throw new RuntimeException("参数不合法");
    }
    int i = serverMapper.updateById(Server.builder().id(1).maxRcpt(maxRcpt).build());
    ServerConfig.updateServerParams();
    return i;
  }

  @Override
  public ServerResponseBody.GetSmtpAndPop3StateRes getSmtpAndPop3State() {
    return ServerResponseBody.GetSmtpAndPop3StateRes.builder()
        .smtpState(ServerConfig.getSmtpState())
        .pop3State(ServerConfig.getPop3State())
        .build();
  }

  @Override
  public int stopSmtpServer() {
    return ServerConfig.stopSmtp();
  }

  @Override
  public int startSmtpServer() {
    return ServerConfig.startSmtp();
  }

  @Override
  public int stopPop3Server() {
    return ServerConfig.stopPop3();
  }

  @Override
  public int startPop3Server() {
    return ServerConfig.startPop3();
  }
}
