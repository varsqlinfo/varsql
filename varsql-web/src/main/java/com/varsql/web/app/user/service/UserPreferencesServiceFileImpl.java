package com.varsql.web.app.user.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.core.exception.FileNotFoundException;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.spec.FileInfoSpec;
import com.varsql.web.repository.user.FileInfoEntityRepository;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.IOUtils;
import com.vartech.common.utils.StringUtils;

@Service
public class UserPreferencesServiceFileImpl extends AbstractService {
	private final Logger logger = LoggerFactory.getLogger(UserPreferencesServiceFileImpl.class);
	
	private final int MAX_LINE = 5000;

	@Autowired
	private FileInfoEntityRepository fileInfoEntityRepository;

	public ResponseResult importFileList(UploadFileType fileType, String vconnid, SearchParameter searchParameter) {
		Page<FileInfoEntity> result = fileInfoEntityRepository.findAll(
				FileInfoSpec.fileTypeSearch(fileType, SecurityUtil.userViewId(), vconnid, searchParameter),
				VarsqlUtils.convertSearchInfoToPage(searchParameter));
		
		List<Map> fileList = new ArrayList<>();
	    result.getContent().forEach(entity -> {
	    	Map item = new HashMap();
	    	
	    	item.put("fileId", entity.getFileId());
	    	item.put("fileName", entity.getFileName());
	    	item.put("ext", entity.getFileExt());
	    	item.put("regDt", entity.getRegDt());
	    	item.put("fileSize", entity.getFileSize());
	    	
	    	fileList.add(item);
	    });
		return VarsqlUtils.getResponseResult(fileList, result.getTotalElements(), searchParameter);
	}

	public ResponseResult detail(String fileId) {
		FileInfoEntity entity = fileInfoEntityRepository.findByFileId(fileId);
		
		if(entity== null) {
			new FileNotFoundException("fileId not found : "+ fileId);
		}
		
		if(UploadFileType.EXPORT.equals(UploadFileType.getDivType(entity.getFileDiv()))){
			Map item;
			
			List<Map> fileList = new ArrayList<>();
			try (ZipFile zipFile = new ZipFile(FileServiceUtils.getFileInfoToFile(entity))){
				final Enumeration<? extends ZipEntry> entries = zipFile.entries();
				ZipEntry entry;
				
				while(entries.hasMoreElements()) {
					entry = entries.nextElement();
					
					item = new HashMap();
					
					item.put("fileName", entry.getName());
			    	item.put("fileSize", entry.getSize());
			    	item.put("compressFileSize", entry.getCompressedSize());
			    	item.put("updDt", VarsqlDateUtils.format(VarsqlConstants.TIMESTAMP_FORMAT, entry.getLastModifiedTime().toMillis()));
					
					fileList.add(item);
				}
				IOUtils.close(zipFile);
			}catch(Exception e){
				logger.error("detail fileId : {}", fileId, e);
			}
			
			return VarsqlUtils.getResponseResultItemList(fileList);
			
		}else if(UploadFileType.IMPORT.equals(UploadFileType.getDivType(entity.getFileDiv()))){
			StringBuffer sb = new StringBuffer();
			
			Map item = new HashMap();
			
			try(InputStreamReader isr = new InputStreamReader(new FileInputStream(FileServiceUtils.getFileInfoToFile(entity)));
				BufferedReader	br = new BufferedReader(isr);){
							
				String read_data ="";
				int lineCount = 0;
				
				while((read_data =br.readLine()) != null){
					if(lineCount >= MAX_LINE) {
						break; 
					}
					lineCount++;
					sb.append(read_data).append(BlankConstants.NEW_LINE);
				}
				
				item.put("lineCount", lineCount);
				item.put("content", sb.toString());
				
				IOUtils.close(br);
				IOUtils.close(isr);
			}catch(Exception e){
				logger.error("detail fileId : {}", fileId, e);
			}
			
			return VarsqlUtils.getResponseResultItemOne(item);
		}
				
		return VarsqlUtils.getResponseResultItemOne(null); 
	}
	
	public ResponseResult zipFileDetail(String fileId, String fileName) {
		
		FileInfoEntity entity = fileInfoEntityRepository.findByFileId(fileId);
		
		if(entity== null) {
			new FileNotFoundException("fileId not found : "+ fileId);
		}
		
		StringBuffer sb = new StringBuffer();
		
		try (ZipFile zipFile = new ZipFile(FileServiceUtils.getFileInfoToFile(entity))){
			
			ZipEntry entry = zipFile.getEntry(fileName);
			
			if(entry.getName().equals(fileName)) {
			
				try(InputStreamReader isr = new InputStreamReader(zipFile.getInputStream(entry));
					BufferedReader	br = new BufferedReader(isr);){
								
					String read_data ="";
					int lineCount = 0;
					
					while((read_data =br.readLine()) != null){
						if(lineCount >= MAX_LINE) {
							break; 
						}
						lineCount++;
						sb.append(read_data).append(BlankConstants.NEW_LINE);
					}
					
					IOUtils.close(br);
					IOUtils.close(isr);
				}
			}
			
			IOUtils.close(zipFile);
		}catch(Exception e){
			logger.error("detail fileId : {}", fileId, e);
		}
		
		return VarsqlUtils.getResponseResultItemOne(sb.toString());
	}

	/**
	 *
	 * @Method Name  : deleteSqlFile
	 * @Method 설명 : sql file 삭제
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 7.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult deleteFiles(String selectItem) {

		logger.debug("deleteSqlFiles : {}" , selectItem);

		String[] fileIdArr = StringUtils.split(selectItem,",");
		
		List<FileInfoEntity> fileInfos = fileInfoEntityRepository.findAll(FileInfoSpec.findIds(SecurityUtil.userViewId(), fileIdArr));
		
		fileInfos.stream().forEach(fileInfo->{
			File file = FileServiceUtils.getFileInfoToFile(fileInfo);
			if (file.exists()) {
				file.delete();
			}
		});

		fileInfoEntityRepository.deleteAll(fileInfos);

		return VarsqlUtils.getResponseResultItemOne(1);
	}
}
