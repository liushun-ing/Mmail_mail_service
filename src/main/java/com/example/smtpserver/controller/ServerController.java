package com.example.smtpserver.controller;

import com.example.smtpserver.entity.Server;
import com.example.smtpserver.pojo.response.ServerResponseBody;
import com.example.smtpserver.service.IServerService;
import com.example.smtpserver.utils.Result;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 前端控制器
 *
 * @author ls
 * @since 2022-04-30
 */
@RestController
@CrossOrigin
@RequestMapping("/server")
public class ServerController {

  @Resource IServerService serverService;

  Logger logger = Logger.getLogger(ServerController.class);

  /** 获取服务器配置 */
  @RequestMapping("/getServerParams")
  public Result<?> getServerParams() {
    logger.info("getServerParams");
    try {
      Server serverParams = serverService.getServerParams();
      HashMap<String, Object> res = new HashMap<>();
      res.put("serverParams", serverParams);
      return Result.success(res);
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 设置邮箱大小 */
  @RequestMapping("/setMailboxSize")
  public Result<Void> setMailboxSize(Integer mailboxSize) {
    logger.info("setMailboxSize");
    try {
      int i = serverService.setMailboxSize(mailboxSize);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 设置smtp端口 */
  @RequestMapping("/setSmtpPort")
  public Result<Void> setSmtpPort(Integer smtpPort) {
    logger.info("setSmtpPort");
    try {
      int i = serverService.setSmtpPort(smtpPort);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 设置pop3端口 */
  @RequestMapping("/setPop3Port")
  public Result<Void> setPop3Port(Integer pop3Port) {
    logger.info("setPop3Port");
    try {
      int i = serverService.setPop3Port(pop3Port);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 设置域名 */
  @RequestMapping("/setDomainName")
  public Result<Void> setDomainName(String domainName) {
    logger.info("setDomainName");
    try {
      int i = serverService.setDomainName(domainName);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  /** 设置最大群发数 */
  @RequestMapping("/setMaxRcpt")
  public Result<Void> setMaxRcpt(Integer maxRcpt) {
    logger.info("setMaxRcpt");
    try {
      int i = serverService.setMaxRcpt(maxRcpt);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  /** 获取smtp和pop3服务状态 */
  @RequestMapping("/getSmtpAndPop3State")
  public Result<?> getSmtpAndPop3State() {
    logger.info("getSmtpAndPop3State");
    try {
      ServerResponseBody.GetSmtpAndPop3StateRes smtpAndPop3State =
          serverService.getSmtpAndPop3State();
      HashMap<String, Object> res = new HashMap<>();
      res.put("smtpState", smtpAndPop3State.getSmtpState());
      res.put("pop3State", smtpAndPop3State.getPop3State());
      return Result.success(res);
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 关闭smtp服务 */
  @RequestMapping("/stopSmtpServer")
  public Result<Void> stopSmtpServer() {
    logger.info("stopSmtpServer");
    try {
      int i = serverService.stopSmtpServer();
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  /** 开启smtp服务 */
  @RequestMapping("/startSmtpServer")
  public Result<Void> startSmtpServer() {
    logger.info("startSmtpServer");
    try {
      int i = serverService.startSmtpServer();
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  /** 关闭pop3服务 */
  @RequestMapping("/stopPop3Server")
  public Result<Void> stopPop3Server() {
    logger.info("stopPop3Server");
    try {
      int i = serverService.stopPop3Server();
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  /** 开启pop3服务 */
  @RequestMapping("/startPop3Server")
  public Result<Void> startPop3Server() {
    logger.info("startPop3Server");
    try {
      int i = serverService.startPop3Server();
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }
}
