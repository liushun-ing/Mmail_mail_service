package com.example.smtpserver.service;

import com.example.smtpserver.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.smtpserver.pojo.request.LogRequestBody;
import com.example.smtpserver.pojo.response.LogResponseBody;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
public interface ILogService extends IService<Log> {

  LogResponseBody.GetLogListRes getLogList(LogRequestBody.GetLogListReq getLogListReq);

  int deleteLogByTime(LogRequestBody.DeleteLogByTimeReq deleteLogByTimeReq);
}
