package com.varsql.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.core.auth.User;
import com.varsql.core.common.beans.ClientInfo;
import com.varsql.web.model.entity.user.UserLogHistEntity;
import com.varsql.web.security.repository.UserLogHistRepository;
import com.varsql.web.util.DefaultValueUtils;

/**
 * security log
 * @FileName : SecurityLogDAO.java
 * @Author   : ytkim
 * @Program desc : 인증 dao
 * @Hisotry :
 */
@Service(value = "securityLogDAO")
public final class SecurityLogDAO {

	private final Logger logger = LoggerFactory.getLogger(SecurityLogDAO.class);

	@Autowired
	private UserLogHistRepository userLogHistRepository;

	/**
	 *
	 * @Method Name  : addLog
	 * @Method 설명 : add 로그인 & 로그아웃 로그.
	 * @작성일   : 2019. 9. 20.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @throws Exception
	 */
	public void addLog(User user, String type, ClientInfo cpi) {
		try {
			userLogHistRepository.save(UserLogHistEntity.builder()
					.viewid(user.getViewid())
					.histType(type)
					.usrIp(cpi.getIp())
					.browser(cpi.getBrowser())
					.deviceType(cpi.getDeviceType())
					.histTime(DefaultValueUtils.currentTimestamp())
					.platform(cpi.getOsType()).build());
		} catch (Exception e) {
			logger.error(this.getClass().getName() +" addLog ", e);
		}

	}
}
