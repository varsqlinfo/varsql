package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.database.service.SQLFileServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.dto.sql.SqlFileRequestDTO;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;

/**
 *
 * @FileName  : SQLController.java
 * @프로그램 설명 :  sql 관련 공통으로 처리할 controller
 * @Date      : 2015. 6. 22.
 * @작성자      : ytkim
 * @변경이력 :
 */
@Controller
@RequestMapping("/sql/file")
public class SQLFileController extends AbstractController  {
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(SQLFileController.class);

	@Autowired
	private SQLFileServiceImpl sqlFileServiceImpl;

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
	@RequestMapping({"/saveSql"})
	public @ResponseBody ResponseResult saveSql( SqlFileRequestDTO sqlParamInfo, HttpServletRequest req) throws Exception {
		sqlParamInfo.setCustom(HttpUtils.getServletRequestParam(req));
		return sqlFileServiceImpl.saveSql(sqlParamInfo);
	}

	/**
	 *
	 * @Method Name  : saveAllQuery
	 * @Method 설명 : sql 모두 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 26.
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/saveAllSql"})
	public @ResponseBody ResponseResult saveAllSql( SqlFileRequestDTO sqlParamInfo, HttpServletRequest req) throws Exception {
		sqlParamInfo.setCustom(HttpUtils.getServletRequestParam(req));
		return sqlFileServiceImpl.saveAllSql(sqlParamInfo);
	}

	/**
	 *
	 * @Method Name  : sqlFileDetailInfo
	 * @Method 설명 : sql file 상세보기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 26.
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/sqlFileDetailInfo"})
	public @ResponseBody ResponseResult sqlFileDetailInfo( SqlFileRequestDTO sqlParamInfo, HttpServletRequest req) throws Exception {
		sqlParamInfo.setCustom(HttpUtils.getServletRequestParam(req));
		return sqlFileServiceImpl.sqlFileDetailInfo(sqlParamInfo);
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
	public @ResponseBody ResponseResult sqlList(SqlFileRequestDTO sqlParamInfo, HttpServletRequest req) throws Exception {

		sqlParamInfo.setCustom(HttpUtils.getServletRequestParam(req));

		return sqlFileServiceImpl.selectSqlFileList(sqlParamInfo);
	}

	@RequestMapping({"/sqlFileTab"})
	public @ResponseBody ResponseResult sqlFileTab(SqlFileRequestDTO sqlParamInfo, HttpServletRequest req) throws Exception {

		sqlParamInfo.setCustom(HttpUtils.getServletRequestParam(req));

		return sqlFileServiceImpl.selectSqlFileTabList(sqlParamInfo);
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
	public @ResponseBody ResponseResult delSqlSaveInfo(SqlFileRequestDTO sqlParamInfo, HttpServletRequest req) throws Exception {
		return sqlFileServiceImpl.deleteSqlSaveInfo(sqlParamInfo);
	}
}
