package com.example.smtpserver.service;

import com.example.smtpserver.entity.FilterAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.smtpserver.pojo.FilterAccountDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
public interface IFilterAccountService extends IService<FilterAccount> {

  int addFilterAccount(String account);

  int deleteFilterAccount(String account);

  List<FilterAccountDTO> getFilterAccountList();
}
