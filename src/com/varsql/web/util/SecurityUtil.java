package com.varsql.web.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.varsql.auth.Role;
import com.varsql.auth.User;

public class SecurityUtil {
	
	/**
	 * 
	 * @Method Name  : loginRole
	 * @Method 설명 : 로그인 사용자 role 가져오기
	 * @작성일   : 2015. 3. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param auth
	 * @return
	 */
	public static String loginRole(HttpServletRequest req) {
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		return loginRole(auth);
	}
	public static String loginRole(Authentication auth) {
		if(auth == null ) return null;
		
		User user = (User)auth.getPrincipal();
		
		java.util.Iterator<? extends GrantedAuthority> iter =user.getAuthorities().iterator();
		
		while(iter.hasNext()){
			return ((Role)iter.next()).getName();
		}
		
		return ""; 
	}
	
	/**
	 * 
	 * @Method Name  : loginId
	 * @Method 설명 : 로그인 id 가져오기
	 * @작성일   : 2015. 3. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param auth
	 * @return
	 */
	public static String loginId(HttpServletRequest req) {
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		return loginId(auth);
	}
	public static String loginId(Authentication auth) {
		if(auth == null ) return null;
		
		User user = (User)auth.getPrincipal();
		
		return user.getUid();
	}
	
	/**
	 * 
	 * @Method Name  : loginName
	 * @Method 설명 : 로그인 사용자 이름 가져오기
	 * @작성일   : 2015. 3. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param auth
	 * @return
	 */
	public static String loginName(Authentication auth) {
		if(auth == null ) return null;
		
		User user = (User)auth.getPrincipal();
		
		return user.getUsername();
	}
	
	/**
	 * 
	 * @Method Name  : loginInfo
	 * @Method 설명 : 사용자 로그인 정보.
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param req
	 * @return
	 */
	public static User loginInfo(HttpServletRequest req) {
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		return loginInfo(auth);
	}
	public static User loginInfo(Authentication auth) {
		return (User)auth.getPrincipal();
	}
}
