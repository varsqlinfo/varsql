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
import org.springframework.transaction.annotation.Transactional;

import com.vartech.common.app.beans.FileInfo;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.PathType;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.VarsqlJdbcUtil;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.ConnectionFactory;
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
import com.varsql.web.dto.db.DBTypeDriverProviderResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBTypeDriverEntity;
import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.varsql.web.model.entity.db.DBTypeEntity;
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
import com.varsql.web.util.VarsqlBeanUtils;
import com.varsql.web.util.VarsqlUtils;
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
	private DBTypeEntityRepository dbTypeModelRepository;

	@Autowired
	private DBConnectionEntityRepository dbConnectionModelRepository;

	@Autowired
	private DBTypeDriverEntityRepository dbTypeDriverModelRepository;

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

		Page<DBConnectionEntity> result = dbConnectionModelRepository.findAll(
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
		Page<DBConnectionEntity> result = dbConnectionModelRepository.findAll(
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
		DBConnectionEntity entity= dbConnectionModelRepository.findOne(DBConnectionSpec.detailInfo(vconnid)).get();

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

		this.dbConnectionModelRepository.findByConnInfo(vtConnection.getVconnid());

		logger.debug("connection check object param :  {}", VartechUtils.reflectionToString(vtConnection));

		String username = vtConnection.getVid();

		DBConnectionEntity dbInfo = this.dbConnectionModelRepository.findOne(DBConnectionSpec.detailInfo(vtConnection.getVconnid())).orElseThrow(NullPointerException::new);

		DBTypeDriverProviderEntity driverProviderEntity = dbInfo.getDbTypeDriverProvider();

		String url = vtConnection.getVurl();

		String defaultDriverValidationQuery = ValidationProperty.getInstance().validationQuery(driverProviderEntity.getDbType());
		if(!"Y".equals(driverProviderEntity.getDirectYn())){
			DBTypeDriverEntity dbDriverModel = this.dbTypeDriverModelRepository.findByDriverId(driverProviderEntity.getDriverId());
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
			if (pwd == null || "".equals(pwd))
				pwd = dbInfo.getVpw();
			pwd = (pwd == null) ? "" : pwd;
			pwd = PasswordCryptionFactory.getInstance().decrypt(pwd);
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
		    		, driverProviderEntity.getDbType(), url, p, driverJarFiles, validation_query, 30, 20);
		    
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

		DBTypeDriverProviderEntity driverProviderEntity = dbTypeDriverProviderRepository.findByDriverProviderId(vtConnection.getVdriver());

		DBTypeDriverEntity dbDriverModel = this.dbTypeDriverModelRepository.findByDriverId(driverProviderEntity.getDriverId());

		String schemeType = dbDriverModel.getSchemaType();
		if ("user".equals(schemeType)) {
			vtConnection.setVdbschema(vtConnection.getVid().toUpperCase());
		} else if ("orginUser".equals(schemeType)) {
			vtConnection.setVdbschema(vtConnection.getVid());
		} else if ("db".equals(schemeType)) {
			vtConnection.setVdbschema(vtConnection.getVdatabasename());
		} else {
			vtConnection.setVdbschema(schemeType);
		}
		DBConnectionEntity reqEntity = vtConnection.toEntity();
		DBConnectionEntity saveEntity = null;
		DBConnectionEntity currentEntity = null;
		boolean updateFlag = false;
		if (reqEntity.getVconnid() != null) {
			currentEntity = this.dbConnectionModelRepository.findByVconnid(reqEntity.getVconnid());
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
			saveEntity.setVpw(vtConnection.getVpw());
		}
			
		logger.debug("saveVtconnectionInfo object param :  {}", VartechUtils.reflectionToString(saveEntity));
		this.dbConnectionModelRepository.save(saveEntity);
		
		if (updateFlag) {
			if (!reqEntity.getVdbschema().equalsIgnoreCase(currentEntity.getVdbschema())) {
				CacheUtils.removeObjectCache(this.cacheManager, currentEntity);
			} else if (!reqEntity.getVdatabasename().equalsIgnoreCase(currentEntity.getVdatabasename())) {
				CacheUtils.removeObjectCache(this.cacheManager, currentEntity);
			} else if ("Y".equals(reqEntity.getUrlDirectYn()) && !reqEntity.getVurl().equals(currentEntity.getVurl())) {
				CacheUtils.removeObjectCache(this.cacheManager, currentEntity);
			} else if (!reqEntity.getVserverip().equals(currentEntity.getVserverip())
					|| reqEntity.getVport() != currentEntity.getVport()) {
				CacheUtils.removeObjectCache(this.cacheManager, currentEntity);
			}
		}
		
		resultObject.setItemOne(Integer.valueOf(1));
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

		DBConnectionEntity dbInfo = dbConnectionModelRepository.findOne(DBConnectionSpec.detailInfo(vconnid)).orElseThrow(NullPointerException::new);
		dbInfo.setDelYn(true);

		dbConnectionModelRepository.save(dbInfo);
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

	public List<DBTypeEntity> selectAllDbType() {
		return dbTypeModelRepository.findAll();
	}

	public Map<String,String> dbTypeUrlFormat() {

		Map<String,String> urlFormat = new HashMap<String,String>();
		this.dbTypeDriverModelRepository.findAll().forEach(item->{
			urlFormat.put(item.getDriverId(), VarsqlJdbcUtil.getSampleJdbcUrl(item.getUrlFormat()) );
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
		return VarsqlUtils.getResponseResultItemList(dbTypeDriverModelRepository.findByDbtype(dbType));
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

		DBConnectionEntity dbInfo = dbConnectionModelRepository.findOne(DBConnectionSpec.detailInfo(vconnid)).orElseThrow(NullPointerException::new);
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
		DBConnectionEntity dbInfo = dbConnectionModelRepository.findOne(DBConnectionSpec.detailInfo(vconnid)).orElseThrow(NullPointerException::new);

		DBConnectionEntity copyEntity = VarsqlBeanUtils.copyEntity(dbInfo);

		copyEntity.setVconnid(null);
		copyEntity.setVname(copyEntity.getVname()+"-copy");

		dbConnectionModelRepository.save(copyEntity);

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