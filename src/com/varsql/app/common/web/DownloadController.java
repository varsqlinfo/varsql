package com.varsql.app.common.web;

import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.varsql.app.common.beans.DownloadInfo;
import com.varsql.app.util.CheckUtils;
import com.varsql.app.util.VarsqlUtils;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.DataExportUtil;
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
	private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);
	
	
	@RequestMapping({"/download"})
	public void gridDownload(DownloadInfo downloadInfo, HttpServletRequest req ,HttpServletResponse res) throws Exception {
		
		if(logger.isDebugEnabled()) {
			logger.debug("download info: {}" , VartechReflectionUtils.reflectionToString(downloadInfo));
		}
		
		String exportType = downloadInfo.getExportType();
		
		String downloadName = downloadInfo.getFileName() !=null && !"".equals(downloadInfo.getFileName().trim())?downloadInfo.getFileName() : "varsql-download."+ exportType;
		
		downloadName = CheckUtils.getValidFileName(downloadName);
		
		OutputStream os = res.getOutputStream();
		
		try {
			if("text".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName,VarsqlConstants.CHAR_SET));
				DataExportUtil.toTextWrite(downloadInfo.getContent(), os);
			}
			if(os !=null) os.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(os);
		}
		
	}
}
