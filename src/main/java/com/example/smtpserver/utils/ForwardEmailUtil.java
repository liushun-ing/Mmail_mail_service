package com.example.smtpserver.utils;

import com.example.smtpserver.entity.EmailBox;
import com.sun.mail.smtp.SMTPTransport;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Properties;

/**
 * 转发邮件给其他邮件服务器的工具类
 */
public class ForwardEmailUtil {

  public static void sendEmail(EmailBox emailBox) throws Exception {
    Properties prop = System.getProperties();
    // 发送服务器需要身份验证
//    prop.setProperty("mail.smtp.auth", "true");
// 设置邮件服务器主机名
//    prop.setProperty("mail.host", "duilianyun.com");
    prop.setProperty("mail.smtp.host", "smtp.duilianyun.com");
// 发送邮件协议名称
//    prop.setProperty("mail.transport.protocol", "smtp");
    Session session = Session.getInstance(prop, null);
    // 开启可以查看发送的指令以及服务器回复的内容
    session.setDebug(true);

    MimeMessage mimeMessage = new MimeMessage(session);

    mimeMessage.setSubject("主题");

    String nickName = "张三";
    InternetAddress sender = new InternetAddress(MimeUtility.encodeText(nickName) + " <zhangsan@duilianyun.com>");
    mimeMessage.setSender(sender);
    mimeMessage.setFrom(sender);

    mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("1743760069@qq.com"));
    mimeMessage.setReplyTo(new Address[] { sender });
    mimeMessage.setText("内容");

    //查询域名对应的邮箱服务器地址
//    Lookup lookup = new Lookup("smtp.qq.com", Type.A);
//    lookup.run();
//    if (lookup.getResult() != Lookup.SUCCESSFUL) {
//      System.out.println("ERROR: " + lookup.getErrorString());
//      return;
//    }
//    Record[] answers = lookup.getAnswers();

//    String dns = null;
//    for (Record record : answers) {
//      dns = record.getAdditionalName().toString();
//    }

//    if (null != dns) {
//      if (dns.endsWith(".")) {
//        dns = dns.substring(0, dns.length() - 1);
//      }
      SMTPTransport transport = (SMTPTransport) session.getTransport(new URLName("smtp", "157.148.54.34", 25, null, null, null));
      transport.connect();
      transport.sendMessage(mimeMessage, new Address[] { new InternetAddress("1743760069@qq.com") });
//    }
  }

  public static void main(String[] args) throws Exception {
    sendEmail(new EmailBox());
  }
}
