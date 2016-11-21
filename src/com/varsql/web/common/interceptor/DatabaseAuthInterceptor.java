package com.varsql.web.common.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.varsql.common.util.SecurityUtil;
import com.varsql.constants.VarsqlConstants;
import com.varsql.web.common.constants.VarsqlParamConstants;

/**
 * 
 * @FileName  : DatabaseAuthInterceptor.java
 * @프로그램 설명 : database check 인터 셉터.
 * @Date      : 2015. 6. 22. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class DatabaseAuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
			throws ServletException, IOException {
		String connid =req.getParameter(VarsqlParamConstants.VCONNID);
		if (!authCheck(req, connid)) {
			res.sendRedirect(req.getContextPath()+"/error401.do");
			return false; 
		}
		return true;
	}
	
	/**
	 * 
	 * @Method Name  : authCheck
	 * @Method 설명 : database 관련 권한 체크. 
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :  
	 * 			/database/base 밑이면 connid만 체크 
	 * 			각 데이터베이스 벤더의 data를 콜하는 경우면 database type까지 체크. 
	 * @param req
	 * @param connid
	 * @return
	 */
	private boolean authCheck(HttpServletRequest req, String connid) {
		if(SecurityUtil.loginInfo(req).getDatabaseInfo().containsKey(connid)){
			String uri =req.getRequestURI(); 
			if(uri.indexOf("/database/base/") > 0){
				return true; 
			}else{
				String type = (String)SecurityUtil.loginInfo(req).getDatabaseInfo().get(connid).get(VarsqlConstants.CONN_TYPE);
				type = type.toLowerCase();
				return uri.indexOf("/"+type+"/") > -1?true :false;
			}
		}else{
			return false;
		}
		
	}
}
