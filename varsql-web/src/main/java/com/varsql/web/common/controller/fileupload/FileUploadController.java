package com.varsql.web.common.controller.fileupload;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.common.service.FileUploadService;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.vartech.common.app.beans.ResponseResult;

import lombok.RequiredArgsConstructor;



/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: FileUploadController.java
* @DESC		: file upload controller
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	private final FileUploadService fileUploadService;

	/**
	 *
	 * @Method Name  : fileUpload
	 * @Method 설명 : unit file upload
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 29.
	 * @변경이력  :
	 * @param uploadfile
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public @ResponseBody ResponseResult fileUploads(
			@RequestParam(name = "div", required = true) String div
    		, @RequestParam(name="fileContId" , defaultValue = "") String paramFileContId
    		, MultipartHttpServletRequest mtfRequest
			) throws IOException {
		logger.debug("file upload!");

		ResponseResult result = new ResponseResult();

		List<FileInfoEntity> uploadFiles = fileUploadService.uploadFiles(mtfRequest, UploadFileType.getDivType(div), paramFileContId);

        if(uploadFiles.size() > 0) {
        	result.setList(uploadFiles);
    	}else {
    		result.setResultCode(VarsqlAppCode.COMM_FILE_EMPTY);
    		result.setMessage("select file");
    	}

        return result;
	}
	
	/**
	 * image file upload 
	 * 
	 * @param div
	 * @param paramFileContId
	 * @param mtfRequest
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/imageUpload", method=RequestMethod.POST)
	public @ResponseBody ResponseResult imgFileUpload(
			 MultipartHttpServletRequest mtfRequest
			) throws IOException {
		logger.debug("image file upload!");
		
		ResponseResult result = new ResponseResult();
		
		Iterator<String> fileNameIter = mtfRequest.getFileNames();
		FileInfoEntity uploadFile = null;
		while (fileNameIter.hasNext()) {
			String fileFieldName = fileNameIter.next();
			List<MultipartFile> files = mtfRequest.getFiles(fileFieldName);
			if (files.size() > 0) {
				uploadFile = fileUploadService.onlySaveFile(UploadFileType.BOARD_CONTENT_IMAGE, files.get(0), "", false);
			}
		}
		
		if(uploadFile != null) {
			String viewId = uploadFile.getFilePath().replaceFirst(UploadFileType.BOARD_CONTENT_IMAGE.getSavePathRoot(), "");
			viewId = viewId.replaceAll("/", "");
			
			uploadFile.setFileContId(viewId);
			result.setItemOne(uploadFile);
		}else {
			result.setResultCode(VarsqlAppCode.COMM_FILE_EMPTY);
			result.setMessage("select file");
		}
		
		return result;
	}
}
