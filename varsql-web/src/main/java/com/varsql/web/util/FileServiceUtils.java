package com.varsql.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.varsql.core.common.beans.FileInfo;
import com.varsql.core.common.constants.VarsqlConstants;
import com.vartech.common.utils.CommUtils;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.exception.FileNotFoundException;
import com.varsql.web.constants.SavePathType;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.exception.VarsqlAppException;
import com.varsql.web.model.entity.FileBaseEntity;
import com.vartech.common.app.beans.ClientInfo;
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
	
	private final static int BUFFER_SIZE = 2048;

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
	 * @Method Name  : getFileInfoToFile
	 * @Method 설명 : upload file 얻기
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31.
	 * @변경이력  :
	 * @param url
	 * @return
	 */
	public static File getFileInfoToFile(FileBaseEntity fileinfo) {
		return getUploadRootPath().resolve(fileinfo.getFilePath()).normalize().toFile();
	}

	public static URL getFileInfoToURL(FileBaseEntity fileinfo) throws MalformedURLException {
		return getUploadRootPath().resolve(fileinfo.getFilePath()).normalize().toUri().toURL();
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
	public static Path getSavePath(UploadFileType fileType) {
		return getSavePath(fileType, null);
	}

	public static Path getSavePath(UploadFileType fileType, String contId) {
		return getUploadRootPath().resolve(getSaveFilePathStr(fileType, contId, true));
	}

	public static String getSaveRelativePath(UploadFileType fileType) {
		return getSaveRelativePath(fileType, null);
	}
	public static String getSaveRelativePath(UploadFileType fileType, String contId) {
		return getSaveFilePathStr(fileType, contId, true);
	}

	private static String getSaveFilePathStr(UploadFileType fileType, String contId, boolean createFlag) {
		String savePath = "";
		if(SavePathType.DATE_YY_MM_DD.equals((fileType.getSavePathType()))) {
			savePath =  FileUtils.getSaveDayPath(fileType.getDiv());
		}else if(SavePathType.DATE_YY.equals((fileType.getSavePathType()))) {
			savePath = FileUtils.getSaveYearPath(fileType.getDiv());
		}else if(SavePathType.CONT_ID.equals((fileType.getSavePathType()))) {
			savePath = FileUtils.pathConcat(fileType.getDiv(), contId);
		}else if(SavePathType.BLANK.equals((fileType.getSavePathType()))) {
			savePath = fileType.getDiv();
		}else {
			savePath = FileUtils.getSaveMonthPath(fileType.getDiv());
		}

		if(createFlag) {
			createDirPath(getUploadRootPath().resolve(savePath));
		}

		return savePath;
	}

	private static void createDirPath(Path path) {
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new VarsqlAppException("Files.createDirectories exception  : "+ path.toAbsolutePath().toString(), e);
			}
		}
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

	/**
	 *
	 * @Method Name  : byteCalculation
	 * @Method 설명 : size 계산
	 * @작성자   : ytkim
	 * @작성일   : 2021. 7. 2.
	 * @변경이력  :
	 * @param size
	 * @return
	 */
	public static String byteCalculation(long size) {
		String retFormat = "0";

		String[] s = { "bytes", "KB", "MB", "GB", "TB", "PB" };

		if (size > 0) {
			int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
			DecimalFormat df = new DecimalFormat("#,###.##");
			double ret = ((size / Math.pow(1024, Math.floor(idx))));
			retFormat = df.format(ret) + " " + s[idx];
		} else {
			retFormat += " " + s[0];
		}

		return retFormat;
	}
	
	public static void fileDownload(HttpServletRequest req, HttpServletResponse res, String downFileName, File file) throws IOException {
		res.setContentType("application/octet-stream; "+VarsqlConstants.CHAR_SET);
		res.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";", getDownloadFileName(req, downFileName)));
		res.setHeader("Content-Transfer-Encoding", "binary;");
		res.setHeader("Pragma", "no-cache;");
		res.setHeader("Expires", "-1;");
		
		byte b[] = new byte[BUFFER_SIZE];

		if (file.isFile()) {
			try(BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream outs = new BufferedOutputStream(res.getOutputStream());){
				int read = 0;
				while ((read = fin.read(b)) != -1){
					outs.write(b,0,read);
				}

				if(fin != null) fin.close();
				if(outs != null) outs.close();
			}
		}
		
	}

	public static void fileDownload(HttpServletRequest req, HttpServletResponse res, String downFileName, FileBaseEntity ... fileBaseEntity) throws IOException {
		
		int fileLen = fileBaseEntity.length;

		if(fileLen > 1) {
			res.setContentType("application/octet-stream; "+VarsqlConstants.CHAR_SET);
			res.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";", getDownloadFileName(req, downFileName)));
			res.setHeader("Content-Transfer-Encoding", "binary;");
			res.setHeader("Pragma", "no-cache;");
			res.setHeader("Expires", "-1;");
			
			try(ZipOutputStream zos = new ZipOutputStream(res.getOutputStream(), Charset.forName(VarsqlConstants.CHAR_SET));){
				for(int idx=0; idx < fileLen; idx++){
					FileBaseEntity fileInfo = fileBaseEntity[idx];

					File file = getFileInfoToFile(fileInfo);

					if (file.isFile()) {

						String orginName = fileInfo.getFileName();

						try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){
							ZipEntry zentry = new ZipEntry(orginName);
					        zos.putNextEntry(zentry);

					        byte[] buffer = new byte[BUFFER_SIZE];
					        int cnt = 0;
					        while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
					            zos.write(buffer, 0, cnt);
					        }
					        zos.closeEntry();
						};
					}
				}
				if(zos != null) zos.close();
			}
		}else {
			fileDownload(req, res, downFileName, getFileInfoToFile(fileBaseEntity[0]));
		}
	}

	public static String getDownloadFileName(HttpServletRequest req, String downFileName) throws UnsupportedEncodingException {
		ClientInfo clientInfo = CommUtils.getClientPcInfo(req);

		if (CommUtils.isIE(clientInfo)) {
			downFileName = URLEncoder.encode(downFileName, "UTF-8").replaceAll("\\+", "%20");
		}else if(CommUtils.isFirefox(clientInfo)){
			downFileName = "\""+new String(downFileName.getBytes("UTF-8"), "ISO-8859-1")+"\"";
		}else if(CommUtils.isChrome(clientInfo)){
			downFileName = URLEncoder.encode(downFileName, "UTF-8").replaceAll("\\+", "%20");
		}else if(CommUtils.isSafari(clientInfo)){
			downFileName = "\""+new String(downFileName.getBytes("UTF-8"), "ISO-8859-1")+"\"";
		}else if(CommUtils.isOpera(clientInfo)){
			downFileName = "\""+new String(downFileName.getBytes("UTF-8"), "ISO-8859-1")+"\"";
		}else {
			downFileName = URLEncoder.encode(downFileName, "UTF-8").replaceAll("\\+", "%20");
		}

		return downFileName;
	}
	
	/**
	 * @method  : fileInfos
	 * @desc : file info 구하기
	 * @author   : ytkim
	 * @date   : 2022. 1. 15. 
	 * @param FilePaths
	 * @return
	 * @throws IOException
	 */
	public static List<FileInfo> getFileInfos(String[] FilePaths) throws IOException{
		List<FileInfo> driverJarFiles = new ArrayList<>();
		
		for (String path : FilePaths) {
			Resource resource= ResourceUtils.getResource(path);
			
			if (resource != null) {
				FileInfo fi = new FileInfo();
				fi.setName(resource.getFile().getName());
				fi.setPath(resource.getFile().getAbsolutePath());
				fi.setExt(FileUtils.extension(resource.getFile().getName()));
				driverJarFiles.add(fi);
			} else {
				throw new FileNotFoundException("file path not found : "+path);
			}
		}
		return driverJarFiles; 
	}
	
	/**
	 * @method  : fileInfos
	 * @desc : file base 정보로 file info 구하기.
	 * @author   : ytkim
	 * @date   : 2022. 1. 15. 
	 * @param fileBaseList
	 * @return
	 */
	public static List<FileInfo> getFileInfos(List<? extends FileBaseEntity> fileBaseList){
		 return fileBaseList.stream().map(fie->{
				FileInfo fi = new FileInfo();
				fi.setName(fie.getFileName());
				fi.setPath(Paths.get(VarsqlConstants.UPLOAD_PATH, new String[0]).toAbsolutePath().resolve(fie.getFilePath()).normalize().toAbsolutePath().toString());
				fi.setExt(fie.getFileExt());

				return fi;
		}).collect(Collectors.toList());
	}
}
