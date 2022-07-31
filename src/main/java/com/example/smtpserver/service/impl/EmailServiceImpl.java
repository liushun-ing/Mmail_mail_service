package com.example.smtpserver.service.impl;

import com.example.smtpserver.entity.Email;
import com.example.smtpserver.mapper.EmailMapper;
import com.example.smtpserver.service.IEmailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
@Service
public class EmailServiceImpl extends ServiceImpl<EmailMapper, Email> implements IEmailService {

}
