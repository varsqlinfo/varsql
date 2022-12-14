package com.varsql.web.common.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.auth.User;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.UUIDUtil;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.web.constants.MailType;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.model.entity.user.EmailTokenEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.user.EmailTokenEntityRepository;
import com.varsql.web.repository.user.UserDBMappingInfoEntityRepository;
import com.varsql.web.repository.user.UserInfoRepository;
import com.varsql.web.util.ConvertUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.MailInfo;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.VartechUtils;

@Service
public class UserCommonService {
	private final Logger logger = LoggerFactory.getLogger(UserCommonService.class);
	
	final private EmailTokenEntityRepository emailTokenEntityRepository;
	
	final private UserInfoRepository userInfoRepository;
	
	final private UserDBMappingInfoEntityRepository userDBMappingInfoEntityRepository;
	
	private MailService mailService; 
	
	public UserCommonService(EmailTokenEntityRepository emailTokenEntityRepository, UserInfoRepository userInfoRepository, MailService mailService,UserDBMappingInfoEntityRepository userDBMappingInfoEntityRepository) {
		this.emailTokenEntityRepository = emailTokenEntityRepository; 
		this.userInfoRepository = userInfoRepository; 
		this.mailService = mailService; 
		this.userDBMappingInfoEntityRepository = userDBMappingInfoEntityRepository; 
	}
	
	/**
	 * send password reset mail
	 *
	 * @method : sendPasswordMail
	 * @param uid
	 * @param uemail
	 * @return
	 */
	public ResponseResult sendPasswordMail(String uid, String uemail) {
		logger.debug("resetPassword uid :{}, uemail:{} ", uid, uemail);
		
		UserEntity userInfo= userInfoRepository.findByUid(uid);
		
		if(userInfo == null || !userInfo.getUemail().equals(uemail)) {
			return ResponseResult.builder().resultCode( RequestResultCode.NOT_FOUND).build();
		}
		
		EmailTokenEntity tokenInfo = EmailTokenEntity.builder().token(VartechUtils.generateUUID()).viewid(userInfo.getViewid()).tokenType(MailType.RESET_PASSWORD.getType()).build();
		
		String passwordResetUrl = Configuration.getInstance().getSiteAddr()+"/resetPassword?token="+tokenInfo.getToken();
		
		logger.debug("password reset url : {}", passwordResetUrl);
		
		emailTokenEntityRepository.save(tokenInfo);
		
		return mailService.sendMail(MailInfo.builder()
			.subject("Varsql password reset")
			.from(Configuration.getInstance().getMailConfigBean().getFromUser())
			.to(uemail)
			.content(passwordResetUrl)
			.build());
	}
	
	/**
	 * password reset token 유효성 체크. 
	 *
	 * @method : isValidToken
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
	 * password reset
	 *
	 * @method : resetPassword
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

	/**
	 * 사용자 권한 있는 데이터 베이스 list
	 *
	 * @method : databaseList
	 * @return
	 */
	public List<DatabaseInfo> databaseList(){
		return userDBMappingInfoEntityRepository.userDBInfo(SecurityUtil.loginInfo());
	}
	
	/**
	 *
	 * @Method Name  : getUserDataBaseInfo
	 * @Method 설명 :
	 * @작성일   : 2015. 6. 22.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 * @throws SQLException
	 */
	public Map<String, DatabaseInfo> reloadDatabaseList(){
		User user = SecurityUtil.loginInfo();
		List<DatabaseInfo> dbList = databaseList();
		
		String viewid = user.getViewid();
		
		Map<String, DatabaseInfo> userDatabaseInfo = new LinkedHashMap<String, DatabaseInfo>();
		Map<String, String> vconnidNconuid = new HashMap<>();
		
		for(DatabaseInfo dbInfo: dbList) {
			String vconnid = dbInfo.getVconnid();
			String uuid = UUIDUtil.vconnidUUID(viewid, vconnid);
			userDatabaseInfo.put(uuid, dbInfo);
			vconnidNconuid.put(vconnid, uuid);
		}

		user.setDatabaseInfo(userDatabaseInfo);
		user.setVconnidNconuid(vconnidNconuid);
		
		return userDatabaseInfo; 
	}
	
}
