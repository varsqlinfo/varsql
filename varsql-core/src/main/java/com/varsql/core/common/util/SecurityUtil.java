package com.varsql.core.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.auth.Authority;
import com.varsql.core.auth.User;
import com.varsql.core.common.constants.VarsqlKeyConstants;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;

public final class SecurityUtil {
	
	private SecurityUtil(){};

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
		
		if(auth instanceof AnonymousAuthenticationToken) {
			return User.ANONYMOUS_USERNAME;
		}

		User user = (User)auth.getPrincipal();

		java.util.Iterator<? extends GrantedAuthority> iter =user.getAuthorities().iterator();

		while(iter.hasNext()){
			return ((Authority)iter.next()).getName();
		}

		return "";
	}


	public static Collection<? extends GrantedAuthority> userAuthorities() {
		return loginInfo().getAuthorities();
	}

	public static int loginRolePriority() {
		java.util.Iterator<? extends GrantedAuthority> iter =loginInfo().getAuthorities().iterator();
		int priority = 0, maxVal =0;
		while(iter.hasNext()){
			maxVal = ((Authority)iter.next()).getPriority();
			priority= maxVal > priority ? maxVal :priority;
		}

		return priority;
	}

	public static String getVconnid(String conuid){
		return ((DatabaseInfo)loginInfo().getDatabaseInfo().get(conuid)).getVconnid();
	}

	/**
	 *
	 * @Method Name  : isAuthenticated
	 * @Method 설명 : login user check
	 * @작성일   : 2019. 1. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	public static boolean isAuthenticated(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth==null){
			return false;
		}else if(auth instanceof AnonymousAuthenticationToken){
			return false; 
		}else {
			return auth.getPrincipal() instanceof User;
		}
	}
	
	/**
	 *
	 * @Method Name  : isAnonymous
	 * @Method 설명 : 익명 사용자 체크. 
	 * @작성일   : 2020. 10. 29
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	public static boolean isAnonymous(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth==null){
			return true;
		}else if(auth instanceof AnonymousAuthenticationToken){
			return true; 
		}
		
		return false; 
	}

	/**
	 *
	 * @Method Name  : hasRole
	 * @Method 설명 : role이  있는지 여부.
	 * @작성일   : 2017. 12. 19.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param roleName
	 * @return
	 */
	public static boolean hasRole(String roleName){
		java.util.Iterator<? extends GrantedAuthority> iter =loginInfo().getAuthorities().iterator();
		while(iter.hasNext()){
			if(roleName.equals(((Authority)iter.next()).getName())){
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @Method Name  : isAdmin
	 * @Method 설명 : 관리자 여부.
	 * @작성일   : 2017. 12. 19.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	public static boolean isAdmin(){
		java.util.Iterator<? extends GrantedAuthority> iter =loginInfo().getAuthorities().iterator();
		while(iter.hasNext()){
			if(AuthorityType.ADMIN.name().equals(((Authority)iter.next()).getName())){
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @Method Name  : isManager
	 * @Method 설명 : 매니저 여부.
	 * @작성일   : 2019. 8. 21.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	public static boolean isManager(){
		java.util.Iterator<? extends GrantedAuthority> iter =loginInfo().getAuthorities().iterator();
		while(iter.hasNext()){
			if(AuthorityType.MANAGER.name().equals(((Authority)iter.next()).getName())){
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @Method Name  : setUserInfo
	 * @Method 설명 : user role Info
	 * @작성일   : 2018. 1. 22.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param userInfo
	 */
	public static void setUserInfo(Map userInfo){
		userInfo.put(VarsqlKeyConstants.PARAM_UID,userViewId());
		userInfo.put(VarsqlKeyConstants.PARAM_ROLE,getUserRoleInfo());

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
	public static String userViewId() {
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		return userViewId(auth);
	}
	public static String userViewId(HttpServletRequest req) {
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		return userViewId(auth);
	}
	public static String userViewId(Authentication auth) {
		if(auth == null ) return null;
		
		if(auth instanceof AnonymousAuthenticationToken) {
			return User.ANONYMOUS_USERNAME;
		}

		if(auth.getPrincipal() instanceof User){
			return ((User)auth.getPrincipal()).getViewid();
		}else{
			return auth.getName();
		}
	}

	/**
	 *
	 * @Method Name  : loginName
	 * @Method 설명 : 로그인 사용자 이름 가져오기
	 * @작성일   : 2015. 3. 18.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	public static String loginName() {
		return loginName(SecurityContextHolder.getContext().getAuthentication());
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
		
		if(auth instanceof AnonymousAuthenticationToken) {
			return User.ANONYMOUS_USERNAME;
		}

		if(auth.getPrincipal() instanceof User){
			return ((User)auth.getPrincipal()).getUsername();
		}else{
			return auth.getName();
		}
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
	public static User loginInfo() {
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		return loginInfo(auth);
	}

	/**
	 *
	 * @Method Name  : loginInfo
	 * @Method 설명 : login user info
	 * @작성일   : 2018. 4. 6.
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
		if(auth ==null) {
			return null; 
		}
		
		if(auth instanceof AnonymousAuthenticationToken) {
			return new User.AnonymousUser().build();
		}
		return (User)auth.getPrincipal();
	}

	/**
	 *
	 * @Method Name  : userDBInfo
	 * @Method 설명 : user database info
	 * @작성일   : 2018. 4. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connid
	 * @return
	 */
	public static DatabaseInfo userDBInfo (String conuid){
		return loginInfo().getDatabaseInfo().get(conuid);
	}

	/**
	 *
	 * @Method Name  : getUserRoleInfo
	 * @Method 설명 : 사용자 role 정보.
	 * @작성일   : 2018. 4. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	public static List<String> getUserRoleInfo() {

		Collection<? extends GrantedAuthority> userRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		List<String> userRoleInfo = new ArrayList<String>();
		userRoleInfo.add(userViewId());

		if(userRole!=null){
			Iterator<? extends GrantedAuthority> iter= userRole.iterator();

			while(iter.hasNext()){
				userRoleInfo.add(iter.next().getAuthority());
			}
		}
		return userRoleInfo;
	}

	public static boolean isSchemaView(DatabaseParamInfo dataParamInfo) {
		return dataParamInfo.isSchemaViewYn() || SecurityUtil.loginRolePriority() >= AuthorityType.MANAGER.getPriority();
	}

}
