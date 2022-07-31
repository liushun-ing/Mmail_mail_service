package com.example.smtpserver.pojo.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseBody {
  private String userId;
  private String account;
  private String nickname;
  private Integer authority;
  private Integer type;
  private String token;
}
