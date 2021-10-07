package com.varsql.web.common.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.exception.BoardInvalidException;
import com.vartech.common.utils.StringUtils;

//TODO 0831
public class DatabaseBoardAuthInterceptor extends HandlerInterceptorAdapter {
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
			throws ServletException, IOException {
		Map pathVariables = (Map) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		String boardId = null; 
		if(pathVariables.containsKey(VarsqlParamConstants.BOARD_CODE)) {
			boardId = String.valueOf(pathVariables.get(VarsqlParamConstants.BOARD_CODE));
			
			req.setAttribute(VarsqlParamConstants.BOARD_CODE, boardId);
			
			if(SecurityUtil.isAdmin()) {
				return true;
			}
			
			if(StringUtils.isBlank(boardId)) {
				return false; 
			}
			
			if (authCheck(req, boardId)) {
				return true;
			}
		}
		
		throw new BoardInvalidException(String.format("Board invalid request userViewId : %s , conuid : %s", SecurityUtil.userViewId(), boardId));
	}

	private boolean authCheck(HttpServletRequest req, String conuid) {
		Map<String, DatabaseInfo> dataBaseInfo = SecurityUtil.loginInfo(req).getDatabaseInfo();
		if (!dataBaseInfo.containsKey(conuid)) {
			return false;
		}
		return true;
	}
}
