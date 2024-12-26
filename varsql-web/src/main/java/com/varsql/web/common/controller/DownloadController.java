package com.varsql.web.common.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.DataExportUtil;
import com.varsql.core.exception.FileNotFoundException;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.dto.file.DownloadInfo;
import com.varsql.web.model.entity.FileBaseEntity;
import com.varsql.web.util.FileServiceUtils;
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
			VarsqlUtils.setResponseDownAttr(res, req, java.net.URLEncoder.encode(downloadName,VarsqlConstants.CHAR_SET));
			DataExportUtil.toTextWrite(downloadInfo.getContent(), os);
			
			if(os !=null) os.close();
		}catch(Exception e) {
			logger.error("grid download error : {}", e.getMessage() ,e);
		}

	}
	
	@RequestMapping("/imageView/{fileId}")
    public void imgView(@PathVariable("fileId") String fileId 
    		, @RequestParam(name = "div", defaultValue = "boardContent") String div
    		, HttpServletRequest req ,HttpServletResponse res) throws FileNotFoundException, java.io.FileNotFoundException, IOException {
    	
		if(logger.isDebugEnabled()) {
			logger.debug("imageView div: {}, fileId :{}", div,fileId);
		}
		
		String yyyy = fileId.substring(0, 4);
		String mm = fileId.substring(4, 6);
		String id = fileId.substring(6);
		
		String path = String.format("%s/%s/%s/%s", UploadFileType.BOARD_CONTENT_IMAGE.getSavePathRoot(), yyyy, mm, id) ;
		
		FileBaseEntity fbe = new FileBaseEntity();
		
		fbe.setFilePath(path);
		
		File file = FileServiceUtils.getFileInfoToFile(fbe);
		
		if (file.isFile()) {
			byte b[] = new byte[FileServiceUtils.BUFFER_SIZE];
			
			try(BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream outs = new BufferedOutputStream(res.getOutputStream());){
				int read = 0;
				while ((read = fin.read(b)) != -1){
					outs.write(b,0,read);
				}

				if(fin != null) fin.close();
				if(outs != null) outs.close();
			}
		}
    }
}
