package com.example.smtpserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpserver.entity.FilterAccount;
import com.example.smtpserver.entity.User;
import com.example.smtpserver.exception.UserNotFoundException;
import com.example.smtpserver.mapper.FilterAccountMapper;
import com.example.smtpserver.mapper.UserMapper;
import com.example.smtpserver.pojo.FilterAccountDTO;
import com.example.smtpserver.service.IFilterAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现类
 *
 * @author ls
 * @since 2022-04-30
 */
@Service
public class FilterAccountServiceImpl extends ServiceImpl<FilterAccountMapper, FilterAccount>
    implements IFilterAccountService {

  @Resource FilterAccountMapper filterAccountMapper;

  @Resource UserMapper userMapper;

  @Override
  public int addFilterAccount(String account) {
    if (account == null || "".equals(account)) {
      throw new RuntimeException("参数为空");
    }
    FilterAccount filterAccount =
        filterAccountMapper.selectOne(
            new LambdaQueryWrapper<FilterAccount>().eq(FilterAccount::getAccount, account));
    if (filterAccount != null) {
      throw new RuntimeException("该账号已经在过滤名单中");
    }
    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, account));
    if (user == null) {
      throw new RuntimeException("该账号系统中不存在");
    }
    return filterAccountMapper.insert(FilterAccount.builder().account(account).build());
  }

  @Override
  public int deleteFilterAccount(String account) {
    if (account == null || "".equals(account)) {
      throw new RuntimeException("参数为空");
    }
    FilterAccount filterAccount =
        filterAccountMapper.selectOne(
            new LambdaQueryWrapper<FilterAccount>().eq(FilterAccount::getAccount, account));
    if (filterAccount == null) {
      throw new RuntimeException("该账号不存在");
    }
    return filterAccountMapper.deleteById(filterAccount.getId());
  }

  @Override
  public List<FilterAccountDTO> getFilterAccountList() {
    List<FilterAccount> filterAccounts =
        filterAccountMapper.selectList(new LambdaQueryWrapper<FilterAccount>());
    ArrayList<FilterAccountDTO> filterAccountDTOS = new ArrayList<>();
    for (FilterAccount filterAccount : filterAccounts) {
      User user =
          userMapper.selectOne(
              new LambdaQueryWrapper<User>().eq(User::getAccount, filterAccount.getAccount()));
      filterAccountDTOS.add(
          FilterAccountDTO.builder()
              .account(user.getAccount())
              .authority(user.getAuthority())
              .avatar(user.getAvatar())
              .nickname(user.getNickname())
              .phone(user.getPhone())
              .build());
    }
    return filterAccountDTOS;
  }
}
