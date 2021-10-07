package com.varsql.web.app.admin.service;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.ClassLoaderUtils;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.VarsqlJdbcUtil;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.connection.beans.JdbcURLFormatParam;
import com.varsql.core.crypto.DBPasswordCryptionFactory;
import com.varsql.core.sql.util.JdbcUtils;
import com.varsql.web.common.cache.CacheUtils;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.db.DBConnectionRequestDTO;
import com.varsql.web.dto.db.DBTypeDriverProviderResponseDTO;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBTypeDriverEntity;
import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.varsql.web.model.entity.db.DBTypeEntity;
import com.varsql.web.model.mapper.db.DBConnectionMapper;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.db.DBGroupMappingDbEntityRepository;
import com.varsql.web.repository.db.DBManagerEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverProviderRepository;
import com.varsql.web.repository.db.DBTypeEntityRepository;
import com.varsql.web.repository.spec.DBConnectionSpec;
import com.varsql.web.repository.spec.DBTypeDriverProviderSpec;
import com.varsql.web.repository.user.FileInfoEntityRepository;
import com.varsql.web.security.UserService;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.VarsqlBeanUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.crypto.EncryptDecryptException;
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
	private UserService userService;

	@Autowired
	@Qualifier(ResourceConfigConstants.CACHE_MANAGER)
	private CacheManager cacheManager;

	@Autowired
	private FileInfoEntityRepository fileInfoEntityRepository;

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

		return VarsqlUtils.getResponseResult(result, searchParameter, DBConnectionMapper.INSTANCE);
	}

	/**
	 *
	 * @Method Name  : selectDetailObject
	 * @Method 설명 : db 정보 상세.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectDetailObject(String vconnid) {
		return VarsqlUtils.getResponseResultItemOne(dbConnectionModelRepository.findOne(DBConnectionSpec.detailInfo(vconnid)));
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
		PreparedStatement pstmt = null;
		Connection connChk = null;

		VarsqlAppCode resultCode = VarsqlAppCode.SUCCESS;
		try {
			String pwd = vtConnection.getVpw();
			if (pwd == null || "".equals(pwd))
				pwd = dbInfo.getVpw();
			pwd = (pwd == null) ? "" : pwd;
			pwd = DBPasswordCryptionFactory.getInstance().decrypt(pwd);
			Properties p = new Properties();
			p.setProperty("user", username);
			p.setProperty("password", pwd);

			List<FileInfoEntity> driverJarFiles= fileInfoEntityRepository.findByFileContId(driverProviderEntity.getDriverProviderId());

			List<URL> jarUrlList = new ArrayList<>();
			for(FileInfoEntity fie : driverJarFiles) {
				jarUrlList.add(FileServiceUtils.getFileInfoToURL(fie));
			}

		    Driver dbDriver = ClassLoaderUtils.getJdbcDriver(driverProviderEntity.getDriverClass(), jarUrlList.toArray(new URL[0]));

		    connChk = dbDriver.connect(url, p);
		    pstmt = connChk.prepareStatement(validation_query);

			pstmt.executeQuery();
			connChk.close();
		} catch (EncryptDecryptException e) {
			resultCode = VarsqlAppCode.ERROR;
			failMessage = "password decrypt error : " + e.getMessage();
			logger.error(getClass().getName(), (Throwable) e);
		} catch (ClassNotFoundException e) {
			resultCode = VarsqlAppCode.ERROR;
			failMessage = e.getMessage();
			logger.error("url :{}", url);
			logger.error(getClass().getName(), e);
		}catch (Exception e) {
			resultCode = VarsqlAppCode.ERROR;
			failMessage = e.getMessage();
			logger.error(getClass().getName(), e);
		}finally {
			JdbcUtils.close(connChk, pstmt, null);
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
				copyNonNullProperties(reqEntity, saveEntity, new String[] { "vpw" });
			}
		}
		if (!updateFlag)
			saveEntity = reqEntity;
		if (vtConnection.isPasswordChange())
			saveEntity.setVpw(vtConnection.getVpw());
		logger.debug("saveVtconnectionInfo object param :  {}", VartechUtils.reflectionToString(saveEntity));
		this.dbConnectionModelRepository.save(saveEntity);
		if (updateFlag)
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
		resultObject.setItemOne(Integer.valueOf(1));
		return resultObject;
	}

	public static void copyNonNullProperties(Object src, Object target, String... checkProperty) throws BeansException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	    BeanUtils.copyProperties(src, target, getNullPropertyNames(src, checkProperty));
	}

	public static String[] getNullPropertyNames (Object source, String[] checkProperty) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
	    List<String> prop = Arrays.asList(checkProperty);
	    Set<String> emptyNames = new HashSet<String>();
	    for(java.beans.PropertyDescriptor pd : pds) {
	    	String name = pd.getName();

	    	if(prop.contains(name)) {
	    		Object srcValue = src.getPropertyValue(pd.getName());
		        if (srcValue == null || "".equals(srcValue)) {
		        	emptyNames.add(name);
		        }else {
		        	if("".equals(srcValue.toString().trim())) {
		        		PropertyUtils.setProperty(source, name, srcValue.toString().trim());
		        	}
		        }
	    	}
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
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
	 * @param paramMap
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
			resultObject.setItemOne(DBPasswordCryptionFactory.getInstance().decrypt(dbInfo.getVpw()));
		}catch(EncryptDecryptException e) {
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
			return dto;
		}).collect(Collectors.toList());

		return VarsqlUtils.getResponseResultItemList(list);
	}

}