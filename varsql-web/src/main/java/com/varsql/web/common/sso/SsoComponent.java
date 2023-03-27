package com.varsql.web.common.sso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.security.SsoAuthToken;
import com.varsql.web.security.VarsqlAuthenticationProvider;
import com.vartech.common.utils.StringUtils;

@Component(ResourceConfigConstants.APP_SSO_COMPONENT)
public class SsoComponent {

	private static final Logger logger = LoggerFactory.getLogger(SsoComponent.class);

	@Autowired
	private VarsqlAuthenticationProvider varsqlAuthenticationProvider;


	public Authentication ssoLogin(String ssoId) {
		if(!StringUtils.isBlank(ssoId)) {
    		try {
    			Authentication currentAuth = SecurityUtil.getAuthentication();

    			if(currentAuth== null || !ssoId.equals(currentAuth.getName())) {
	    			Authentication auth = varsqlAuthenticationProvider.authenticate(new SsoAuthToken(ssoId));
	    			SecurityContextHolder.getContext().setAuthentication(auth);

	    			return auth;
    			}else {
    				return currentAuth;
    			}
    		}catch(Exception e) {
    			logger.error(e.getMessage());
    			return null;
    		}
    	}

    	return null;
	}
}