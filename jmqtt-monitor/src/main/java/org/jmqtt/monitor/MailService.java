package org.jmqtt.monitor;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {

    private static final String HOST = "smtp.qq.com";
    private static final Integer PORT = 465;
    private static final String USERNAME = "1137465078@qq.com";
    private static final String PASSWORD = "oekuiigzabgegjii";
    private static final String emailForm = "1137465078@qq.com";
    private static final String timeout = "25000";
    private static JavaMailSenderImpl mailSender = createMailSender();

    private static JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", timeout);
        p.setProperty("mail.smtp.auth", "false");
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sender.setJavaMailProperties(p);
        return sender;
    }


    public static void sendMail(String to, String subject, String comment)  {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(emailForm);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(comment);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /*
    @Autowired
    JavaMailSenderImpl mailSender;

    public void SimpleMailMessage() {

        //简单邮件
        SimpleMailMessage message = new SimpleMailMessage();

        //邮件标题
        message.setSubject("ITAEM团队招新了");

        //邮件内容
        message.setText("招新的方向：前端、后台、安卓、UI、AI、大数据");

        //发送者：必填
        message.setTo("123456789@qq.com");

        //接收者：必填
        message.setFrom("987654321@qq.com");


        mailSender.send(message);
    }

*/
}
