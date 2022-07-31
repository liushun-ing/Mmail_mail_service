package com.example.smtpserver.controller;


import com.example.smtpserver.entity.FilterAccount;
import com.example.smtpserver.pojo.FilterAccountDTO;
import com.example.smtpserver.service.IFilterAccountService;
import com.example.smtpserver.utils.Result;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
@RestController
@CrossOrigin
@RequestMapping("/filterAccount")
public class FilterAccountController {

  @Resource
  IFilterAccountService filterAccountService;

  Logger logger = Logger.getLogger(FilterAccountController.class);

  @RequestMapping("/addFilterAccount")
  public Result<Void> addFilterAccount(String account) {
    logger.info("addFilterAccount");
    try {
      int i = filterAccountService.addFilterAccount(account);
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

  @RequestMapping("/deleteFilterAccount")
  public Result<Void> deleteFilterAccount(String account) {
    logger.info("deleteFilterAccount");
    try {
      int i = filterAccountService.deleteFilterAccount(account);
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

  @RequestMapping("/getFilterAccountList")
  public Result<?> getFilterAccountList() {
    logger.info("getFilterAccountList");
    try {
      List<FilterAccountDTO> filterAccountList = filterAccountService.getFilterAccountList();
      HashMap<String, Object> res = new HashMap<>();
      res.put("total", filterAccountList.size());
      res.put("filterAccountList", filterAccountList);
      return Result.success(res);
    } catch (Exception e) {
      return Result.error();
    }
  }

}

