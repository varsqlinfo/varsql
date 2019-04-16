package com.varsql.app.database.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.app.database.beans.SqlGridDownloadInfo;
import com.varsql.app.database.beans.SqlParamInfo;
import com.varsql.app.database.service.SQLServiceImpl;
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
		return sQLServiceImpl.sqlData(sqlParamInfo, req);
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
	public @ResponseBody ResponseResult sqlFormat(SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		sqlParamInfo.addCustom("formatType", HttpUtils.getString(req, "formatType"));
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
		sQLServiceImpl.dataExport(HttpUtils.getServletRequestParam(req), sqlParamInfo, response);
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
		sqlParamInfo.setCustom(HttpUtils.getServletRequestParam(req));
		return sQLServiceImpl.saveQuery(sqlParamInfo);
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
	@RequestMapping({"/saveAllQuery"})
	public @ResponseBody ResponseResult saveAllQuery( SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		sqlParamInfo.setCustom(HttpUtils.getServletRequestParam(req));
		return sQLServiceImpl.saveAllQuery(sqlParamInfo);
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
	public @ResponseBody ResponseResult sqlFileDetailInfo( SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		sqlParamInfo.setCustom(HttpUtils.getServletRequestParam(req));
		return sQLServiceImpl.sqlFileDetailInfo(sqlParamInfo);
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
		
		sqlParamInfo.setCustom(HttpUtils.getServletRequestParam(req));
		
		return sQLServiceImpl.selectSqlFileList(sqlParamInfo);
	}
	
	@RequestMapping({"/sqlFileTab"})
	public @ResponseBody ResponseResult sqlFileTab(SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		
		sqlParamInfo.setCustom(HttpUtils.getServletRequestParam(req));
		
		return sQLServiceImpl.selectSqlFileTabList(sqlParamInfo);
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
	
	/**
	 * 
	 * @Method Name  : gridDownload
	 * @Method 설명 : grid data download
	 * @작성자   : ytkim
	 * @작성일   : 2018. 10. 12. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @param req
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({"/gridDownload"})
	public void gridDownload(SqlGridDownloadInfo sqlGridDownloadInfo, HttpServletRequest req ,HttpServletResponse response) throws Exception {
		sQLServiceImpl.gridDownload(sqlGridDownloadInfo, response);
	}
	
}
