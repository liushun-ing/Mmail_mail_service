package com.example.smtpserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterAccountDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String account;
  private String nickname;
  private Integer authority;
  private String phone;
  private String avatar;
}
