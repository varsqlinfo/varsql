package com.varsql.core.sso;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*
* @FileName  : SsoHandler.java
* @프로그램 설명 : sso hanlder
* @Date      : 2019. 11. 26.
* @작성자      : ytkim
* @변경이력 :
*/
public interface SsoHandler {
	
	/**
	 * login page ulr
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String loginURL(HttpServletRequest request, HttpServletResponse response);
		
	/**
	 * sso 파라미터 확인
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean beforeSso(HttpServletRequest request, HttpServletResponse response);

	/**
	 * user id 추출
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String extractUserId(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 추가 사용자 정보 처리
	 * 
	 * @param request
	 * @param response
	 * @param userInfo
	 * @return
	 */
	public Map<String,Object> getAdditionalUserInfo(HttpServletRequest request, HttpServletResponse response, Map userInfo);

	/**
	 * sso 끝나고 난뒤 처리.
	 * 
	 * @param request
	 * @param response
	 * @param auth 
	 * @return false = 로그인 거부 failureRedirectUrl 로 redirect, true = 로그인
	 */
	public boolean afterSso(HttpServletRequest request, HttpServletResponse response, Object auth);
	
	
	/**
	 * 접근 거부
	 * 
	 * @param request
	 * @param response
	 * @param auth
	 * @return
	 */
	public String failureRedirectURL(HttpServletRequest request, HttpServletResponse response, Object auth);

	/**
	 * 추가 시용자 권한 처리
	 * 
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	public List<String> getAdditionalAuthorities(String userId, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 로그인 성공후 redirectURL
	 * 
	 * @param request
	 * @param response
	 * @param auth
	 * @return
	 */
	public String successRedirectURL(HttpServletRequest request, HttpServletResponse response, Object auth);
	
}
