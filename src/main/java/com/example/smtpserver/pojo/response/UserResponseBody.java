package com.example.smtpserver.pojo.response;

import com.example.smtpserver.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;


public class UserResponseBody {

  @Data
  @Builder
  public static class GetUserListRes {
    private List<User> userList;
    private long total;
  }

  @Data
  @Builder
  public static class JudgeAccountValidRes {
    private Boolean isValid;
    private String msg;
  }

}
