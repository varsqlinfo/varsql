package com.varsql.web.common.controller;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.DataExportUtil;
import com.varsql.web.dto.file.DownloadInfo;
import com.varsql.web.util.ValidateUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.utils.VartechReflectionUtils;



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
public class DownloadController extends AbstractController {

	/** The Constant logger. */
	private final Logger logger = LoggerFactory.getLogger(DownloadController.class);


	@RequestMapping(value="/download", method = RequestMethod.POST)
	public void gridDownload(DownloadInfo downloadInfo, HttpServletRequest req ,HttpServletResponse res) throws Exception {

		if(logger.isDebugEnabled()) {
			logger.debug("download info: {}" , VartechReflectionUtils.reflectionToString(downloadInfo));
		}

		VarsqlFileType exportType = downloadInfo.getExportType();

		String downloadName = downloadInfo.getFileName() !=null && !"".equals(downloadInfo.getFileName().trim()) ? downloadInfo.getFileName() : "varsql-download" ;

		downloadName = ValidateUtils.getValidFileName(exportType.concatExtension(downloadName));

		try(OutputStream os = res.getOutputStream()) {

			if(VarsqlFileType.TEXT.equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, req, java.net.URLEncoder.encode(downloadName,VarsqlConstants.CHAR_SET));
				DataExportUtil.toTextWrite(downloadInfo.getContent(), os);
			}
			if(os !=null) os.close();
		}catch(Exception e) {
			logger.error("grid download error : {}", e.getMessage() ,e);
		}

	}
}
