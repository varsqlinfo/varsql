package com.varsql.web.app.database.sql;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.el.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.common.util.SecurityUtil;
import com.varsql.web.app.database.DatabaseController;
import com.varsql.web.app.database.beans.SqlParamInfo;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.constants.UserConstants;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.util.VarsqlUtil;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.VartechUtils;

/**
 * 
 * @FileName  : SQLController.java
 * @프로그램 설명 : databse관련 공통으로 처리할 controller
 * @Date      : 2015. 6. 22. 
 * @작성자      : ytkim
 * @변경이력 :
 */
@Controller
@RequestMapping("/sql/base")
public class SQLController {
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);
	
	@Autowired
	private SQLServiceImpl sQLServiceImpl;
	
	/**
	 * 
	 * @Method Name  : sqlData
	 * @Method 설명 : 쿼리 실행시 처리. 
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @throws Exception
	 */
	@RequestMapping({"/sqlData"})
	public @ResponseBody ResponseResult sqlData(SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		return sQLServiceImpl.sqlData(sqlParamInfo);
	}
	
	/**
	 * 
	 * @Method Name  : sqlFormat
	 * @Method 설명 : 쿼리 포켓 
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @throws Exception
	 */
	@RequestMapping({"/sqlFormat"})
	public @ResponseBody String sqlFormat(SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		return sQLServiceImpl.sqlFormat(sqlParamInfo);
	}
	
	/**
	 * 
	 * @Method Name  : dataExport
	 * @Method 설명 : data export
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param vconnid
	 * @param dbtype
	 * @param objectName
	 * @param exportType
	 * @param columnInfo
	 * @param limit
	 * @param req
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({"/dataExport"})
	public void dataExport(SqlParamInfo sqlParamInfo, HttpServletRequest req ,HttpServletResponse response) throws Exception {
		sQLServiceImpl.dataExport(sqlParamInfo, response);
	}

	/**
	 * 쿼리 저장
	 * @param vconnid
	 * @param sqlTitle
	 * @param sql
	 * @param sql_id
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/saveQuery"})
	public @ResponseBody ResponseResult saveQuery( SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		
		sqlParamInfo.setCustomInfo(HttpUtils.getServletRequestParam(req));
		
		return sQLServiceImpl.saveQuery(sqlParamInfo);
	
	}
	/**
	 * 사용자 정보 셋팅
	 * @param vconnid
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/userSettingInfo"})
	public @ResponseBody ResponseResult userSettingInfo(SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		return sQLServiceImpl.userSettingInfo(sqlParamInfo);
	}
	
	/**
	 * sql 저장 목록 보기.
	 * @param vconnid
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/sqlList"})
	public @ResponseBody ResponseResult sqlList(SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		
		sqlParamInfo.setCustomInfo(HttpUtils.getServletRequestParam(req));
		
		return sQLServiceImpl.selectSqlList(sqlParamInfo);
	}
	/**
	 * sql 정보 삭제 하기.
	 * @param vconnid
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/delSqlSaveInfo"})
	public @ResponseBody ResponseResult delSqlSaveInfo(SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		return sQLServiceImpl.deleteSqlSaveInfo(sqlParamInfo);
	}
	
}
