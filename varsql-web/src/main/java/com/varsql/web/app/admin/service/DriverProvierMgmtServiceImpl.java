package com.varsql.web.app.admin.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vartech.common.app.beans.FileInfo;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.common.constants.PathType;
import com.varsql.core.common.util.JdbcDriverLoader;
import com.varsql.core.connection.beans.JDBCDriverInfo;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.dto.db.DBTypeDriverProviderRequestDTO;
import com.varsql.web.dto.db.DBTypeDriverProviderResponseDTO;
import com.varsql.web.model.entity.db.DBTypeDriverEntity;
import com.varsql.web.model.entity.db.DBTypeDriverFileEntity;
import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.varsql.web.repository.db.DBTypeDriverEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverFileEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverProviderRepository;
import com.varsql.web.repository.db.DBTypeEntityRepository;
import com.varsql.web.repository.spec.DBTypeDriverProviderSpec;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.crypto.EncryptDecryptException;
import com.vartech.common.utils.FileUtils;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

@Service
public class DriverProvierMgmtServiceImpl extends AbstractService {
	private final Logger logger = LoggerFactory.getLogger(DriverProvierMgmtServiceImpl.class);

	private DBTypeEntityRepository dbTypeEntityRepository;
	
	private DBTypeDriverProviderRepository dbTypeDriverProviderRepository;
	
	private DBTypeDriverEntityRepository dbTypeDriverModelRepository;
	
	private DBTypeDriverFileEntityRepository dbTypeDriverFileEntityRepository;
	
	public DriverProvierMgmtServiceImpl(DBTypeEntityRepository dbTypeEntityRepository, DBTypeDriverProviderRepository dbTypeDriverProviderRepository
			, DBTypeDriverEntityRepository dbTypeDriverModelRepository, DBTypeDriverFileEntityRepository dbTypeDriverFileEntityRepository) {
		this.dbTypeEntityRepository = dbTypeEntityRepository; 
		this.dbTypeDriverProviderRepository = dbTypeDriverProviderRepository; 
		this.dbTypeDriverModelRepository = dbTypeDriverModelRepository; 
		this.dbTypeDriverFileEntityRepository = dbTypeDriverFileEntityRepository; 
	}

	public ResponseResult list(SearchParameter searchParameter) {
		Sort sort =Sort.by(Sort.Direction.DESC, DBTypeDriverProviderEntity.REG_DT).and(Sort.by(Sort.Direction.DESC, DBTypeDriverProviderEntity.PROVIDER_NAME));
		
		Page<DBTypeDriverProviderEntity> result = dbTypeDriverProviderRepository.findAll(
				DBTypeDriverProviderSpec.searchField(searchParameter)
				, VarsqlUtils.convertSearchInfoToPage(searchParameter, sort));
		
		List<DBTypeDriverProviderResponseDTO> list= result.stream().map(item ->{
			DBTypeDriverProviderResponseDTO dto = new DBTypeDriverProviderResponseDTO();
			dto.setDbType(item.getDbType());
			dto.setProviderName(item.getProviderName());
			dto.setDriverClass(item.getDriverClass());
			dto.setPathType(item.getPathType());
			dto.setDriverProviderId(item.getDriverProviderId());
			return dto;
		}).collect(Collectors.toList());

		return VarsqlUtils.getResponseResult(list, result.getTotalElements(),searchParameter);
	}

	public ResponseResult detail(String driverProviderId) {
		
		DBTypeDriverProviderEntity entity = dbTypeDriverProviderRepository.findByDriverProviderId(driverProviderId);
		if(entity == null) {
			ResponseResult resultObject = new ResponseResult();
			resultObject.setResultCode(RequestResultCode.NOT_FOUND);
			resultObject.setMessage("db jdbc provider not found : "+ driverProviderId);
			return resultObject;
		}
		
		DBTypeDriverProviderResponseDTO dto = DBTypeDriverProviderResponseDTO.detailDto(entity);
		
		return VarsqlUtils.getResponseResultItemOne(dto);
	}

	@Transactional(transactionManager=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Throwable.class)
	public ResponseResult saveInfo(DBTypeDriverProviderRequestDTO dto)
			throws BeansException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		ResponseResult resultObject = new ResponseResult();
		
		String driverProviderId =  dto.getDriverProviderId();
		
		DBTypeDriverProviderEntity entity;
		if(StringUtils.isBlank(driverProviderId)) {
			entity = dto.toEntity();
		}else {
			entity = dbTypeDriverProviderRepository.findByDriverProviderId(driverProviderId);
			if(entity == null) {
				logger.debug("update jdbc provider info : {}", dto);
				resultObject.setResultCode(RequestResultCode.NOT_FOUND);
				resultObject.setMessage("db jdbc provider not found : "+ driverProviderId);
				return resultObject;
			}
			
			entity.setProviderName(dto.getProviderName());
			entity.setDriverId(dto.getDriverId());
			entity.setDbType(dto.getDbType());
			entity.setDriverClass(dto.getDriverClass());
			entity.setDriverDesc(dto.getDriverDesc());
			entity.setValidationQuery(dto.getValidationQuery());
			entity.setDirectYn(dto.getDirectYn());
			
			entity.setPathType(dto.getPathType());
			entity.setDriverPath(dto.getDriverPath());
			
			String fileIds = dto.getRemoveFileIds();
			
			if(!StringUtils.isBlank(fileIds)) {
				dbTypeDriverFileEntityRepository.deleteByIdInQuery(Arrays.asList(fileIds.split(",")).stream().map(item->{
					return item;
				}).collect(Collectors.toList()));
			}
		}
		
		entity = dbTypeDriverProviderRepository.save(entity);
		
