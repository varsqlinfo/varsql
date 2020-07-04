package com.varsql.web.security;

import org.springframework.beans.factory.annotation.Autowired;
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
@Service("rememberMeUserService")
public class RememberMeUserService extends UserService {

	@Autowired
	private AuthDAO authDao;

	@Override
	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		return authDao.loadUserByUsername(username,true);

	}
}
