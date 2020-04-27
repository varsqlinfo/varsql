package com.varsql.web.app.admin.service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.VarsqlJdbcUtil;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.sql.util.SQLUtil;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.dto.db.ConnectionRequestDTO;
import com.varsql.web.dto.db.ConnectionResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBTypeDriverEntity;
import com.varsql.web.model.entity.db.DBTypeEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
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

		return VarsqlUtils.getResponseResult(result, searchParameter, domainMapper, ConnectionResponseDTO.class);
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
		ResponseResult resultObject = new ResponseResult();

		resultObject.setItemOne(dbConnectionModelRepository.findOne(DBConnectionSpec.detailInfo(vconnid)));

		return resultObject;
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
	public ResponseResult connectionCheck(ConnectionRequestDTO vtConnection) {

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
			SQLUtil.close(connChk , pstmt, null);
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
	 */
	public ResponseResult saveVtconnectionInfo(ConnectionRequestDTO vtConnection) {

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

		logger.debug("saveVtconnectionInfo object param :  {}" , VartechUtils.reflectionToString(vtConnection));

		DBConnectionEntity result = dbConnectionModelRepository.save(vtConnection.toEntity());

		resultObject.setItemOne(result != null? 1 : 0);

		if("Y".equals(vtConnection.getPoolInit())){
			try {
				ConnectionFactory.getInstance().resetConnectionPool(vtConnection.getVconnid());
				resultObject.setResultCode(ResultConst.CODE.SUCCESS.toInt());
			} catch (Exception e) {
				resultObject.setResultCode(ResultConst.CODE.ERROR.toInt());
				resultObject.setMessage(e.getMessage());
			}
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
	public boolean deleteVtconnectionInfo(String vconnid) {

		DBConnectionEntity dbInfo = dbConnectionModelRepository.findOne(DBConnectionSpec.detailInfo(vconnid)).orElseThrow(NullPointerException::new);
		dbInfo.setDelYn("Y");
		DBConnectionEntity result = dbConnectionModelRepository.save(dbInfo);

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
		ResponseResult resultObject = new ResponseResult();
		resultObject.setItemList(dbTypeDriverModelRepository.findByDbtype(dbType));

		return resultObject;
	}

}