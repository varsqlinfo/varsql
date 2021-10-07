package com.varsql.web.app.admin.service;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.common.constants.PathType;
import com.varsql.core.common.util.ClassLoaderUtils;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.common.service.FileUploadService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.dto.db.DBTypeDriverProviderRequestDTO;
import com.varsql.web.dto.db.DBTypeDriverProviderResponseDTO;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.varsql.web.repository.db.DBTypeDriverEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverProviderRepository;
import com.varsql.web.repository.db.DBTypeEntityRepository;
import com.varsql.web.repository.spec.DBTypeDriverProviderSpec;
import com.varsql.web.repository.user.FileInfoEntityRepository;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.crypto.EncryptDecryptException;
import com.vartech.common.utils.StringUtils;

@Service
public class DriverProvierMgmtServiceImpl extends AbstractService {
	private final Logger logger = LoggerFactory.getLogger(DriverProvierMgmtServiceImpl.class);

	private DBTypeEntityRepository dbTypeEntityRepository;
	
	private DBTypeDriverProviderRepository dbTypeDriverProviderRepository;
	
	private DBTypeDriverEntityRepository dbTypeDriverModelRepository;
	
	private FileUploadService fileUploadService;
	
	private FileInfoEntityRepository fileInfoEntityRepository;
	
	public DriverProvierMgmtServiceImpl(DBTypeEntityRepository dbTypeEntityRepository, DBTypeDriverProviderRepository dbTypeDriverProviderRepository
			, DBTypeDriverEntityRepository dbTypeDriverModelRepository, FileUploadService fileUploadService, FileInfoEntityRepository fileInfoEntityRepository) {
		this.dbTypeEntityRepository = dbTypeEntityRepository; 
		this.dbTypeDriverProviderRepository = dbTypeDriverProviderRepository; 
		this.dbTypeDriverModelRepository = dbTypeDriverModelRepository; 
		this.fileUploadService = fileUploadService; 
		this.fileInfoEntityRepository = fileInfoEntityRepository; 
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
			entity.setDbType(dto.getDbType());
			entity.setDriverClass(dto.getDriverClass());
			entity.setDriverDesc(dto.getDriverDesc());
			entity.setValidationQuery(dto.getValidationQuery());
			entity.setDirectYn(dto.getDirectYn());
			
			entity.setPathType(dto.getPathType());
			entity.setDriverPath(dto.getDriverPath());
			
			String fileIds = dto.getRemoveFileIds();
			
			if(!StringUtils.isBlank(fileIds)) {
				fileInfoEntityRepository.deleteByIdInQuery(Arrays.asList(fileIds.split(",")).stream().map(item->{
					return item;
				}).collect(Collectors.toList()));
			}
		}
		
		entity = dbTypeDriverProviderRepository.save(entity);
		
		if(dto.getFile() !=null && dto.getFile().size() > 0) {
			fileUploadService.uploadFiles( UploadFileType.JDBC_DIRVER, dto.getFile(), entity.getDriverProviderId(), UploadFileType.JDBC_DIRVER.getDiv(), "file", true, true);
		}
		
		return VarsqlUtils.getResponseResultItemOne(1);
		
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
		 return VarsqlUtils.getResponseResultItemList(this.dbTypeDriverModelRepository.findByDbtype(dbtype));
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
			List<URL> resources = new ArrayList<>();			
			if(PathType.PATH.equals(PathType.getPathType(dto.getPathType()))){
				String[] pathArr = dto.getPathType().split(";");
				
				for (String pathStr : pathArr) {
					Resource resource= ResourceUtils.getResource(pathStr);
					
					if(resource != null) {
						resources.add(resource.getURL());
					}else {
						errorMsg.append(pathStr).append(";");
					}
				}
			}else {
				List<FileInfoEntity> driverJarFiles= fileInfoEntityRepository.findByFileContId(entity.getDriverProviderId());
				
				for(FileInfoEntity fie : driverJarFiles) {
					try {
						resources.add(FileServiceUtils.getFileInfoToURL(fie));
					}catch (Exception e) {
						errorMsg.append(fie.getFilePath()).append(";");
					}
				}
			}
			
			ClassLoaderUtils.getJdbcDriver(dto.getDriverClass(), resources.toArray(new URL[0]));
			
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
