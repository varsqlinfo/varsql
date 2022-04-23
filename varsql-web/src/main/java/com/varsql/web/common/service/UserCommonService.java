package com.varsql.web.common.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.beans.MailInfo;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.core.configuration.Configuration;
import com.varsql.web.constants.MailType;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.model.entity.user.EmailTokenEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.user.EmailTokenEntityRepository;
import com.varsql.web.repository.user.UserInfoRepository;
import com.varsql.web.repository.user.UserMgmtRepository;
import com.varsql.web.util.ConvertUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.crypto.password.PasswordUtil;
import com.vartech.common.utils.VartechUtils;

@Service
public class UserCommonService {
	private final Logger logger = LoggerFactory.getLogger(UserCommonService.class);
	
	private EmailTokenEntityRepository emailTokenEntityRepository;
	
	private UserInfoRepository userInfoRepository;
	
	private MailService mailService; 
	
	public UserCommonService(EmailTokenEntityRepository emailTokenEntityRepository, UserInfoRepository userInfoRepository, MailService mailService) {
		this.emailTokenEntityRepository = emailTokenEntityRepository; 
		this.userInfoRepository = userInfoRepository; 
		this.mailService = mailService; 
	}
	

	public ResponseResult sendPasswordMail(String uid, String uemail) {
		logger.debug("resetPassword uid :{}, uemail:{} ", uid, uemail);
		
		UserEntity userInfo= userInfoRepository.findByUid(uid);
		
		if(!userInfo.getUemail().equals(uemail)) {
			return VarsqlUtils.getResponseResultItemOne(RequestResultCode.NOT_FOUND);
		}
		
		EmailTokenEntity tokenInfo = EmailTokenEntity.builder().token(VartechUtils.generateUUID()).viewid(userInfo.getViewid()).tokenType(MailType.RESET_PASSWORD.getType()).build();
		
		String passwordResetUrl = Configuration.getInstance().getSiteAddr()+"/resetPassword?token="+tokenInfo.getToken();
		
		logger.debug("password reset url : {}", passwordResetUrl);
		
		emailTokenEntityRepository.save(tokenInfo);
		
		mailService.sendMail(MailInfo.builder()
			.subject("Varsql password reset")
			.from(Configuration.getInstance().getMailConfigBean().getFromUser())
			.to(uemail)
			.content(passwordResetUrl)
			.build());
		
		return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	/**
	 * token 유효성 체크. 
	 * @param token
	 * @return
	 */
	public boolean isValidToken(String token) {
		
		EmailTokenEntity tokenInfo = emailTokenEntityRepository.findByToken(token);
		
		if(tokenInfo == null) return false; 
		
		int val = VarsqlDateUtils.dateDiff(ConvertUtils.localDateTimeToDate(tokenInfo.getRegDt()), new Date(), VarsqlDateUtils.DateCheckType.DAY);
		
		if(val < 1) {
			return true; 
		}
		
		return false;
	}

	
	/**
	 * password 초기화
	 * @param token
	 * @param upw
	 * @param confirmUpw
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult resetPassword(String token, String upw, String confirmUpw) {
		
		if(!upw.equals(confirmUpw)) {
			return VarsqlUtils.getResponseResultItemOne("password");
		}
		EmailTokenEntity tokenInfo = emailTokenEntityRepository.findByToken(token);
		
		if(tokenInfo == null) {
			return VarsqlUtils.getResponseResultItemOne("token");
		}
		
		int val = VarsqlDateUtils.dateDiff(ConvertUtils.localDateTimeToDate(tokenInfo.getRegDt()), new Date(), VarsqlDateUtils.DateCheckType.DAY);
		
		if(val >= 1) {
			return VarsqlUtils.getResponseResultItemOne("token");
		}
		
		UserEntity entity= userInfoRepository.findByViewid(tokenInfo.getViewid());
		
		entity.setUpw(upw);
		entity = userInfoRepository.save(entity);
		
		emailTokenEntityRepository.deleteTokenInfo(tokenInfo.getToken(), tokenInfo.getTokenType());
		
		return VarsqlUtils.getResponseResultItemOne("success");
	}

	
	
	
}
