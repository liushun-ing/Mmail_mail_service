package com.example.smtpserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "token")
// 从配置文件中拿
public class TokenConfig {
  private long expireDate;
  private String secret;
}

