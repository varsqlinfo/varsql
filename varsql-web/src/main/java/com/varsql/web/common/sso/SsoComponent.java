package com.varsql.web.common.sso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.security.SsoAuthToken;
import com.varsql.web.security.VarsqlAuthenticationProvider;
import com.varsql.web.util.SecurityUtil;
import com.vartech.common.utils.StringUtils;

@Component(ResourceConfigConstants.APP_SSO_COMPONENT)
public class SsoComponent {

	private static final Logger logger = LoggerFactory.getLogger(SsoComponent.class);

	@Autowired
	private VarsqlAuthenticationProvider varsqlAuthenticationProvider;


	public Authentication ssoLogin(String ssoId) {
		
		Authentication currentAuth = SecurityUtil.getAuthentication();
		
		if(StringUtils.isBlank(ssoId)) {
			if(currentAuth != null && !(currentAuth instanceof AnonymousAuthenticationToken)) {
				return currentAuth;
			}
			
			return null; 
		}
		
		try {
			if(currentAuth== null || !ssoId.equals(currentAuth.getName())) {
    			return varsqlAuthenticationProvider.authenticate(new SsoAuthToken(ssoId));
			}else {
				return currentAuth;
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
}