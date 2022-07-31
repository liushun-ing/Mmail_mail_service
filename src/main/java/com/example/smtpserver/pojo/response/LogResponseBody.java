package com.example.smtpserver.pojo.response;

import com.example.smtpserver.entity.Log;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class LogResponseBody {

  @Data
  @Builder
  public static class GetLogListRes {
    private long total;
    private List<Log> logList;
  }

}
