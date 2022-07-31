package com.example.smtpserver.service;

import com.example.smtpserver.entity.EmailBox;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
public interface IEmailBoxService extends IService<EmailBox> {

  int getTotalEmailNum();
}
