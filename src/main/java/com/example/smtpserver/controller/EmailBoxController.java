package com.example.smtpserver.controller;

import com.example.smtpserver.service.IEmailBoxService;
import com.example.smtpserver.utils.Result;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
@RestController
@CrossOrigin
@RequestMapping("/emailBox")
public class EmailBoxController {

  @Resource
  IEmailBoxService emailBoxService;

  Logger logger = Logger.getLogger(EmailBoxController.class);

  @RequestMapping("/getTotalEmailNum")
  public Result<?> getTotalEmailNum() {
    logger.info("getTotalEmailNum");
    try {
      int totalEmailNum = emailBoxService.getTotalEmailNum();
      HashMap<String, Object> res = new HashMap<>();
      res.put("total", totalEmailNum);
      return Result.success(res);
    } catch (Exception e) {
      return Result.error();
    }
  }


}

