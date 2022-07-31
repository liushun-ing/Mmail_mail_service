package com.example.smtpserver.controller;

import com.example.smtpserver.entity.User;
import com.example.smtpserver.exception.UserNotFoundException;
import com.example.smtpserver.pojo.request.UserRequestBody;
import com.example.smtpserver.pojo.response.UserResponseBody;
import com.example.smtpserver.service.IUserService;
import com.example.smtpserver.utils.Result;
import com.example.smtpserver.utils.ResultEnum;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 前端控制器
 *
 * @author ls
 * @since 2022-04-30
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

  @Resource IUserService userService;

  Logger logger = Logger.getLogger(UserController.class);

  /** 判断用户名是否合法 */
  @RequestMapping("/judgeAccountValid")
  public Result<?> judgeAccountValid(String account) {
    logger.info("judgeAccountValid");
    try {
      UserResponseBody.JudgeAccountValidRes judgeAccountValidRes =
          userService.judgeAccountValid(account);
      HashMap<String, Object> res = new HashMap<>();
      res.put("isValid", judgeAccountValidRes.getIsValid());
      res.put("msg", judgeAccountValidRes.getMsg());
      return Result.success(res);
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  @RequestMapping("/registerUser")
  public Result<Void> registerUser(UserRequestBody.RegisterUserReq registerUser) {
    logger.info("/registerUser");
    try {
      int i = userService.registerUser(registerUser);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  /** 更新用户权限 */
  @RequestMapping("/updateAuthority")
  public Result<Void> updateAuthority(UserRequestBody.UpdateAuthorityReq updateAuthorityReq) {
    logger.info("/updateAuthority");
    try {
      int i = userService.updateAuthority(updateAuthorityReq);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (UserNotFoundException e) {
      return Result.error(ResultEnum.USER_NOT_FOUND);
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 更新用户禁用状态 */
  @RequestMapping("/updateDisabledState")
  public Result<Void> updateDisabledState(UserRequestBody.UpdateDisabledReq updateDisabledReq) {
    logger.info("/updateDisabledState");
    try {
      int i = userService.updateDisabledState(updateDisabledReq);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (UserNotFoundException e) {
      return Result.error(ResultEnum.USER_NOT_FOUND);
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 更新用户禁用状态 */
  @RequestMapping("/deleteUser")
  public Result<Void> deleteUser(String userId) {
    logger.info("/deleteUser");
    try {
      int i = userService.deleteUser(userId);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 验证原密码是否正确 */
  @RequestMapping("/verifyPassword")
  public Result<Void> deleteUser(UserRequestBody.VerifyPasswordReq verifyPasswordReq) {
    logger.info("/deleteUser");
    try {
      boolean i = userService.verifyPassword(verifyPasswordReq);
      if (i) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (UserNotFoundException e) {
      return Result.error(ResultEnum.USER_NOT_FOUND);
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 修改密码 */
  @RequestMapping("/changePassword")
  public Result<Void> deleteUser(UserRequestBody.ChangePasswordReq changePasswordReq) {
    logger.info("/changePassword");
    try {
      int i = userService.changePassword(changePasswordReq);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (UserNotFoundException e) {
      return Result.error(ResultEnum.USER_NOT_FOUND);
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  /** 获取用户列表分页 */
  @RequestMapping("/getUserList")
  public Result<?> getUserList(UserRequestBody.GetUserListReq getUserListReq) {
    logger.info("getUserList");
    try {
      UserResponseBody.GetUserListRes userList = userService.getUserList(getUserListReq);
      HashMap<String, Object> res = new HashMap<>();
      res.put("total", userList.getTotal());
      res.put("userList", userList.getUserList());
      return Result.success(res);
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 搜索用户 */
  @RequestMapping("/searchUser")
  public Result<?> searchUser(String account) {
    logger.info("searchUser");
    try {
      List<User> users = userService.searchUser(account);
      HashMap<String, Object> res = new HashMap<>();
      res.put("users", users);
      return Result.success(res);
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

}
