package com.varsql.web.app.admin.service;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.PathType;
import com.varsql.core.common.util.VarsqlJdbcUtil;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.connection.ConnectionInfoManager;
import com.varsql.core.connection.beans.JdbcURLFormatParam;
import com.varsql.core.crypto.PasswordCryptionFactory;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.util.JdbcUtils;
import com.varsql.web.common.cache.CacheUtils;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.db.DBConnectionRequestDTO;
import com.varsql.web.dto.db.DBConnectionResponseDTO;
import com.varsql.web.dto.db.DBTypeDTO;
import com.varsql.web.dto.db.DBTypeDriverProviderResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBTypeDriverEntity;
import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.varsql.web.model.mapper.db.DBConnectionDetailMapper;
import com.varsql.web.model.mapper.db.DBConnectionMapper;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.db.DBGroupMappingDbEntityRepository;
import com.varsql.web.repository.db.DBManagerEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverFileEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverProviderRepository;
import com.varsql.web.repository.db.DBTypeEntityRepository;
import com.varsql.web.repository.spec.DBConnectionSpec;
import com.varsql.web.repository.spec.DBTypeDriverProviderSpec;
import com.varsql.web.security.UserService;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlBeanUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.FileInfo;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.crypto.EncryptDecryptException;
import com.vartech.common.utils.PagingUtil;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

