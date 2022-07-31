package com.example.smtpserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.smtpserver.controller.ServerController;
import com.example.smtpserver.entity.FilterAccount;
import com.example.smtpserver.entity.User;
import com.example.smtpserver.exception.UserNotFoundException;
import com.example.smtpserver.mapper.FilterAccountMapper;
import com.example.smtpserver.mapper.UserMapper;
import com.example.smtpserver.pojo.request.UserRequestBody;
import com.example.smtpserver.pojo.response.UserResponseBody;
import com.example.smtpserver.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smtpserver.utils.PasswordMD5;
import com.example.smtpserver.utils.ServerConfig;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务实现类
 *
 * @author ls
 * @since 2022-04-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

  @Resource UserMapper userMapper;

  @Resource FilterAccountMapper filterAccountMapper;

  /** 判断account是否合法 */
  public UserResponseBody.JudgeAccountValidRes judgeAccountValid(String account) {
    if (account == null || "".equals(account)) {
      return UserResponseBody.JudgeAccountValidRes.builder().isValid(false).msg("用户名为空").build();
    }
    if (!account.endsWith("@" + ServerConfig.serverParams.getDomainName())) {
      return UserResponseBody.JudgeAccountValidRes.builder().isValid(false).msg("用户名格式错误").build();
    }
    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, account));
    if (user != null) {
      return UserResponseBody.JudgeAccountValidRes.builder().isValid(false).msg("用户名已存在").build();
    }
    return UserResponseBody.JudgeAccountValidRes.builder().isValid(true).msg("合法").build();
  }

  /** 注册用户 */
  @Override
  public int registerUser(UserRequestBody.RegisterUserReq registerUser) {
    if (registerUser == null
        || "".equals(registerUser.getAccount())
        || "".equals(registerUser.getPassword())) {
      throw new RuntimeException("参数有误");
    }
    return userMapper.insert(
        User.builder()
            .account(registerUser.getAccount())
            .password(PasswordMD5.getPasswordMD5(registerUser.getPassword()))
            .type(1)
            .authority(1)
            .isDisabled(0)
            .build());
  }

  /** 更新用户权限 */
  public int updateAuthority(UserRequestBody.UpdateAuthorityReq updateAuthorityReq) {
    User user = userMapper.selectById(updateAuthorityReq.getUserId());
    if (user == null) {
      throw new UserNotFoundException();
    }
    return userMapper.updateById(
        User.builder()
            .userId(updateAuthorityReq.getUserId())
            .authority(updateAuthorityReq.getAuthority())
            .build());
  }

  /** 更新用户禁用状态 */
  public int updateDisabledState(UserRequestBody.UpdateDisabledReq updateDisabledReq) {
    User user = userMapper.selectById(updateDisabledReq.getUserId());
    if (user == null) {
      throw new UserNotFoundException();
    }
    return userMapper.updateById(
        User.builder()
            .userId(updateDisabledReq.getUserId())
            .isDisabled(updateDisabledReq.getIsDisabled())
            .build());
  }

  /** 删除用户 */
  @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
  public int deleteUser(String userId) {
    User user = userMapper.selectById(userId);
    if (user == null) {
      throw new UserNotFoundException();
    }
    FilterAccount filterAccount =
        filterAccountMapper.selectOne(
            new LambdaQueryWrapper<FilterAccount>()
                .eq(FilterAccount::getAccount, user.getAccount()));
    int i = 0;
    try {
      if (filterAccount != null) {
        filterAccountMapper.deleteById(filterAccount.getId());
      }
      i = userMapper.deleteById(userId);
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      throw e;
    }
    return i;
  }

  /** 验证原密码是否正确 */
  public boolean verifyPassword(UserRequestBody.VerifyPasswordReq verifyPasswordReq) {
    User user = userMapper.selectById(verifyPasswordReq.getUserId());
    if (user == null) {
      throw new UserNotFoundException();
    }

    if (!user.getPassword().equals(PasswordMD5.getPasswordMD5(verifyPasswordReq.getPassword()))) {
      return false;
    }
    return true;
  }

  /** 修改密码 */
  public int changePassword(UserRequestBody.ChangePasswordReq changePasswordReq) {
    User user = userMapper.selectById(changePasswordReq.getUserId());
    if (user == null) {
      throw new UserNotFoundException();
    }

    if (changePasswordReq.getNewPassword() == null
        || "".equals(changePasswordReq.getNewPassword())) {
      throw new RuntimeException("参数错误");
    }

    return userMapper.updateById(
        User.builder()
            .userId(changePasswordReq.getUserId())
            .password(PasswordMD5.getPasswordMD5(changePasswordReq.getNewPassword()))
            .build());
  }

  @Override
  public UserResponseBody.GetUserListRes getUserList(
      UserRequestBody.GetUserListReq getUserListReq) {
    Page<User> pageHelper =
        new Page<>(getUserListReq.getCurrentPage(), getUserListReq.getPageSize());
    Page<User> userPage =
        userMapper.selectPage(
            pageHelper,
            new LambdaQueryWrapper<User>()
                .like(
                    getUserListReq.getSearchKey() != null
                        && !"".equals(getUserListReq.getSearchKey()),
                    User::getAccount,
                    getUserListReq.getSearchKey())
                .or()
                .like(
                    getUserListReq.getSearchKey() != null
                        && !"".equals(getUserListReq.getSearchKey()),
                    User::getNickname,
                    getUserListReq.getSearchKey()));
    return UserResponseBody.GetUserListRes.builder()
        .userList(userPage.getRecords())
        .total(userPage.getTotal())
        .build();
  }

  @Override
  public List<User> searchUser(String account) {
    if (account == null || account.length() == 0) {
      throw new RuntimeException("参数为空");
    }
    return userMapper.selectList(
        new LambdaQueryWrapper<User>()
            .like(account != null && account.length() != 0, User::getAccount, account));
  }
}
