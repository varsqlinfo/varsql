package com.varsql.web.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.web.exception.FileNotFoundException;
import com.varsql.web.exception.VarsqlAppException;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.vartech.common.utils.FileUtils;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: FileServiceUtils.java
* @DESC		: file service utils
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class FileServiceUtils {

	private FileServiceUtils() {};


	/**
	 * @method  : getUploadRootPath
	 * @desc : get upload root path
	 * @author   : ytkim
	 * @date   : 2021. 1. 3.
	 * @return
	 */
	public static Path getUploadRootPath(){
		return Paths.get(VarsqlConstants.UPLOAD_PATH).toAbsolutePath().normalize();
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
	public static File getUploadFile(FileInfoEntity fileinfo){
		Path filePath = getUploadRootPath().resolve(fileinfo.getFilePath()).normalize();
		return filePath.toFile();
	}

	/**
	 * @method  : getSavePath
	 * @desc :
	 * @author   : ytkim
	 * @date   : 2021. 1. 3.
	 * @param div
	 * @return
	 * @throws IOException
	 */
	public static Path getSavePath(String div) throws IOException {
		Path fileExportPath = getUploadRootPath().resolve(FileUtils.getSaveDayPath(div));

		if (Files.notExists(fileExportPath)) {
			Files.createDirectories(fileExportPath);
		}
		return fileExportPath;
	}

	/**
	 * @method  : getSaveRelativePath
	 * @desc : save
	 * @author   : ytkim
	 * @date   : 2021. 1. 3.
	 * @param div
	 * @return
	 * @throws IOException
	 */
	public static String getSaveRelativePath(String div) {

		String relativePath = FileUtils.getSaveDayPath(div);
		Path fileExportPath = getUploadRootPath().resolve(relativePath);

		if (Files.notExists(fileExportPath)) {
			try {
				Files.createDirectories(fileExportPath);
			} catch (IOException e) {
				throw new VarsqlAppException(e.getMessage() , e);
			}
		}
		return relativePath;
	}

	/**
	 * @method  : loadFileAsResource
	 * @desc : file nam
	 * @author   : ytkim
	 * @date   : 2021. 1. 3.
	 * @param fileName
	 * @return
	 * @throws MalformedURLException
	 */
	public static Resource loadFileAsResource(String filePath) throws MalformedURLException {
		Path path = getUploadRootPath().resolve(filePath).normalize();

		Resource resource = new UrlResource(path.toUri());
		if (resource.exists()) {
			return resource;
		} else {
			throw new FileNotFoundException("File resource not found " + filePath);
		}
	}

	/**
	 * @method  : resolve
	 * @desc :
	 * @author   : ytkim
	 * @date   : 2021. 1. 3.
	 * @param filePath
	 * @return
	 */
	public static Path getPath(String filePath) {
		return getUploadRootPath().resolve(filePath).normalize();
	}
}
