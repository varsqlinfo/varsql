package com.varsql.web.app.admin.service;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.codehaus.janino.Java;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.core.common.util.VarsqlJdbcUtil;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.sql.util.SqlUtils;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.db.DBConnectionRequestDTO;
import com.varsql.web.dto.db.DBConnectionResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBTypeDriverEntity;
import com.varsql.web.model.entity.db.DBTypeEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.db.DBGroupMappingDbEntityRepository;
import com.varsql.web.repository.db.DBManagerEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverEntityRepository;
import com.varsql.web.repository.db.DBTypeEntityRepository;
import com.varsql.web.repository.spec.DBConnectionSpec;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.VartechUtils;

/**
 *
 * @FileName  : AdminServiceImpl.java
 * @Date      : 2014. 8. 18.
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class AdminServiceImpl extends AbstractService{
	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

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

		return VarsqlUtils.getResponseResult(result, searchParameter, domainMapper, DBConnectionResponseDTO.class);
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
	 */
	public ResponseResult connectionCheck(DBConnectionRequestDTO vtConnection) {

		ResponseResult resultObject = new ResponseResult();

		logger.debug("connection check object param :  {}" , VartechUtils.reflectionToString(vtConnection));

		String username = vtConnection.getVid();
		String pwd = vtConnection.getVpw();
		String dbtype = vtConnection.getVtype();

		DBConnectionEntity dbInfo = dbConnectionModelRepository.findOne(DBConnectionSpec.detailInfo(vtConnection.getVconnid())).orElseThrow(NullPointerException::new);

		if(pwd ==null || "".equals(pwd)){
			pwd = dbInfo.getVpw();
		}

		pwd = pwd==null ? "":pwd;

		DBTypeDriverEntity dbDriverModel = dbTypeDriverModelRepository.findByDriverId(vtConnection.getVdriver());

		String url = vtConnection.getVurl();

		if(!"Y".equals(vtConnection.getUrlDirectYn())){
			url = VarsqlJdbcUtil.getJdbcUrl(dbDriverModel.getUrlFormat(), vtConnection.getVserverip() , vtConnection.getVport() , vtConnection.getVdatabasename());
		}

		logger.debug("connection check url :  {}" , url);

		String conn_query = dbInfo.getVquery();
		String dbvalidation_query = dbDriverModel.getValidationQuery();

		conn_query = conn_query ==null?"":conn_query;
		dbvalidation_query = dbvalidation_query ==null?"":dbvalidation_query;

		String validation_query = !"".equals(conn_query.trim()) ? conn_query:
			( !"".equals(dbvalidation_query.trim()) ?  dbvalidation_query : ValidationProperty.getInstance().validationQuery(dbtype));

		Properties p =new Properties();

		p.setProperty("user", username);
		p.setProperty("password", pwd);

		String result = "";
		String failMessage = "";

		PreparedStatement pstmt = null;
		Connection connChk = null;
		try {
			Class.forName(dbDriverModel.getDbdriver());
			connChk = DriverManager.getConnection(url, p);

			pstmt=connChk.prepareStatement(validation_query);
			pstmt.executeQuery();

			result="success";
			connChk.close();
		} catch (ClassNotFoundException e) {
			result="fail";
			failMessage = e.getMessage();
			logger.error(this.getClass().getName() , e);
		} catch (SQLException e) {
			result="fail";
			failMessage = e.getMessage();
			logger.error(this.getClass().getName() , e);
		}finally{
			SqlUtils.close(connChk , pstmt, null);
		}

		resultObject.setMessageCode(result);
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

		DBTypeDriverEntity dbDriverModel = dbTypeDriverModelRepository.findByDriverId(vtConnection.getVdriver());

		String schemeType = dbDriverModel.getSchemaType();

		if("user".equals(schemeType)) {
			vtConnection.setVdbschema(vtConnection.getVid().toUpperCase());
		}else if("orginUser".equals(schemeType)) {
			vtConnection.setVdbschema(vtConnection.getVid());
		}else if("db".equals(schemeType)){
			vtConnection.setVdbschema(vtConnection.getVdatabasename());
		}else {
			vtConnection.setVdbschema(schemeType);
		}

		DBConnectionEntity reqEntity = vtConnection.toEntity();

		DBConnectionEntity saveEntity= null;
		if(reqEntity.getVconnid() != null) {
			saveEntity= dbConnectionModelRepository.findByVconnid(reqEntity.getVconnid());
			if(saveEntity!= null) {
				copyNonNullProperties(reqEntity, saveEntity, "vpw");
			}
		}

		if(saveEntity==null) {
			saveEntity= reqEntity;
		}

		logger.debug("saveVtconnectionInfo object param :  {}" , VartechUtils.reflectionToString(saveEntity));

		DBConnectionEntity result = dbConnectionModelRepository.save(saveEntity);

		resultObject.setItemOne(result != null? 1 : 0);
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
		DBConnectionEntity result = dbConnectionModelRepository.save(dbInfo);

		dbGroupMappingDbEntityRepository.deleteByVconnid(vconnid);
		dbManagerRepository.deleteByVconnid(vconnid);

		return result != null;
	}

	public List<DBTypeEntity> selectAllDbType() {
		return dbTypeModelRepository.findAll();
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
	public ResponseResult connectionClose(@Valid String vconnid) {
		ResponseResult resultObject = new ResponseResult();
		try {
			ConnectionFactory.getInstance().poolShutdown(vconnid);
			resultObject.setResultCode(ResultConst.CODE.SUCCESS.toInt());
		} catch (Exception e) {
			resultObject.setResultCode(ResultConst.CODE.ERROR.toInt());
			resultObject.setMessage(e.getMessage());
		}

		return resultObject;
	}

	/**
	 * @method  : dbConnectionReset
	 * @desc : db connection pool 초기화
	 * @author   : ytkim
	 * @date   : 2020. 7. 4.
	 * @param vconnid
	 * @return
	 */
	public ResponseResult dbConnectionReset(String vconnid) {
		ResponseResult resultObject = new ResponseResult();
		try {
			ConnectionFactory.getInstance().resetConnectionPool(vconnid);
			resultObject.setResultCode(ResultConst.CODE.SUCCESS.toInt());
		} catch (Exception e) {
			resultObject.setResultCode(ResultConst.CODE.ERROR.toInt());
			resultObject.setMessage(e.getMessage());
		}
		return resultObject;
	}

}