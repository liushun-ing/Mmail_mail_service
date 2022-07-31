package com.example.smtpserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.smtpserver.entity.Log;
import com.example.smtpserver.entity.User;
import com.example.smtpserver.mapper.LogMapper;
import com.example.smtpserver.pojo.request.LogRequestBody;
import com.example.smtpserver.pojo.request.UserRequestBody;
import com.example.smtpserver.pojo.response.LogResponseBody;
import com.example.smtpserver.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 服务实现类
 *
 * @author ls
 * @since 2022-04-30
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

  @Resource LogMapper logMapper;

  @Override
  public LogResponseBody.GetLogListRes getLogList(LogRequestBody.GetLogListReq getLogListReq) {
    Page<Log> pageHelper = new Page<>(getLogListReq.getCurrentPage(), getLogListReq.getPageSize());
    Page<Log> logPage =
        logMapper.selectPage(
            pageHelper,
            new LambdaQueryWrapper<Log>()
                .like(
                    getLogListReq.getSearchKey() != null
                        && !"".equals(getLogListReq.getSearchKey()),
                    Log::getType,
                    getLogListReq.getSearchKey())
                .or()
                .like(
                    getLogListReq.getSearchKey() != null
                        && !"".equals(getLogListReq.getSearchKey()),
                    Log::getContent,
                    getLogListReq.getSearchKey())
                .orderByDesc(Log::getLogTime));
    return LogResponseBody.GetLogListRes.builder()
        .logList(logPage.getRecords())
        .total(logPage.getTotal())
        .build();
  }

  @Override
  public int deleteLogByTime(LogRequestBody.DeleteLogByTimeReq deleteLogByTimeReq) {
    if (deleteLogByTimeReq == null
        || deleteLogByTimeReq.getStartTime() == null
        || deleteLogByTimeReq.getEndTime() == null) {
      throw new RuntimeException("参数错误");
    }

    return logMapper.delete(
        new LambdaQueryWrapper<Log>()
            .between(
                Log::getLogTime,
                deleteLogByTimeReq.getStartTime(),
                deleteLogByTimeReq.getEndTime()));
  }
}
