package com.varsql.web.common.controller.fileupload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.varsql.core.configuration.Configuration;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.dto.file.FileInfo;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.DateUtils;
import com.vartech.common.utils.VartechUtils;



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
@RequestMapping("/upload")
public class FileUploadController extends AbstractController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	
	String fileUploadPath = Configuration.getInstance().getFileUploadPath();
	
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
	@RequestMapping(value="/file", method=RequestMethod.POST)
	public @ResponseBody ResponseResult fileUpload(@RequestParam(value = "file", required = true) MultipartFile uploadfile
			,@RequestParam(value = "param", required = true, defaultValue = "{}") String jsonParam) throws IOException {
		logger.debug("Single file upload!");
		
		ParamMap param = VarsqlUtils.stringToObject(jsonParam);
		
		ResponseResult result = new ResponseResult(); 
        if (uploadfile.isEmpty()) {
        	result.setMessage("select file");
        }else {
        	List<FileInfo> fileLists= uploadFile(Arrays.asList(uploadfile));
        	
        	if(fileLists.size() > 0) {
        		FileInfo fileInfo = fileLists.get(0);
        		if(param.getBoolean("read")) {
        			
        		}else {
        			
        		}
        		result.setItemOne(fileInfo);
        	}else {
        		result.setMessage("select file");
        	}
        }
        
        return result;
	}
	
	/**
	 * 
	 * @Method Name  : multiFileUpload
	 * @Method 설명 : multi file upload
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 29. 
	 * @변경이력  :
	 * @param uploadfile
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/multiFile", method=RequestMethod.POST)
	public @ResponseBody ResponseResult multiFileUpload(@RequestParam(value = "file", required = true) MultipartFile uploadfile
			,@RequestParam(value = "param", required = true, defaultValue = "{}") String jsonParam) throws IOException {
		List<FileInfo> fileLists  =uploadFile(Arrays.asList(uploadfile));
		
		ResponseResult result = new ResponseResult();
		
		result.setItemList(fileLists);
		
		return result; 	
	}
	
	private List<FileInfo> uploadFile(List<MultipartFile> files) throws IllegalStateException, IOException {
		List<FileInfo> fileLists = new ArrayList<FileInfo>();
		
		File uploadFile;
		FileInfo fileInfo;
		int fileLen = files.size(); 
		
		String uploadDir = fileUploadPath+File.separator +DateUtils.getCurrentDate("yyyyMMdd");
		
		final File dir = new File(uploadDir);
		if(!dir.isDirectory()){
			dir.mkdirs();
		}
		
		for (int i = 0; i < fileLen ; i++) {
			MultipartFile mfileInfo = files.get(i);
			
			String orginName = mfileInfo.getOriginalFilename(); 
			
			if(StringUtils.isEmpty(orginName)) {
				continue ; 
			}
			
			String extensions = FilenameUtils.getExtension(orginName); 
			String saveFileName = VartechUtils.generateUUID() +"."+extensions; 
			fileInfo = new FileInfo();
			
			fileInfo.setFileName(orginName);
			fileInfo.setSize(mfileInfo.getSize());
			fileInfo.setExtensions(extensions);
			fileInfo.setSaveFileName(saveFileName);
			fileInfo.setSavePath(uploadDir);
			
			uploadFile = new File(uploadDir,fileInfo.getSaveFileName());
			
			mfileInfo.transferTo(uploadFile);	
			
			fileLists.add(fileInfo);
		}
		
		return fileLists; 
	}
}