/**
 *
 * @FileName  : AdminDbMgmtServiceImpl.java
 * @Date      : 2014. 8. 18.
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class AdminDbMgmtServiceImpl extends AbstractService{
	private final Logger logger = LoggerFactory.getLogger(AdminDbMgmtServiceImpl.class);

	@Autowired
	private DBTypeEntityRepository dbTypeEntityRepository;

	@Autowired
	private DBConnectionEntityRepository dbConnectionEntityRepository;

	@Autowired
	private DBTypeDriverEntityRepository dbTypeDriverEntityRepository;

	@Autowired
	private DBGroupMappingDbEntityRepository dbGroupMappingDbEntityRepository;

	@Autowired
	private DBManagerEntityRepository dbManagerRepository;

	@Autowired
	@Qualifier(ResourceConfigConstants.USER_DETAIL_SERVICE)
	private UserService userService;

	@Autowired
	@Qualifier(ResourceConfigConstants.CACHE_MANAGER)
	private CacheManager cacheManager;

	@Autowired
	private DBTypeDriverFileEntityRepository dbTypeDriverFileEntityRepository;

	@Autowired
	private DBTypeDriverProviderRepository dbTypeDriverProviderRepository;

	/**
	 *
	 * @Method Name  : selectDblist
	 * @Method 설명 : db 목록 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectDblist(SearchParameter searchParameter) {

		Page<DBConnectionEntity> result = dbConnectionEntityRepository.findAll(
			DBConnectionSpec.getVnameOrVurl(searchParameter.getKeyword())
			, VarsqlUtils.convertSearchInfoToPage(searchParameter)
		);
		
		ResponseResult responseResult = new ResponseResult();
		responseResult.setList(result.stream().map(item->{
			DBConnectionResponseDTO dto = DBConnectionMapper.INSTANCE.toDto(item);
			dto.setStatus(ConnectionFactory.getInstance().getStatus(dto.getVconnid()).name());
			return dto;
		}).collect(Collectors.toList()));
		responseResult.setPage(PagingUtil.getPageObject(result.getTotalElements(), searchParameter));
		
		return responseResult;

	}

	public ResponseResult selectDbNameSearch(SearchParameter searchParameter) {
		Page<DBConnectionEntity> result = dbConnectionEntityRepository.findAll(
			DBConnectionSpec.getVname(searchParameter.getKeyword())
			, VarsqlUtils.convertSearchInfoToPage(searchParameter)
		);

		return VarsqlUtils.getResponseResult(result, searchParameter, DBConnectionMapper.INSTANCE);
	}

	/**
	 *
	 * @Method Name  : selectDetailObject
	 * @Method 설명 : db 정보 상세.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22.
	 * @변경이력  :
	 * @param vconnid
	 * @return
	 */
	public ResponseResult selectDetailObject(String vconnid) {
		DBConnectionEntity entity= dbConnectionEntityRepository.findOne(DBConnectionSpec.detailInfo(vconnid)).get();

		if( entity == null) {
			return VarsqlUtils.getResponseResultItemOne(null);
		}else {
			return VarsqlUtils.getResponseResultItemOne(DBConnectionDetailMapper.INSTANCE.toDto(entity));
		}

	}

	/**
	 *
	 * @Method Name  : connectionCheck
	 * @Method 설명 : 커넥션 체크.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22.
	 * @변경이력  :
	 * @param vtConnection
	 * @return
	 * @throws EncryptDecryptException
	 */
	public ResponseResult connectionCheck(DBConnectionRequestDTO vtConnection) throws EncryptDecryptException {

		ResponseResult resultObject = new ResponseResult();

		logger.debug("connection check object param :  {}", VartechUtils.reflectionToString(vtConnection));

		String username = vtConnection.getVid();

		DBConnectionEntity dbInfo = this.dbConnectionEntityRepository.findOne(DBConnectionSpec.detailInfo(vtConnection.getVconnid())).orElseThrow(NullPointerException::new);

		DBTypeDriverProviderEntity driverProviderEntity = dbInfo.getDbTypeDriverProvider();

		String url = vtConnection.getVurl();

		String defaultDriverValidationQuery = ValidationProperty.getInstance().validationQuery(driverProviderEntity.getDbType());
		if(!"Y".equals(driverProviderEntity.getDirectYn())){
			DBTypeDriverEntity dbDriverModel = this.dbTypeDriverEntityRepository.findByDriverId(driverProviderEntity.getDriverId());
			if (!"Y".equals(vtConnection.getUrlDirectYn())) {
				url = VarsqlJdbcUtil.getJdbcUrl(dbDriverModel.getUrlFormat(), JdbcURLFormatParam.builder()
						.serverIp(vtConnection.getVserverip())
						.port(vtConnection.getVport())
						.databaseName( vtConnection.getVdatabasename())
						.build());
			}

			defaultDriverValidationQuery = StringUtils.isBlank(dbDriverModel.getValidationQuery()) ? defaultDriverValidationQuery : dbDriverModel.getValidationQuery();
		}

		logger.debug("connection check url :  {}", url);

		String  validation_query = driverProviderEntity.getValidationQuery();

		validation_query = StringUtils.isBlank(validation_query) ? defaultDriverValidationQuery : validation_query;

		String failMessage = "";
		VarsqlAppCode resultCode = VarsqlAppCode.SUCCESS;
		try {
			String pwd = vtConnection.getVpw();
			
			if (StringUtils.isBlank(pwd)) {
				pwd = StringUtils.isBlank(dbInfo.getVpw()) ? "" : PasswordCryptionFactory.getInstance().decrypt(dbInfo.getVpw());
			}
			
			Properties p = new Properties();
			p.setProperty("user", username);
			p.setProperty("password", pwd);

			List<FileInfo> driverJarFiles;

			if(PathType.PATH.equals(PathType.getPathType(driverProviderEntity.getPathType()))){
				driverJarFiles = FileServiceUtils.getFileInfos(driverProviderEntity.getDriverPath().split(";"));
			}else {
				driverJarFiles = FileServiceUtils.getFileInfos(dbTypeDriverFileEntityRepository.findByFileContId(driverProviderEntity.getDriverProviderId()));
			}
			
		    JdbcUtils.connectionCheck(dbInfo.getDbTypeDriverProvider().getDriverProviderId()
		    		, dbInfo.getDbTypeDriverProvider().getDriverClass()
		    		, driverProviderEntity.getDbType(), url, p, driverJarFiles, validation_query, 10, 5);
		    
		} catch (ClassNotFoundException e) {
			resultCode = VarsqlAppCode.ERROR;
			failMessage = e.getMessage();
			logger.error("url :{}", url);
			logger.error(getClass().getName(), e);
		}catch (Exception e) {
			resultCode = VarsqlAppCode.ERROR;
			failMessage = e.getMessage();
			logger.error(getClass().getName(), e);
		}
		
		resultObject.setResultCode(resultCode);
		resultObject.setMessage(failMessage);
		return resultObject;
	}

	/**
	 *
	 * @Method Name  : insertVtconnectionInfo
	 * @Method 설명 : 정보 저장
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22.
	 * @변경이력  :
	 * @param vtConnection
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws BeansException
	 */
	
	public ResponseResult saveVtconnectionInfo(DBConnectionRequestDTO vtConnection) throws BeansException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ResponseResult resultObject = new ResponseResult();
		
		vtConnection.setUserId(SecurityUtil.userViewId());

		DBConnectionEntity reqEntity = vtConnection.toEntity();
		DBConnectionEntity saveEntity = null;
		DBConnectionEntity currentEntity = null;
		boolean updateFlag = false;
		if (reqEntity.getVconnid() != null) {
			currentEntity = this.dbConnectionEntityRepository.findByVconnid(reqEntity.getVconnid());
			if (currentEntity != null) {
				saveEntity = new DBConnectionEntity();
				BeanUtils.copyProperties(currentEntity, saveEntity);
				updateFlag = true;
				VarsqlBeanUtils.copyNonNullProperties(reqEntity, saveEntity, new String[] { DBConnectionEntity.VPW });
			}
		}
		
		if (!updateFlag) {
			saveEntity = reqEntity;
		}
		
		if (vtConnection.isPasswordChange()) {
			saveEntity.setVpw(PasswordCryptionFactory.getInstance().encrypt(vtConnection.getVpw()));
		}
		
		// 2024.08.20 schema 사용 안함. 
		// 접속한 스키마 사용하기 위해서 사용하지 않음. 
		saveEntity.setVdbschema("");
		
		if(StringUtils.isBlank(saveEntity.getVdatabasename())) {
			saveEntity.setVdatabasename("");
		}
		
		this.dbConnectionEntityRepository.saveAndFlush(saveEntity);
		
		if (updateFlag) {
			if (!reqEntity.getVdbschema().equalsIgnoreCase(currentEntity.getVdbschema()) 
					|| !reqEntity.getVdatabasename().equalsIgnoreCase(currentEntity.getVdatabasename())
					||"Y".equals(reqEntity.getUrlDirectYn()) && !reqEntity.getVurl().equals(currentEntity.getVurl())
					||(!reqEntity.getVserverip().equals(currentEntity.getVserverip())
							|| reqEntity.getVport() != currentEntity.getVport())
					) {
				CacheUtils.removeObjectCache(this.cacheManager, currentEntity);
			}
			currentEntity = null; 
			// 접속 정보 reload
			ConnectionInfoManager.getInstance().getConnectionInfo(saveEntity.getVconnid(), true);
		}
		
		return resultObject;
	}
	
	/**
	 * @method  : deleteVtconnectionInfo
	 * @desc : db 정보 삭제 처리.
	 * @author   : ytkim
	 * @date   : 2020. 4. 20.
	 * @param vconnid
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public boolean deleteVtconnectionInfo(String vconnid) {

		DBConnectionEntity dbInfo = dbConnectionEntityRepository.findOne(DBConnectionSpec.detailInfo(vconnid)).orElseThrow(NullPointerException::new);
		dbInfo.setUseYn("N");
		dbInfo.setDelYn(true);
		dbInfo.setVname("[Delete] "+dbInfo.getVname());

		dbConnectionEntityRepository.save(dbInfo);
		dbGroupMappingDbEntityRepository.deleteByVconnid(vconnid);
		dbManagerRepository.deleteByVconnid(vconnid);

		try {
			ConnectionFactory.getInstance().poolShutdown(vconnid);
		} catch (Exception e) {
			logger.error("deleteVtconnectionInfo vconnid :  {}" , vconnid, e);
		}finally {
			CacheUtils.removeObjectCache(cacheManager, vconnid);
		}

		return true;
	}

	public List<DBTypeDTO> selectAllDbType() {
		return dbTypeEntityRepository.findAll().stream()
                .map(item -> DBTypeDTO.toDto(item))
                .collect(Collectors.toList());
	}

	public Map<String,String> dbTypeUrlFormat() {

		Map<String,String> urlFormat = new HashMap<String,String>();
		this.dbTypeDriverEntityRepository.findAll().forEach(item->{
			urlFormat.put(item.getDriverId(), VarsqlJdbcUtil.getSampleJdbcUrl(item.getUrlFormat() , JdbcURLFormatParam.builder()
					.type(item.getDbtype())
					.port(item.getDefaultPort())
					.build()) );
		});;

		return urlFormat;
	}

	/**
	 *
	 * @Method Name  : selectDbDriverList
	 * @Method 설명 : db driver list
	 * @작성자   : ytkim
	 * @작성일   : 2017. 5. 25.
	 * @변경이력  :
	 * @param dbType
	 * @return
	 */
	public ResponseResult selectDbDriverList(String dbType) {
		return VarsqlUtils.getResponseResultItemList(dbTypeDriverEntityRepository.findByDbtype(dbType));
	}

	/**
	 * @method  : connectionClose
	 * @desc : db 연결 전부 닫기
	 * @author   : ytkim
	 * @date   : 2020. 5. 24.
	 * @param vconnid
	 * @return
	 */
	public ResponseResult connectionClose(String vconnid) {
		ResponseResult resultObject = new ResponseResult();
		try {
			ConnectionFactory.getInstance().poolShutdown(vconnid);
		} catch (Exception e) {
			resultObject.setResultCode(RequestResultCode.ERROR);
			resultObject.setMessage(e.getMessage());
		}finally {
			CacheUtils.removeObjectCache(cacheManager, vconnid);
		}

		return resultObject;
	}

	/**
	 * @method  : dbConnectionReset
	 * @desc : db connection pool 초기화
	 * @author   : ytkim
	 * @date   : 2020. 7. 4.
	 * @param vconnid
	 * @return ResponseResult
	 */
	public ResponseResult dbConnectionReset(String vconnid) {
		ResponseResult resultObject = new ResponseResult();
		try {
			ConnectionFactory.getInstance().resetConnectionPool(vconnid);
		} catch (Exception e) {
			resultObject.setResultCode(RequestResultCode.ERROR);
			resultObject.setMessage(e.getMessage());
		}finally {
			CacheUtils.removeObjectCache(cacheManager, vconnid);
		}
		return resultObject;
	}

	/**
	 * @method  : viewVtConntionPwInfo
	 * @desc : view db password
	 * @author   : ytkim
	 * @date   : 2020. 7. 4.
	 * @param vconnid
	 * @return ResponseResult
	 */
	public ResponseResult viewVtConntionPwInfo(String vconnid, String userPw)  {
		ResponseResult resultObject = new ResponseResult();
		if(!userService.passwordCheck(SecurityUtil.loginName(), userPw)) {
			resultObject.setResultCode(RequestResultCode.ERROR);
			resultObject.setMessage("password not valid");
			return resultObject;
		}

		DBConnectionEntity dbInfo = dbConnectionEntityRepository.findOne(DBConnectionSpec.detailInfo(vconnid)).orElseThrow(NullPointerException::new);
		try {
			resultObject.setItemOne(PasswordCryptionFactory.getInstance().decrypt(dbInfo.getVpw()));
		}catch(VarsqlRuntimeException e) {
			resultObject.setItemOne("password decrypt error");
		}
		return resultObject;
	}

	/**
	 * @method  : dbConnectionCopy
	 * @desc : view connection info copy
	 * @author   : ytkim
	 * @date   : 2020. 7. 4.
	 * @param vconnid
	 * @return ResponseResult
	 */
	public ResponseResult dbConnectionCopy(String vconnid) {
		DBConnectionEntity dbInfo = dbConnectionEntityRepository.findOne(DBConnectionSpec.detailInfo(vconnid)).orElseThrow(NullPointerException::new);

		DBConnectionEntity copyEntity = VarsqlBeanUtils.copyEntity(dbInfo);

		copyEntity.setVconnid(null);
		copyEntity.setVname(copyEntity.getVname()+"-copy");

		dbConnectionEntityRepository.save(copyEntity);

		return VarsqlUtils.getResponseResultItemOne(copyEntity.getVconnid());
	}

	public ResponseResult jdbcProviderList(SearchParameter searchParameter) {
		Sort sort =Sort.by(Sort.Direction.ASC, DBTypeDriverProviderEntity.DB_TYPE).and(Sort.by(Sort.Direction.ASC, DBTypeDriverProviderEntity.PROVIDER_NAME));

		List<DBTypeDriverProviderEntity> result = dbTypeDriverProviderRepository.findAll(DBTypeDriverProviderSpec.searchField(searchParameter), sort);

		List<DBTypeDriverProviderResponseDTO> list= result.stream().map(item ->{
			DBTypeDriverProviderResponseDTO dto = new DBTypeDriverProviderResponseDTO();
			dto.setDbType(item.getDbType());
			dto.setProviderName(item.getProviderName());
			dto.setDriverProviderId(item.getDriverProviderId());
			dto.setDriverId(item.getDriverId());
			dto.setDirectYn(item.getDirectYn());
			dto.setVersionList(MetaControlFactory.getDbInstanceFactory(DBVenderType.getDBType(item.getDbType())).getVenderVersionInfo());
			return dto;
		}).collect(Collectors.toList());

		return VarsqlUtils.getResponseResultItemList(list);
	}
}