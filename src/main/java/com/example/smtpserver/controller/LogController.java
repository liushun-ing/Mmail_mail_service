package com.example.smtpserver.controller;

import com.example.smtpserver.pojo.request.LogRequestBody;
import com.example.smtpserver.pojo.response.LogResponseBody;
import com.example.smtpserver.service.ILogService;
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
@RequestMapping("/log")
public class LogController {

  @Resource ILogService logService;

  Logger logger = Logger.getLogger(LogController.class);

  @RequestMapping("/getLogList")
  public Result<?> getLogList(LogRequestBody.GetLogListReq getLogListReq) {
    logger.info("getLogList");
    try {
      LogResponseBody.GetLogListRes logList = logService.getLogList(getLogListReq);
      HashMap<String, Object> res = new HashMap<>();
      res.put("total", logList.getTotal());
      res.put("logList", logList.getLogList());
      return Result.success(res);
    } catch (Exception e) {
      return Result.error();
    }
  }

  @RequestMapping("/deleteLogByTime")
  public Result<?> deleteLogByTime(LogRequestBody.DeleteLogByTimeReq deleteLogByTimeReq) {
    logger.info("deleteLogByTime");
    try {
      int i = logService.deleteLogByTime(deleteLogByTimeReq);
      HashMap<String, Object> res = new HashMap<>();
      res.put("rows", i);
      return Result.success(res);
    } catch (Exception e) {
      return Result.error();
    }
  }
}
