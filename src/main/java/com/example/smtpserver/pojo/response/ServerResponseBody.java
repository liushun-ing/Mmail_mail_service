package com.example.smtpserver.pojo.response;


import lombok.Builder;
import lombok.Data;

public class ServerResponseBody {

  @Data
  @Builder
  public static class GetSmtpAndPop3StateRes {
    private Boolean smtpState;
    private Boolean pop3State;
  }
}
