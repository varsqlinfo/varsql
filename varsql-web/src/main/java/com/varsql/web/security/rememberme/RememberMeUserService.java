package com.varsql.web.security.rememberme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.varsql.web.security.AuthService;
import com.varsql.web.security.User;
import com.varsql.web.security.UserService;


/**
 *
 * @FileName : UserService.java
 * @Author   : ytkim
 * @Program desc :
 * @Hisotry :
 */
public class RememberMeUserService extends UserService {

	@Autowired
	private AuthService authService;

	@Override
	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		return authService.loadUserByUsername(username,true);
	}
}
