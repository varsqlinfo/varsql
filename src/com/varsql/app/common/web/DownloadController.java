package com.varsql.app.common.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varsql.app.common.beans.DownloadInfo;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.DataExportUtil;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.vartech.common.utils.VartechReflectionUtils;
import com.vartech.common.utils.VartechUtils;



/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DownloadController.java
* @DESC		: 공통 다운로드 처리.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 9. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
public class DownloadController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);
	
	
	@RequestMapping({"/download"})
	public void gridDownload(DownloadInfo downloadInfo, HttpServletRequest req ,HttpServletResponse res) throws Exception {
		
		if(logger.isDebugEnabled()) {
			logger.debug("download info: {}" , VartechReflectionUtils.reflectionToString(downloadInfo));
		}
		
		String exportType = downloadInfo.getExportType();
		
		String downloadName = downloadInfo.getFileName() !=null && !"".equals(downloadInfo.getFileName().trim())?downloadInfo.getFileName() : "varsql-download."+ exportType; 
		
		if("text".equals(exportType)){
			VarsqlUtil.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName,VarsqlConstants.CHAR_SET));
			DataExportUtil.toTextWrite(downloadInfo.getContent(), res.getOutputStream());
		}
	}
}
