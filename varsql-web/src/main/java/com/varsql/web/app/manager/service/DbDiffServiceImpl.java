package com.varsql.web.app.manager.service;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.util.DatabaseUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.RequestResultCode;

import lombok.RequiredArgsConstructor;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DbDiffServiceImpl.java
* @DESC		: db 비교
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 1. 23. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
@RequiredArgsConstructor
public class DbDiffServiceImpl{

	private final Logger logger = LoggerFactory.getLogger(DbDiffServiceImpl.class);

	private final DBConnectionEntityRepository  dbConnectionEntityRepository;

	/**
	 *
	 * @Method Name  : objectTypeList
	 * @Method 설명 : db object type list (table , view 등등)
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 2.
	 * @변경이력  :
	 * @param vconnid
	 * @return
	 * @throws SQLException
	 */
	public ResponseResult objectTypeList(String vconnid) throws SQLException {
		
		logger.debug("vconnid : {}",vconnid);

		ResponseResult resultObject = new ResponseResult();

		DBConnectionEntity vtConnRVO = dbConnectionEntityRepository.findByVconnid(vconnid);

		if(vtConnRVO==null){
			resultObject.setResultCode(RequestResultCode.ERROR);
			resultObject.setList(null);
		}else{
			
			String dbType = vtConnRVO.getDbTypeDriverProvider().getDbType(); 
			
			DBVenderType venderType = DBVenderType.getDBType(dbType);

			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(venderType);
			resultObject.setList(dbMetaEnum.getServiceMenu());
			
			DatabaseParamInfo param = DatabaseUtils.dbConnectionEntityToDatabaseParamInfo(vtConnRVO);
			
			if(venderType.isUseDatabaseName()) {
				resultObject.addCustomMapAttribute("schemaInfo", dbMetaEnum.getDatabases(param));
			}else {
				resultObject.addCustomMapAttribute("schemaInfo", dbMetaEnum.getSchemas(param));
			}
		}

		return resultObject;
	}

}