package com.varsql.web.common.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.model.entity.db.DBTypeDriverFileEntity;
import com.varsql.web.model.entity.scheduler.ScheduleHistoryEntity;
import com.varsql.web.repository.app.FileInfoEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverFileEntityRepository;
import com.varsql.web.repository.scheduler.ScheduleHistoryEntityRepository;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.CommUtils;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/file")
@AllArgsConstructor
public class FileDownloadController {
	private final static Logger logger = LoggerFactory.getLogger(FileDownloadController.class);
	
	private FileInfoEntityRepository fileInfoEntityRepository;
	private DBTypeDriverFileEntityRepository dbTypeDriverFileEntityRepository;
	private ScheduleHistoryEntityRepository ScheduleHistoryEntityRepository;
	
	// 첨부파일 다운로드
	@RequestMapping(value = "/download")
	public void fileDownload(@RequestParam(value = "contId") String contId,
			@RequestParam(value = "fileId") String fileId, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		logger.debug("fileDownload");
		
		List<FileInfoEntity> fileList = new ArrayList<>();
		
		if(!StringUtils.isBlank(fileId)) {
			
			FileInfoEntity fie= fileInfoEntityRepository.findByFileId(fileId);
			if(fie != null) {
				fileList.add(fie);
			}
			
		}else if(!StringUtils.isBlank(contId)) {
			fileList = fileInfoEntityRepository.findByFileContId(fileId);
		}

		if (fileList.size() < 1) {
			fileErrorMessage(req,res, VarsqlAppCode.COMM_FILE_EMPTY);
			return;
		}
		
		DataMap param =HttpUtils.getServletRequestParam(req);

		String downFileName = "";
		int fileSize = fileList.size();
		if (fileSize == 1) {
			downFileName = fileList.get(0).getFileName();
		} else {
			downFileName = param.getString("downFileName", "downloadFile");
			downFileName = downFileName + ".zip";
		}
		FileServiceUtils.fileDownload(req, res, downFileName, fileList.toArray(new FileInfoEntity[0]));
	}
	
	// 첨부파일 다운로드
	@RequestMapping(value = "/backup/download")
	public void backupFileDownload(	@RequestParam(value = "fileId", required = true) long fileId, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		logger.debug("backupFileDownload");
		
		if(!(SecurityUtil.isManager() || SecurityUtil.isAdmin())) {
			fileErrorMessage(req, res, VarsqlAppCode.SECURITY_AUTH_ERROR);
		}
		
		ScheduleHistoryEntity she =null;
		if(fileId > 0) {
			she = ScheduleHistoryEntityRepository.findByHistSeq(fileId);
		}
		
		String customInfo = she.getCustomInfo();
		
		if (she==null || StringUtils.isBlank(customInfo)) {
			fileErrorMessage(req, res, VarsqlAppCode.COMM_FILE_EMPTY);
			return;
		}
		
		Map customInfoMap = VartechUtils.jsonStringToObject(customInfo, HashMap.class);
		
		if(!customInfoMap.containsKey("filePath")) {
			fileErrorMessage(req, res, VarsqlAppCode.COMM_FILE_EMPTY);
			return;
		}
		
		String filePath = customInfoMap.get("filePath").toString();

		File downloadFile = new File(filePath);
		
		FileServiceUtils.fileDownload(req, res, downloadFile.getName(), downloadFile);
	}
	
	@RequestMapping(value = "/driverFileDownload")
	public void driverFileDownload(@RequestParam(value = "contId") String contId,
			@RequestParam(value = "fileId") String fileId, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		logger.debug("fileDownload");
		
		List<DBTypeDriverFileEntity> fileList = new ArrayList<>();
		
		if(!StringUtils.isBlank(fileId)) {
			
			DBTypeDriverFileEntity fie= dbTypeDriverFileEntityRepository.findByFileId(fileId);
			if(fie != null) {
				fileList.add(fie);
			}
			
		}else if(!StringUtils.isBlank(contId)) {
			fileList = dbTypeDriverFileEntityRepository.findByFileContId(fileId);
		}
		
		if (fileList.size() < 1) {
			fileErrorMessage(req, res, VarsqlAppCode.COMM_FILE_EMPTY);
			return;
		}
		
		DataMap param =HttpUtils.getServletRequestParam(req);
		
		String downFileName = "";
		int fileSize = fileList.size();
		if (fileSize == 1) {
			downFileName = fileList.get(0).getFileName();
		} else {
			downFileName = param.getString("downFileName", "downloadFile");
			downFileName = downFileName + ".zip";
		}
		
		FileServiceUtils.fileDownload(req, res, downFileName, fileList.toArray(new DBTypeDriverFileEntity[0]));
	}

	private void fileErrorMessage(HttpServletRequest req, HttpServletResponse res, VarsqlAppCode appCode) throws IOException {
		try (PrintWriter out = res.getWriter()) {
			
			if(VarsqlUtils.isAjaxRequest(req)){
				res.setContentType(VarsqlConstants.JSON_CONTENT_TYPE);
				if(VarsqlAppCode.COMM_FILE_EMPTY.equals(appCode)) {
					res.setStatus(HttpStatus.NOT_FOUND.value());
					out.write("file not found");
				}else if(VarsqlAppCode.SECURITY_AUTH_ERROR.equals(appCode)) {
					res.setStatus(HttpStatus.UNAUTHORIZED.value());
					out.write("unauthorized");
				}
				
			}else{
				res.setContentType("text/html");
				if(VarsqlAppCode.COMM_FILE_EMPTY.equals(appCode)) {
					res.setStatus(HttpStatus.OK.value());
					out.write("<script>alert('file not found')</script>");
				}else if(VarsqlAppCode.SECURITY_AUTH_ERROR.equals(appCode)) {
					res.setStatus(HttpStatus.OK.value());
					out.write("<script>alert('unauthorized')</script>");
				}
			}
		}
	}
}
