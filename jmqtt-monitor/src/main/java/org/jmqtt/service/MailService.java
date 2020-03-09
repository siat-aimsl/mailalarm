package org.jmqtt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
public class MailService {

    @Value("${mailHost}")
    private String host;
    @Value("${mailPort}")
    private int port;
    @Value("${mailUsername}")
    private String userName;
    @Value("${mailPassword}")
    private String passWord;
    @Value("${mailFrom}")
    private String emailFrom;
    @Value("${mailTimeout}")
    private int timeOut;

   /*private String host = "smtp.163.com";
     private Integer port = 465;
    private String userName = "t1137465078@163.com";
    private String emailFrom = "t1137465078@163.com";
    private String timeOut = "25000";
    private String passWord = "tu13317100";*/

    private  JavaMailSenderImpl mailSender = createMailSender();

    private  JavaMailSenderImpl createMailSender() {

        System.out.println("host:"+host);
        System.out.println("port:"+port);
        System.out.println("userName:"+userName);
        System.out.println("passWord:"+passWord);
        System.out.println("emailFrom:"+emailFrom);
        System.out.println("timeOut:"+timeOut);
        int i = 10000;
        while(i > 0){
            i--;
        }
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(userName);
        sender.setPassword(passWord);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", timeOut+"");
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sender.setJavaMailProperties(p);
        return sender;
    }

    public void sendMail(String to, String subject, String comment)  {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(comment);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
