package com.varsql.web.configuration;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.varsql.web.security.User;

/**
 * -----------------------------------------------------------------------------
* @fileName		: JPAAuditorAware.java
* @desc		: jpa audit
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class JPAAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null == authentication || !authentication.isAuthenticated()) {
			return Optional.empty();
		}
		
		if(authentication.getPrincipal() instanceof User) {
			return Optional.of(((User) authentication.getPrincipal()).getViewid());
		}else {
			return Optional.of(authentication.getPrincipal().toString());
		}
		
	}
}
