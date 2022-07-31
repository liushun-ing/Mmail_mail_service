package com.example.smtpserver.pojo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class LogRequestBody {

  @Data
  @Builder
  public static class GetLogListReq {
    private String searchKey;
    private long pageSize;
    private long currentPage;
  }

  @Data
  @Builder
  public static class DeleteLogByTimeReq {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
  }
}
