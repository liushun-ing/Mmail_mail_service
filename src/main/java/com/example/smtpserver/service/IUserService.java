package com.example.smtpserver.service;

import com.example.smtpserver.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.smtpserver.pojo.request.UserRequestBody;
import com.example.smtpserver.pojo.response.UserResponseBody;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
public interface IUserService extends IService<User> {

  UserResponseBody.JudgeAccountValidRes judgeAccountValid(String account);

  int registerUser(UserRequestBody.RegisterUserReq registerUser);

  int updateAuthority(UserRequestBody.UpdateAuthorityReq updateAuthorityReq);

  int updateDisabledState(UserRequestBody.UpdateDisabledReq updateDisabledReq);

  int deleteUser(String userId);

  boolean verifyPassword(UserRequestBody.VerifyPasswordReq verifyPasswordReq);

  int changePassword(UserRequestBody.ChangePasswordReq changePasswordReq);

  UserResponseBody.GetUserListRes getUserList(UserRequestBody.GetUserListReq getUserListReq);

  List<User> searchUser(String account);
}
