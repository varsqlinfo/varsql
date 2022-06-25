package com.varsql.web.common.service;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.web.constants.ResourceConfigConstants;
import com.vartech.common.app.beans.MailInfo;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.StringUtils;

@Service
public class MailService {
	private final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	private JavaMailSender mailSender;
	
	public MailService(@Qualifier(ResourceConfigConstants.MAIL_SERVICE) JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public ResponseResult sendMail(MailInfo mailInfo) {
		
		final MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
            	
                final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StringUtils.isBlank(mailInfo.getCharset())?VarsqlConstants.CHAR_SET : mailInfo.getCharset());
                
                helper.setFrom(mailInfo.getFrom()); 
                helper.setTo(mailInfo.getTo());
                helper.setSubject(mailInfo.getSubject()); // mail title
                helper.setText(mailInfo.getContent(), true); // mail content
            }
        };

		mailSender.send(preparator);
		
		return null; 
	}
	
}
