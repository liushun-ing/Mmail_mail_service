package com.example.smtpserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpserver.entity.EmailBox;
import com.example.smtpserver.mapper.EmailBoxMapper;
import com.example.smtpserver.mapper.EmailMapper;
import com.example.smtpserver.service.IEmailBoxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
@Service
public class EmailBoxServiceImpl extends ServiceImpl<EmailBoxMapper, EmailBox> implements IEmailBoxService {

  @Resource
  EmailBoxMapper emailBoxMapper;

  @Override
  public int getTotalEmailNum() {
    Long aLong = emailBoxMapper.selectCount(new LambdaQueryWrapper<EmailBox>());
    return aLong.intValue();
  }
}
