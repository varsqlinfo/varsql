package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.database.service.SQLServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.dto.sql.SqlExecuteDTO;
import com.varsql.web.dto.sql.SqlGridDownloadInfo;
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
public class SQLController extends AbstractController  {
	private final Logger logger = LoggerFactory.getLogger(SQLController.class);

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
	@RequestMapping(value = "/sqlData", method = RequestMethod.POST)
	public @ResponseBody ResponseResult sqlData(SqlExecuteDTO sqlExecuteInfo, HttpServletRequest req) throws Exception {
		logger.debug("sqlData , :{}" , sqlExecuteInfo);
		return sQLServiceImpl.sqlData(sqlExecuteInfo, req);
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
	@RequestMapping(value = "/sqlFormat", method = RequestMethod.POST)
	public @ResponseBody ResponseResult sqlFormat(SqlExecuteDTO sqlExecuteInfo, HttpServletRequest req) throws Exception {
		sqlExecuteInfo.addCustom("formatType", HttpUtils.getString(req, "formatType"));
		return sQLServiceImpl.sqlFormat(sqlExecuteInfo);
	}

	/**
	 *
	 * @Method Name  : dataExport
	 * @Method 설명 : data export
	 * @작성일   : 2015. 6. 22.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param req
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/dataExport", method = RequestMethod.POST)
	public void dataExport(SqlExecuteDTO sqlExecuteInfo, HttpServletRequest req ,HttpServletResponse response) throws Exception {
		sQLServiceImpl.dataExport(HttpUtils.getServletRequestParam(req), sqlExecuteInfo, response);
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
	@RequestMapping(value = "/gridDownload", method = RequestMethod.POST)
	public void gridDownload(SqlGridDownloadInfo sqlGridDownloadInfo, HttpServletRequest req ,HttpServletResponse response) throws Exception {
		sQLServiceImpl.gridDownload(sqlGridDownloadInfo, response);
	}

}
