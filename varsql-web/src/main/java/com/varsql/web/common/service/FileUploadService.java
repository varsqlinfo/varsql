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
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.common.controller.fileupload.FileUploadController;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.app.FileInfoEntityRepository;
import com.varsql.web.util.FileServiceUtils;
import com.vartech.common.utils.FileUtils;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

@Service
public class FileUploadService {
	private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private FileInfoEntityRepository fileInfoEntityRepository;

	public FileInfoEntity uploadFile(MultipartHttpServletRequest mtfRequest, UploadFileType fileType, String paramFileContId) {
		List<FileInfoEntity> uploadFiles = uploadFiles(mtfRequest, fileType, paramFileContId);
		if (uploadFiles.size() > 0)
			return uploadFiles.get(0);
		return null;
	}

	public List<FileInfoEntity> uploadFiles(MultipartHttpServletRequest mtfRequest, UploadFileType fileType, String paramFileContId) {
		return uploadFiles(mtfRequest, fileType, paramFileContId, null);
	}

	public List<FileInfoEntity> uploadFiles(MultipartHttpServletRequest mtfRequest, UploadFileType fileType, String contentId,	String contGroupId) {

		String fileContId = StringUtils.isBlank(contentId) ? VartechUtils.generateUUID() : contentId;
		Iterator<String> fileNameIter = mtfRequest.getFileNames();
		List<FileInfoEntity> uploadFiles = new ArrayList<>();

		while (fileNameIter.hasNext()) {
			String fileFieldName = fileNameIter.next();
			List<MultipartFile> files = mtfRequest.getFiles(fileFieldName);
			if (files.size() > 0) {
				uploadFiles.addAll(uploadFiles(fileType, files, fileContId, contGroupId, fileFieldName, false, true));
			}
		}

		if (uploadFiles.size() > 0)	this.fileInfoEntityRepository.saveAll(uploadFiles);

		return uploadFiles;
	}

	public List<FileInfoEntity> uploadFiles(UploadFileType fileType, List<MultipartFile> files, String fileContId, String contGroupId, String fieldName, boolean addExtensionSuffix, boolean fileInfoSaveFlag) {

		List<FileInfoEntity> fileInfos = new ArrayList<FileInfoEntity>();
		files.forEach(file ->{
			if(!(file.getSize() <= 0 && "blob".equals(file.getOriginalFilename()))) {
				try {
					FileInfoEntity fileInfo = saveFile(fileType, file, fileContId, addExtensionSuffix);
					fileInfo.setFileContId(fileContId);
					fileInfo.setFileFieldName(fieldName);
					fileInfo.setContGroupId(contGroupId);
					fileInfos.add(fileInfo);
				} catch (IllegalStateException | IOException e) {
					logger.error("file upload exception : {}", e.getMessage(), e);
					throw new VarsqlRuntimeException(VarsqlAppCode.COMM_FILE_UPLOAD_ERROR, "file upload error", e);
				}
			}
		});

		if(fileInfoSaveFlag) {
			fileInfoEntityRepository.saveAll(fileInfos);
		}

		return fileInfos;
	}
	
	private FileInfoEntity saveFile(UploadFileType fileType, MultipartFile mfileInfo, String contentId, boolean addExtensionSuffix)
			throws IllegalStateException, IOException {

		String filePath = FileServiceUtils.getSaveRelativePath(fileType, contentId);

		// 파일 원본명
		String fileName = FileUtils.normalize(mfileInfo.getOriginalFilename());
		// 파일 확장자 구하기
		String extension = FileUtils.extension(fileName);

		String fileId = VartechUtils.generateUUID();

		if(fileType.isOrginFileName()) {
			filePath = FileUtils.pathConcat(filePath, fileName);
		}else {
			if(addExtensionSuffix) {
				filePath = FileUtils.pathConcat(filePath, String.format("%s.%s",  fileId, extension));
			}else {
				filePath = FileUtils.pathConcat(filePath, fileId);
			}
		}

		Path createFilePath = FileServiceUtils.getPath(filePath);
		mfileInfo.transferTo(createFilePath);
		return FileInfoEntity.builder()
				.fileDiv(fileType.getDiv())
				.fileId(fileId)
				.fileName(fileName)
				.fileSize(mfileInfo.getSize())
				.fileExt(extension)
				.filePath(filePath).build();
	}
}
