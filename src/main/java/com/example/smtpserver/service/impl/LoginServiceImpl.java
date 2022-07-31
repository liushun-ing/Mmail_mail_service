package com.example.smtpserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpserver.entity.User;
import com.example.smtpserver.exception.PasswordErrorException;
import com.example.smtpserver.exception.SignTokenException;
import com.example.smtpserver.exception.UserInvalidException;
import com.example.smtpserver.exception.UserNotFoundException;
import com.example.smtpserver.mapper.UserMapper;
import com.example.smtpserver.pojo.request.LoginRequestBody;
import com.example.smtpserver.pojo.response.LoginResponseBody;
import com.example.smtpserver.service.LoginService;
import com.example.smtpserver.utils.JWTUtil;
import com.example.smtpserver.utils.PasswordMD5;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {

  @Resource
  private UserMapper userMapper;

  @Resource
  private JWTUtil JWTUtil;

  @Override
  public LoginResponseBody login(LoginRequestBody loginRequestBody) {

    // 查询用户
    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, loginRequestBody.getAccount()));
    if (user == null) {
      throw new UserNotFoundException();
    }

    if (user.getIsDisabled() == 1) {
      throw new UserInvalidException();
    }

    if (user.getAuthority() == 0) {
      throw new RuntimeException("该用户不是管理员");
    }

    // 需要加密后进行判断
    if (!user.getPassword().equals(PasswordMD5.getPasswordMD5(loginRequestBody.getPassword()))) {
      throw new PasswordErrorException();
    }

    String token;
    try {
      // 生成token
      token = this.JWTUtil.sign(user.getAccount());
    } catch (Exception e) {
      e.printStackTrace();
      throw new SignTokenException();
    }

    return LoginResponseBody.builder()
        .userId(user.getUserId())
        .account(user.getAccount())
        .nickname(user.getNickname())
        .authority(user.getAuthority())
        .type(user.getType())
        .token(token)
        .build();
  }
}
