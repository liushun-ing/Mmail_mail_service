package com.example.smtpserver.pop3.command;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpserver.entity.EmailBox;
import com.example.smtpserver.exception.ArgsErrorException;
import com.example.smtpserver.mapper.EmailBoxMapper;
import com.example.smtpserver.pop3.server.Pop3Data;
import com.example.smtpserver.utils.ReplyBuilder;
import com.example.smtpserver.utils.ApplicationContextHolder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

public class QuitCommand extends BaseCommand {

  EmailBoxMapper emailBoxMapper;

  public QuitCommand() {
    super("quit");
    emailBoxMapper = ApplicationContextHolder.getBean("emailBoxMapper", EmailBoxMapper.class);
  }

  @Override
  @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {
    String[] parsedArgs = getParsedArgs(commandString);
    if (parsedArgs.length != 1) {
      pop3Data.reply(ReplyBuilder.buildQuitErrReply());
      throw new ArgsErrorException();
    }
    try {
      List<EmailBox> emailBoxList =
          emailBoxMapper.selectList(
              new LambdaQueryWrapper<EmailBox>()
                  .eq(EmailBox::getToEmail, pop3Data.getUser().getAccount()));
      // 需要将已标记的邮件删除
      if (pop3Data.getDeleteList().size() != 0) {
        for (int index : pop3Data.getDeleteList()) {
          // 序号在加进入的时候已经保证合法了，可以直接删除
          emailBoxMapper.deleteById(emailBoxList.get(index - 1).getId());
        }
      }
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      throw new RuntimeException(e.getMessage());
    }
    // 在回复完消息之后，将通道关闭
    ChannelFuture future = pop3Data.getChannel().writeAndFlush(ReplyBuilder.buildQuitOkReply());
    // 添加异步回调事件，在回调队列中等待执行
    future.addListener(ChannelFutureListener.CLOSE);
  }
}
