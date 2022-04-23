package com.varsql.web.app.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.user.service.UserPreferencesServiceFileImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.UploadFileType;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;

//TODO 2022
@Controller
@RequestMapping({ "/user/preferences/file" })
public class UserPreferencesFileController extends AbstractController {
	private final Logger logger = LoggerFactory.getLogger(UserPreferencesFileController.class);

	@Autowired
	private UserPreferencesServiceFileImpl userPreferencesServiceFileImpl;

	@RequestMapping(value = { "/fileList" }, method = { RequestMethod.POST })
	@ResponseBody
	public ResponseResult importFileList(@RequestParam(value = "fileType", required = true) String fileType,
			@RequestParam(value = "vconnid", required = true) String vconnid, HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		return this.userPreferencesServiceFileImpl.importFileList(UploadFileType.getDivType(fileType), vconnid, searchParameter);
	}
	
	@RequestMapping(value = { "/detail" }, method = { RequestMethod.POST })
	@ResponseBody
	public ResponseResult detail(@RequestParam(value = "fileId", required = true) String fileId, HttpServletRequest req) throws Exception {
		return this.userPreferencesServiceFileImpl.detail(fileId);
	}
	
	@RequestMapping(value = { "/zipDetail" }, method = { RequestMethod.POST })
	@ResponseBody
	public ResponseResult zipFileDetail(@RequestParam(value = "fileId", required = true) String fileId,@RequestParam(value = "fileName", required = true) String fileName, HttpServletRequest req) throws Exception {
		return this.userPreferencesServiceFileImpl.zipFileDetail(fileId, fileName);
	}
	
	/**
	 *
	 * @Method Name  : delete
	 * @Method 설명 : 삭제
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/delete" ,method = RequestMethod.POST)
	public @ResponseBody ResponseResult delete(@RequestParam(value = "selectItem", required = true )  String selectItem , HttpServletRequest req){
		logger.debug("remove file removeIds : {}" , selectItem);
		return  userPreferencesServiceFileImpl.deleteFiles(selectItem);
	}
}
