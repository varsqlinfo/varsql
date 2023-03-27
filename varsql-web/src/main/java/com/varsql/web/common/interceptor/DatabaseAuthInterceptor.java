package com.varsql.web.common.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.exception.DatabaseInvalidException;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : DatabaseAuthInterceptor.java
 * @프로그램 설명 : database check 인터 셉터.
 * @Date      : 2015. 6. 22.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class DatabaseAuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
			throws ServletException, IOException {
		String conuid =req.getParameter(VarsqlParamConstants.CONN_UUID);

		if (!authCheck(req, conuid)) {
			if(StringUtils.isBlank(conuid) && VarsqlUtils.isRuntimelocal() && SecurityUtil.isAdmin()) {
				return true;
			}

			throw new DatabaseInvalidException(String.format("Database invalid request userViewId : %s , conuid : %s", SecurityUtil.userViewId() , conuid));
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
	 * 			/database/base 밑이면 conuid만 체크
	 * 			각 데이터베이스 벤더의 data를 콜하는 경우면 database type까지 체크.
	 * @param req
	 * @param connid
	 * @return
	 */
	private boolean authCheck(HttpServletRequest req, String conuid) {
		Map<String, DatabaseInfo> dataBaseInfo = SecurityUtil.loginInfo(req).getDatabaseInfo();

		if(!dataBaseInfo.containsKey(conuid)){
			return false;
		}

		req.setAttribute(VarsqlParamConstants.CONN_UUID, conuid);
		req.setAttribute(VarsqlParamConstants.DB_TYPE, dataBaseInfo.get(conuid).getType());
		req.setAttribute(VarsqlParamConstants.VCONNID, dataBaseInfo.get(conuid).getVconnid());
		req.setAttribute(VarsqlParamConstants.DB_SCHEMA, dataBaseInfo.get(conuid).getSchema());
		return true;
	}
}
