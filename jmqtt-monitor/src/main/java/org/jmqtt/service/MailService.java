package org.jmqtt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class MailService {


    @Value("${mailFrom}")
    private String emailFrom;
   /*private String host = "smtp.163.com";
     private Integer port = 465;
    private String userName = "t1137465078@163.com";
    private String emailFrom = "t1137465078@163.com";
    private String timeOut = "25000";
    private String passWord = "tu13317100";*/
    @Autowired
    private  JavaMailSenderImpl mailSender;


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
