package com.varsql.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.varsql.web.constants.ResourceConfigConstants;


/**
 *
 * @FileName : UserService.java
 * @Author   : ytkim
 * @Program desc :
 * @Hisotry :
 */
@Service(ResourceConfigConstants.USER_DETAIL_SERVICE)
public class UserService implements UserDetailsService {

	@Autowired
	private AuthService authService;

	@Override
	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		return authService.loadUserByUsername(username);
	}

	public User loadUserByUsername(String username, String password) {
		return authService.loadUserByUsername(username, password, false);
	}
	
	public User loadUserByUsername(String username, String password, boolean ssoFlag) {
		return authService.loadUserByUsername(username, password, ssoFlag);
	}
	
	public boolean passwordCheck(String username, String password) {
		return authService.passwordCheck(username, password);
	}
}
