package com.example.smtpserver.smtp.server;

import com.example.smtpserver.entity.EmailBox;
import com.example.smtpserver.entity.User;
import com.example.smtpserver.utils.ServerConfig;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;

/** smtp连接的数据保存处理类 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmtpData {
  // 通道
  private Channel channel;
  // 前一个处理的指令，防止指令顺序不对
  private int lastCommandOrder;
  // 用户
  private User user;
  // 邮件
  private EmailBox emailBox;
  // 收件人列表，因为可能需要发给很多人，使用set自动去重
  private HashSet<String> rcptList;
  // 状态
  /** 0 未开始登陆 1 初始化登陆状态，等待输入账号，执行了auth 2 输入了账号，等待输入密码 3 登陆成功 4 登陆失败 */
  private int state;
  // 是否等待接收邮件主体输入
  private int isWaitingData;

  // 设置channel和指令序号
  public SmtpData(Channel channel) {
    this.channel = channel;
    this.lastCommandOrder = 0;
    this.isWaitingData = 0;
    rcptList = new HashSet<>();
  }

  // 提供回写函数，服务器回复客户端
  public void reply(String replyMsg) {
    this.channel.writeAndFlush(replyMsg);
  }
}
