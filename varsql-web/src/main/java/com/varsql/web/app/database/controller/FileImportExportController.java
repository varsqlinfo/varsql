package com.varsql.web.app.database.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.database.service.FileImportExportServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.common.service.FileUploadService;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.StringUtils;



/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: FileImportExportController.java
* @DESC		: database sql , data import , export
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/database/file")
public class FileImportExportController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(FileImportExportController.class);

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private FileImportExportServiceImpl fileImportExportServiceImpl;

	/**
	 *
	 * @Method Name  : fileImport
	 * @Method 설명 : file import
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 29.
	 * @변경이력  :
	 * @param uploadfile
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public @ResponseBody ResponseResult upload(
			@RequestParam(name = "div", required = true) String div
			, @RequestParam(name="fileExtensions" , required = true) String fileExtensions
    		, @RequestParam(name="fileContId" , defaultValue = "") String paramFileContId
    		, @RequestParam(name="conuid" , defaultValue = "") String conuid
    		, MultipartHttpServletRequest mtfRequest
			) throws IOException {
		logger.debug("file upload!");

		ResponseResult result = new ResponseResult();

		String vconnid= SecurityUtil.getVconnid(conuid);

		List<FileInfoEntity> uploadFiles = fileUploadService.uploadFiles(mtfRequest, UploadFileType.getDivType(div), paramFileContId, vconnid);

        if(uploadFiles.size() > 0) {
        	result.setItemList(uploadFiles);
    	}else {
    		result.setResultCode(VarsqlAppCode.COMM_FILE_EMPTY);
    		result.setMessage("select file");
    	}

        return result;
	}

	@RequestMapping(value="/import", method=RequestMethod.POST)
	public @ResponseBody ResponseResult fileImport(
			@RequestParam(name="importType",required = true) String importType
			, @RequestParam(name="fileIds", required = true) String fileIds
			, @RequestParam(name="conuid", required = true) String conuid
			, HttpServletRequest request
			) throws IOException {
		logger.debug("file fileImport! importType : {}, conuid : {} , fileIds : {}" , importType , conuid, fileIds);

		if(StringUtils.isBlank(fileIds)) {
			ResponseResult result = new ResponseResult();
			result.setResultCode(VarsqlAppCode.COMM_FILE_EMPTY);
			result.setMessage("select file");
			return result;
		}

		return fileImportExportServiceImpl.importFile(conuid, importType, fileIds);

	}


}
