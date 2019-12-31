package com.varsql.core.auth;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * 
 * @FileName : UserService.java
 * @Author   : ytkim
 * @Program desc :
 * @Hisotry :
 */
@Service
public class UserService implements UserDetailsService {

	private AuthDAO authDao = new AuthDAO();

	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		return authDao.loadUserByUsername(username);
		
	}

	public User loadUserByUsername(String username, String password) {
		return authDao.loadUserByUsername(username, password);
	}
}
