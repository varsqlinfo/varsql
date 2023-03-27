package com.varsql.web.common.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.exception.BoardInvalidException;
import com.vartech.common.utils.StringUtils;

public class DatabaseBoardAuthInterceptor implements HandlerInterceptor {
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
			throws ServletException, IOException {
		Map pathVariables = (Map) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		String boardCd = null;
		if(pathVariables.containsKey(VarsqlParamConstants.BOARD_CODE)) {
			boardCd = String.valueOf(pathVariables.get(VarsqlParamConstants.BOARD_CODE));

			req.setAttribute(VarsqlParamConstants.BOARD_CODE, boardCd);

			if(StringUtils.isBlank(boardCd)) {
				return false;
			}

			if (authCheck(req, boardCd)) {
				return true;
			}
		}

		throw new BoardInvalidException(String.format("Board invalid request userViewId : %s , conuid : %s", SecurityUtil.userViewId(), boardCd));
	}

	private boolean authCheck(HttpServletRequest req, String boardCd) {
		Map<String, DatabaseInfo> dataBaseInfo = SecurityUtil.loginInfo(req).getDatabaseInfo();
		if (!dataBaseInfo.containsKey(boardCd)) {
			return false;
		}

		req.setAttribute(VarsqlParamConstants.VCONNID, dataBaseInfo.get(boardCd).getVconnid());

		return true;
	}
}
