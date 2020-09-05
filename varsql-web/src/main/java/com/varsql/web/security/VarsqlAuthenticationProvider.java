package com.varsql.web.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.varsql.core.auth.User;
import com.varsql.core.exception.BlockingUserException;

@Component
public class VarsqlAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		Map<String, String> userInfo = new HashMap<String,String>();
		
		userInfo.put("username", username);
		userInfo.put("password", password);
		
		User user = userService.loadUserByUsername(username , password);
		
		if (user == null) {
			throw new BadCredentialsException("Username not match.");
		}
		
		if(user.isBlockYn()){
			throw new BlockingUserException("block user");
		}

		return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
	}

	public boolean supports(Class<?> arg0) {
		return true;
	}

}
