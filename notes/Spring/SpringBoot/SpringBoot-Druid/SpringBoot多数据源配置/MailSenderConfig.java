package net.shopin.oms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
/**
 * <p>ClassName:  </p>
 * <p>Description: 邮件信息配置类 </p>
 * <p>Company: http://www.shopin.net</p>
 * @author zhangyong@shopin.cn
 * @version 1.0.0
 * @date 2018/7/17 10:45
 */
@Configuration
public class MailSenderConfig {

    @Value("${email.host}")
    private String emailHost;

    @Value("${email.username}")
    private String emailUsername;

    @Value("${email.password}")
    private String emailPassword;

    /**
     * @param
     * @auther zhangyong@shopin.cn
     * @desc 特定名称Bean注入IOC
     * @date 2018/7/17  17:55
     * @from JDK 1.8
     */
    @Bean("mailSender")
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        //step [1] :设置HOST;
        javaMailSender.setHost(emailHost);
        //step [2] :设置Properties;
        Properties properties = javaMailSender.getJavaMailProperties();
        properties.setProperty("mail.smtp.auth", true + "");
        properties.setProperty("mail.smtp.timeout", 25000 + "");
        javaMailSender.setJavaMailProperties(properties);
        //step [3] :设置username;
        javaMailSender.setUsername(emailUsername);
        //step [4] :设置password;
        javaMailSender.setPassword(emailPassword);
        return javaMailSender;
    }
}
