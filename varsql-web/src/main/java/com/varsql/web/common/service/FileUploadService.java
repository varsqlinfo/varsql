package com.varsql.web.common.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.code.VarsqlFilePathCode;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.common.controller.fileupload.FileUploadController;
import com.varsql.web.exception.FileUploadException;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.user.FileInfoEntityRepository;
import com.varsql.web.util.FileServiceUtils;
import com.vartech.common.utils.FileUtils;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

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

	@Autowired
	private FileInfoEntityRepository fileInfoEntityRepository;

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

	public FileInfoEntity uploadFile(MultipartHttpServletRequest mtfRequest, String div, String paramFileContId) {
		List<FileInfoEntity> uploadFiles = uploadFiles(mtfRequest, div, paramFileContId);

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
	public List<FileInfoEntity> uploadFiles(MultipartHttpServletRequest mtfRequest, String div, String paramFileContId) {
		return uploadFiles(mtfRequest, div, paramFileContId, null);
	}

	public List<FileInfoEntity> uploadFiles(MultipartHttpServletRequest mtfRequest, String div, String paramFileContId,	String vconnid) {
		final String fileContId =StringUtils.isBlank(paramFileContId) ? VartechUtils.generateUUID() :paramFileContId;

		Iterator<String> fileNameIter = mtfRequest.getFileNames();

		List<FileInfoEntity> uploadFiles = new ArrayList<FileInfoEntity>();

		// 파일 날짜별(yyyyMMdd) 로 생성 하기
        // 업무 구분 + 날짜별 디렉토리 위치
        String filePath = FileServiceUtils.getSaveRelativePath(VarsqlFilePathCode.getFileType(div).getDiv());

		while(fileNameIter.hasNext()) {
    		String fileFieldName  = fileNameIter.next();

    		List<MultipartFile> files = mtfRequest.getFiles(fileFieldName);

    		if(files.size() > 0) {
    			files.forEach(file ->{
    				if(!file.isEmpty()) {
    					try {
    						FileInfoEntity fileInfo = saveFile(div, file, filePath);
    						fileInfo.setFileContId(fileContId);
        					fileInfo.setFileFieldName(fileFieldName);
        					fileInfo.setVconnid(vconnid);
							uploadFiles.add(fileInfo);
						} catch (IllegalStateException | IOException e) {
							logger.error("file upload exception : {}", e.getMessage(), e);
							throw new VarsqlRuntimeException(VarsqlAppCode.COMM_FILE_UPLOAD_ERROR, e, "file upload error");
						}
    				}
    			});
    		}
    	}

		if(uploadFiles.size() > 0 ) {
			fileInfoEntityRepository.saveAll(uploadFiles);
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
	private FileInfoEntity saveFile(String div, MultipartFile mfileInfo, String filePath) throws IllegalStateException, IOException {
		// 파일 존재 확인
		if (mfileInfo.isEmpty()) {
			throw new FileUploadException("File empty : " + mfileInfo.getOriginalFilename());
		}

		// 파일 원본명
		String fileName = FileUtils.normalize(mfileInfo.getOriginalFilename());

		// 파일 확장자 구하기
		String extension = FileUtils.extension(fileName);

		String fileId = VartechUtils.generateUUID();

		// 파일명을 UUID 로 생성
		String saveFileName = String.format("%s.%s", fileId, extension);

		filePath = FileUtils.pathConcat(filePath, saveFileName);

		Path createFilePath = FileServiceUtils.getPath(filePath);

		mfileInfo.transferTo(createFilePath);

		return FileInfoEntity.builder()
				.fileDiv(div)
				.fileId(fileId)
				.fileName(fileName)
				.fileSize(mfileInfo.getSize())
				.fileExt(extension)
				.filePath(filePath).build();

	}
}