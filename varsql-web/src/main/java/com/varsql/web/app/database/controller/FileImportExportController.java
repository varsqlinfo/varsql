package com.varsql.web.app.database.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.app.database.service.DatabaseServiceImpl;
import com.varsql.web.app.database.service.PreferencesServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.common.service.FileUploadService;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.dto.db.DBConnTabRequestDTO;
import com.varsql.web.dto.file.FileInfo;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;



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
	@RequestMapping(value="/import", method=RequestMethod.POST)
	public @ResponseBody ResponseResult fileImport(
			@RequestParam(name = "div", required = true) String div
    		, @RequestParam(name="fileContId" , defaultValue = "") String paramFileContId
    		, @RequestParam(name="importType" , required = true) String importType
    		, MultipartHttpServletRequest mtfRequest
			) throws IOException {
		logger.debug("file upload!");

		ResponseResult result = new ResponseResult();

		List<FileInfo> uploadFiles = fileUploadService.uploadFiles(div, paramFileContId , mtfRequest);

        if(uploadFiles.size() > 0) {
        	result.setItemList(uploadFiles);
    	}else {
    		result.setResultCode(VarsqlAppCode.COMM_FILE_EMPTY.code());
    		result.setMessage("select file");
    	}

        return result;
	}

	
}
