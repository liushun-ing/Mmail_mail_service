package com.example.smtpserver.mapper;

import com.example.smtpserver.entity.EmailBox;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.smtpserver.pojo.StatEmailBox;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
public interface EmailBoxMapper extends BaseMapper<EmailBox> {

  StatEmailBox getStatEmailBox(String account);

}
