package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.database.service.SQLServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.dto.db.SqlExecuteDTO;
import com.varsql.web.dto.sql.SqlGridDownloadInfo;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;

import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class SQLController extends AbstractController  {
	private final Logger logger = LoggerFactory.getLogger(SQLController.class);

	private final SQLServiceImpl sqlServiceImpl;

	/**
	 *
	 * @Method Name  : sqlData
	 * @Method 설명 : 쿼리 실행시 처리.
	 * @작성일   : 2015. 6. 22.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @throws Exception
	 */
	@RequestMapping(value = "/execute", method = RequestMethod.POST)
	public @ResponseBody ResponseResult sqlExecute(SqlExecuteDTO sqlExecuteInfo, HttpServletRequest req) throws Exception {
		logger.debug("sqlData , :{}" , sqlExecuteInfo);
		
		return sqlServiceImpl.sqlExecute(sqlExecuteInfo, VarsqlUtils.getClientIp(req));
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
	@RequestMapping(value = "/format", method = RequestMethod.POST)
	public @ResponseBody ResponseResult sqlFormat(SqlExecuteDTO sqlExecuteInfo, HttpServletRequest req) throws Exception {
		sqlExecuteInfo.addCustom("formatType", HttpUtils.getString(req, "formatType"));
		return sqlServiceImpl.sqlFormat(sqlExecuteInfo);
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
	public @ResponseBody ResponseResult dataExport(SqlExecuteDTO sqlExecuteInfo, HttpServletRequest req ,HttpServletResponse response) throws Exception {
		return sqlServiceImpl.dataExport(HttpUtils.getServletRequestParam(req), sqlExecuteInfo, req, response);
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
		sqlServiceImpl.gridDownload(sqlGridDownloadInfo, req, response);
	}
	
	
	/**
	 * 실행중인 request sql 취소
	 * 
	 * @param requestUid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value =  "/requestCancel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult requestCancel(@RequestParam(value = HttpParamConstants.REQ_UID, required = true) String requestUid) throws Exception {
		return VarsqlUtils.getResponseResultItemOne(sqlServiceImpl.requestCancel(requestUid));
	}
}
