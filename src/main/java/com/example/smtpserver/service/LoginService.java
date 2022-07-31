package com.example.smtpserver.service;


import com.example.smtpserver.pojo.request.LoginRequestBody;
import com.example.smtpserver.pojo.response.LoginResponseBody;

public interface LoginService {
  LoginResponseBody login(LoginRequestBody loginRequestBody);
}
