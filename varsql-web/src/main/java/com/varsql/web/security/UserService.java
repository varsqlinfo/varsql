package com.varsql.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.varsql.core.auth.User;


/**
 *
 * @FileName : UserService.java
 * @Author   : ytkim
 * @Program desc :
 * @Hisotry :
 */
@Service
public class UserService implements UserDetailsService {

	@Autowired
	private AuthDAO authDao;

	@Override
	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		return authDao.loadUserByUsername(username);

	}

	public User loadUserByUsername(String username, String password) {
		return authDao.loadUserByUsername(username, password, false);
	}
	
	public User loadUserByUsername(String username, String password, boolean ssoFlag) {
		return authDao.loadUserByUsername(username, password, ssoFlag);
	}
	
	public boolean passwordCheck(String username, String password) {
		return authDao.passwordCheck(username, password);
	}
}
