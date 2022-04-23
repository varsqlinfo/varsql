package com.varsql.web.configuration;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.varsql.core.configuration.beans.MailConfigBean;
import com.varsql.web.constants.ResourceConfigConstants;
import com.vartech.common.utils.VartechReflectionUtils;

public class MailConfigurer {
	
	private final Logger logger = LoggerFactory.getLogger(MailConfigurer.class);
    
    @Bean(ResourceConfigConstants.MAIL_SERVICE)
    public JavaMailSender getMailSender() {
        
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        MailConfigBean mailConfig = com.varsql.core.configuration.Configuration.getInstance().getMailConfigBean();
        
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());
        
        Properties javaMailProperties = new Properties();
        
        logger.info("mailConfig : {} ", VartechReflectionUtils.reflectionToString(mailConfig));
        
        javaMailProperties.put("mail.smtp.starttls.enable", mailConfig.getSmtpStarttlsEnable());
        javaMailProperties.put("mail.smtp.auth", mailConfig.getSmtpAuth());
        javaMailProperties.put("mail.transport.protocol", mailConfig.getTransportProtocol());
        javaMailProperties.put("mail.debug", mailConfig.getDebug());
 
        mailSender.setJavaMailProperties(javaMailProperties);
        
        return mailSender;
    }
}


