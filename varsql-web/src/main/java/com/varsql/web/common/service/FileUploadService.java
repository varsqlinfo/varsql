package com.varsql.web.common.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.varsql.core.configuration.Configuration;
import com.varsql.web.common.controller.fileupload.FileUploadController;
import com.varsql.web.dto.file.FileInfo;
import com.varsql.web.exception.FileNotFoundException;
import com.varsql.web.exception.FileUploadException;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.utils.FileUtils;
import com.vartech.common.utils.StringUtils;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: FileUploadService.java
* @DESC		: file upload service
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class FileUploadService {

	private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	// upload root path
	private final String uploadRootPath = Configuration.getInstance().getFileUploadPath();

	private final Path fileUploadPath = Paths.get(uploadRootPath).toAbsolutePath().normalize();;

	/**
	 *
	 * @Method Name  : loadFileAsResource
	 * @Method 설명 : 파일 resource
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31.
	 * @변경이력  :
	 * @param url
	 * @return
	 */
	public Resource loadFileAsResource(String fileName) throws MalformedURLException {
		Path filePath = this.fileUploadPath.resolve(fileName).normalize();
		Resource resource = new UrlResource(filePath.toUri());
		if (resource.exists()) {
			return resource;
		} else {
			throw new FileNotFoundException("File not found " + fileName);
		}
	}

	/**
	 *
	 * @Method Name  : getUploadFile
	 * @Method 설명 : upload file 얻기
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31.
	 * @변경이력  :
	 * @param url
	 * @return
	 */
	public File getUploadFile(FileInfo fileinfo) throws MalformedURLException {
		Path filePath = this.fileUploadPath.resolve(fileinfo.getFilePath()).normalize();
		return filePath.toFile();
	}

	/**
	 *
	 * @Method Name  : getRootPath
	 * @Method 설명 : get root path 얻기
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31.
	 * @변경이력  :
	 * @param url
	 * @return
	 */
	public String getRootPath () {
		return uploadRootPath;
	}

	/**
	 *
	 * @Method Name  : uploadFile
	 * @Method 설명 : 파일 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31.
	 * @변경이력  :
	 * @param url
	 * @return
	 */

	public FileInfo uploadFile(String div, String paramFileContId, MultipartHttpServletRequest mtfRequest) {
		List<FileInfo> uploadFiles = uploadFiles(div, paramFileContId, mtfRequest);

		if(uploadFiles.size() > 0) {
			return uploadFiles.get(0);
		}else {
			return null;
		}
	}

	/**
	 *
	 * @Method Name  : uploadFiles
	 * @Method 설명 : 멀티 파일 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31.
	 * @변경이력  :
	 * @param url
	 * @return
	 */
	public List<FileInfo> uploadFiles(String div, String paramFileContId, MultipartHttpServletRequest mtfRequest) {

		final String fileContId =StringUtils.isBlank(paramFileContId) ? VarsqlUtils.generateUUID() :paramFileContId;

		Iterator<String> fileNameIter = mtfRequest.getFileNames();

		List<FileInfo> uploadFiles = new ArrayList<FileInfo>();

		// 파일 날짜별(yyyyMMdd) 로 생성 하기
        // 업무 구분 + 날짜별 디렉토리 위치
        String filePath = FileUtils.getSaveDayPath(div);

		while(fileNameIter.hasNext()) {
    		String fileFieldName  = fileNameIter.next();

    		List<MultipartFile> files = mtfRequest.getFiles(fileFieldName);

    		if(files.size() > 0) {
    			files.forEach(file ->{
    				if(!file.isEmpty()) {
    					try {
    						FileInfo fileInfo = saveFile(div, file, filePath);
    						fileInfo.setFileContId(fileContId);
        					fileInfo.setFileFieldName(fileFieldName);
							uploadFiles.add(fileInfo);
						} catch (IllegalStateException | IOException e) {
							logger.error("file upload exception : {}", e.getMessage(), e);
						}
    				}
    			});
    		}
    	}
		return uploadFiles;
	}

	/**
	 *
	 * @Method Name  : saveFile
	 * @Method 설명 : 파일 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31.
	 * @변경이력  :
	 * @param url
	 * @return
	 */
	private FileInfo saveFile(String div, MultipartFile mfileInfo, String filePath) throws IllegalStateException, IOException {
		// 파일 원본명
		String fileName = FileUtils.normalize(mfileInfo.getOriginalFilename());
		// 파일 존재 확인
		if (mfileInfo.isEmpty()) {
			throw new FileUploadException("File empty" + fileName);
		}

		// 파일 확장자 구하기
		String extension = FileUtils.extension(fileName);

		// 파일명을 UUID 로 생성
		String saveFileName = FileUtils.getSaveFileName(fileName, extension);

		filePath = FileUtils.pathConcat(filePath, saveFileName);

		mfileInfo.transferTo(new File(FileUtils.pathConcat(getRootPath(),filePath)));

		return FileInfo.builder()
				.fileDiv(div)
				.fileName(saveFileName)
				.orginFileName(fileName)
				.fileSize(mfileInfo.getSize())
				.fileExt(extension)
				.filePath(filePath).build();

	}
}