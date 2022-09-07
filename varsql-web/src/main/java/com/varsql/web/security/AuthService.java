package com.varsql.web.security;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.varsql.core.auth.Authority;
import com.varsql.core.auth.AuthorityType;
import com.varsql.core.auth.User;
import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.UUIDUtil;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.user.UserDBMappingInfoEntityRepository;
import com.varsql.web.security.repository.UserAuthRepository;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.utils.VartechUtils;

/**
 * 로그인 사용자 체크.
 * @FileName : AuthDAO.java
 * @Author   : ytkim
 * @Program desc : 인증 dao
 * @Hisotry :
 */
@Service(value = "authService")
public final class AuthService {

	private final Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private UserAuthRepository userAuthRepository;
	
	@Autowired
	private UserDBMappingInfoEntityRepository userDBMappingInfoEntityRepository;
	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	@Qualifier(ResourceConfigConstants.APP_PASSWORD_ENCODER)
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 *
	 * @Method Name  : loadUserByUsername
	 * @Method 설명 : 사용자 로그인 정보 추출.
	 * @작성일   : 2015. 6. 22.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	public User loadUserByUsername(final String userInfoJson) throws UsernameNotFoundException {
		DataMap userInfo = VartechUtils.jsonStringToObject(userInfoJson);
		String username = userInfo.getString("username");
		String password = userInfo.getString("password");

		return loadUserByUsername(username, password, false);
	}

	public User loadUserByUsername(String username, boolean remembermeFlag) {
		return loadUserByUsername(username, null, remembermeFlag);
	}

	public User loadUserByUsername(String username, String password, boolean remembermeFlag) {
		try {
			UserEntity userModel= userAuthRepository.findByUid(username);

			if(userModel==null){
				return null;
				//throw new UsernameNotFoundException("Wrong username or password ");
			}

			if(remembermeFlag==false) {
				if(!passwordEncoder.matches(password, userModel.getUpw())){
					return null;
					//throw new UsernameNotFoundException("Wrong username or password ");
				}
			}

			User user = new User();
			user.setLoginRememberMe(remembermeFlag);
			user.setViewid(userModel.getViewid());
			user.setUsername(userModel.getUid());
			user.setPassword("");
			user.setFullname(userModel.getUname());

			if(userModel.isBlockYn()){ //차단된 사용자 체크.
				user.setBlockYn(true);
				return user;
			}

			user.setUserLocale(LocaleConstants.parseLocaleString(userModel.getLang()));
			user.setOrgNm(userModel.getOrgNm());
			user.setDeptNm(userModel.getDeptNm());
			user.setEmail(userModel.getUemail());
			user.setAcceptYn(userModel.isAcceptYn());

			String userRole = userModel.getUserRole();


			List<Authority> roles = new ArrayList<Authority>();
			Authority r = new Authority();

			AuthorityType authType = AuthorityType.valueOf(userRole);
			r = new Authority();
			r.setName(userRole);
			r.setPriority(authType.getPriority());
			roles.add(r);

			user.setTopAuthority(authType);
			user.setAuthorities(roles);

			return user;
		}catch(Exception e){
			logger.error(this.getClass().getName() , e);
			throw new UsernameNotFoundException(new StringBuilder().append("Wrong username or password :").append(username).append(" ").append(e.getMessage()).toString());
		}
	}

	/**
	 * 사용자 비밀번호 체크
	 *
	 * @method : passwordCheck
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean passwordCheck(String username, String password) {
		UserEntity userModel= userAuthRepository.findByUid(username);

		if(userModel==null){
			return false;
		}

		if(!passwordEncoder.matches(password, userModel.getUpw())){
			return false;
		}

		return true;
	}


}
