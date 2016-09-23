package com.varsql.auth;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private AuthDAO authDao = new AuthDAO();

	
	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		return authDao.loadUserByUsername(username);
	}
}
