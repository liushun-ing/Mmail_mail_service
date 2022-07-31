package com.example.smtpserver.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Roles {

  // 超级管理员
  Admin(0, "admin"),
  // 高级管理员
  User(1, "user");

  private int authority;
  private String role;
}