		if(dto.getFile() !=null && dto.getFile().size() > 0) {
			uploadDriverFiles(dto.getFile(), entity.getDriverProviderId());
		}
		
		return VarsqlUtils.getResponseResultItemOne(1);
		
	}
	
	private List<DBTypeDriverFileEntity> uploadDriverFiles( List<MultipartFile> files, String fileContId) {
		
		final UploadFileType fileType = UploadFileType.JDBC_DIRVER;
		List<DBTypeDriverFileEntity> fileInfos = new ArrayList<DBTypeDriverFileEntity>();
		files.forEach(file ->{
			if(!(file.getSize() <= 0 && "blob".equals(file.getOriginalFilename()))) {
				try {
					
					String filePath = FileServiceUtils.getSaveRelativePath(fileType, fileContId);
					
					// 파일 원본명
					String fileName = FileUtils.normalize(file.getOriginalFilename());
					// 파일 확장자 구하기
					String extension = FileUtils.extension(fileName);
					
					String fileId = VartechUtils.generateUUID();
					
					if(fileType.isOrginFileName()) {
						filePath = FileUtils.pathConcat(filePath, fileName);
					}else {
						filePath = FileUtils.pathConcat(filePath, String.format("%s.%s",  fileId, extension));
					}
					
					Path createFilePath = FileServiceUtils.getPath(filePath);
					file.transferTo(createFilePath);
					
					fileInfos.add(DBTypeDriverFileEntity.builder()
							.fileDiv(fileType.getDiv())
							.fileId(fileId)
							.fileName(fileName)
							.fileSize(file.getSize())
							.fileExt(extension)
							.fileContId(fileContId)
							.filePath(filePath).build());
				} catch (IllegalStateException | IOException e) {
					logger.error("file upload exception : {}", e.getMessage(), e);
					throw new VarsqlRuntimeException(VarsqlAppCode.COMM_FILE_UPLOAD_ERROR, "file upload error", e);
				}
			}
		});
		
		dbTypeDriverFileEntityRepository.saveAll(fileInfos);
		
		return fileInfos;
	}

	@Transactional(value = "transactionManager", rollbackFor = { Exception.class })
	public ResponseResult deleteInfo(String driverProviderId) {
		logger.debug("delete jdbc provider id : {}", driverProviderId);
		
		ResponseResult resultObject = new ResponseResult();
		
		DBTypeDriverProviderEntity entity = dbTypeDriverProviderRepository.findByDriverProviderId(driverProviderId);
		
		if(entity == null) {
			logger.debug("delete driver id not found : {}", driverProviderId);
			resultObject.setResultCode(RequestResultCode.NOT_FOUND);
			resultObject.setMessage("db jdbc provider not found : "+ driverProviderId);
			return resultObject;
		}
		
		dbTypeDriverProviderRepository.delete(entity);
		resultObject.setItemOne(1);
		
		return resultObject;
	}

	public ResponseResult dbTypeList() {
		return VarsqlUtils.getResponseResultItemList(dbTypeEntityRepository.findAll());
	}

	public ResponseResult driverList(String dbtype) {
		Sort sort =Sort.by(Sort.Direction.DESC, DBTypeDriverEntity.DRIVER_ID);
		
		return VarsqlUtils.getResponseResultItemList(this.dbTypeDriverModelRepository.findByDbtype(dbtype, sort));
	}
	
	public ResponseResult driverCheck(DBTypeDriverProviderRequestDTO dto) throws EncryptDecryptException {
		ResponseResult resultObject = new ResponseResult();
		
		String driverProviderId =  dto.getDriverProviderId();
		
		DBTypeDriverProviderEntity entity = dbTypeDriverProviderRepository.findByDriverProviderId(driverProviderId); 
		
		if(entity == null) {
			resultObject.setResultCode(RequestResultCode.NOT_FOUND);
			resultObject.setMessage("db jdbc provider not found : "+ driverProviderId);
			return resultObject;
		}
		
	    StringBuffer errorMsg = new StringBuffer();
	    
		try {
			List<FileInfo> driverJarFiles;
			
			if(PathType.PATH.equals(PathType.getPathType(dto.getPathType()))){
				driverJarFiles = FileServiceUtils.getFileInfos(dto.getDriverPath().split(";"));
			}else {
				driverJarFiles = FileServiceUtils.getFileInfos(dbTypeDriverFileEntityRepository.findByFileContId(entity.getDriverProviderId()));
			}
			
			JDBCDriverInfo jdbcDriverInfo =	JDBCDriverInfo.builder()
				.driverId(dto.getDriverProviderId())
				.driverClass(dto.getDriverClass())
				.driverFiles(driverJarFiles)
				.build();

			if (JdbcDriverLoader.checkDriver(jdbcDriverInfo) != null) {
				resultObject.setResultCode(VarsqlAppCode.SUCCESS);
			} else {
				resultObject.setMessage("driver null");
				resultObject.setResultCode(VarsqlAppCode.ERROR);
			}
			
			resultObject.setResultCode(VarsqlAppCode.SUCCESS);
		} catch (ClassNotFoundException e) {
			resultObject.setResultCode(VarsqlAppCode.ERROR);
			resultObject.setMessage("class not found : " + dto.getDriverClass() + "\nerror message : "+e.getMessage());
			logger.error(getClass().getName(), e);
		} catch (Exception e) {
			resultObject.setResultCode(VarsqlAppCode.ERROR);
			resultObject.setMessage( e.getMessage()+BlankConstants.NEW_LINE+errorMsg);
			logger.error(getClass().getName(), e);
		} 
		
		return resultObject;
	}
}
