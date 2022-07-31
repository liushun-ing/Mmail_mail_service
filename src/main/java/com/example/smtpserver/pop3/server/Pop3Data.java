package com.example.smtpserver.pop3.server;

import com.example.smtpserver.entity.EmailBox;
import com.example.smtpserver.entity.User;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

/** smtp连接的数据保存处理类 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pop3Data {
  // 通道
  private Channel channel;
  // 用户
  private User user;
  // 状态
  /** 1 初始状态 等待输入账号， 2 输入了账号，等待输入密码 3 登陆成功 */
  private int state;

  // 标记删除邮件列表，里面是邮件的序号
  private HashSet<Integer> deleteList;

  // 设置channel和指令序号
  public Pop3Data(Channel channel) {
    this.channel = channel;
    this.state = 1;
    this.user = new User();
    this.deleteList = new HashSet<>();
  }

  // 提供回写函数，服务器回复客户端
  public void reply(String replyMsg) {
    this.channel.writeAndFlush(replyMsg);
  }
}
