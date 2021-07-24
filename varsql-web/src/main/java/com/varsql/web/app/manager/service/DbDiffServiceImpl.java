package com.varsql.web.app.manager.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.valueobject.BaseObjectInfo;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.util.ConvertUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.ResultConst;

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
public class DbDiffServiceImpl{

	private final Logger logger = LoggerFactory.getLogger(DbDiffServiceImpl.class);

	@Autowired
	private DBConnectionEntityRepository  dbConnectionEntityRepository;
	/**
	 *
	 * @Method Name  : objectTypeList
	 * @Method 설명 : db object type list (table , view 등등)
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 2.
	 * @변경이력  :
	 * @param vconnid
	 * @return
	 */
	public ResponseResult objectTypeList(String vconnid) {

		ResponseResult resultObject = new ResponseResult();

		DBConnectionEntity vtConnRVO = dbConnectionEntityRepository.findByVconnid(vconnid);

		if(vtConnRVO==null){
			resultObject.setStatus(ResultConst.CODE.ERROR.toInt());
			resultObject.setItemList(null);
		}else{
			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(vtConnRVO.getVtype());
			resultObject.setItemList(dbMetaEnum.getServiceMenu());
			resultObject.addCustoms("schemaInfo",dbMetaEnum.getSchemas(  getDatabaseParamInfo(vtConnRVO)));
		}

		return resultObject;
	}

	/**
	 *
	 * @param objectType
	 * @Method Name  : objectList
	 * @Method 설명 : object list
	 * @작성자   : ytkim
	 * @작성일   : 2018. 12. 19.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult objectList(String vconnid, String schema, String objectType) {

		ResponseResult resultObject = new ResponseResult();

		DBConnectionEntity vtConnRVO = dbConnectionEntityRepository.findByVconnid(vconnid);

		if(vtConnRVO==null){
			resultObject.setStatus(ResultConst.CODE.ERROR.toInt());
			resultObject.setItemList(null);
		}else{
			DatabaseParamInfo dpi = getDatabaseParamInfo(vtConnRVO);
			dpi.setSchema(schema);
			dpi.setObjectType(objectType);

			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(vtConnRVO.getVtype());
			String objectId = ObjectType.getDBObjectType(objectType).getObjectTypeId();
			if(ObjectType.TABLE.getObjectTypeId().equals(objectId)){
				resultObject.setItemList(dbMetaEnum.getDBObjectMeta(objectId, dpi));
			}else{
				List<BaseObjectInfo> objectList = dbMetaEnum.getDBObjectList(objectId, dpi);

				String[] objectNameArr = new String[objectList.size()];

				int idx =0 ;
				for(BaseObjectInfo boi : objectList){
					//System.out.println("boi.getName() : "+ boi.getName());
					objectNameArr[idx] =boi.getName();
					++idx;
				}
				resultObject.setItemList(dbMetaEnum.getDDLScript(objectId, dpi, new DDLCreateOption(), objectNameArr));
			}
		}

		return resultObject;
	}

	private DatabaseParamInfo getDatabaseParamInfo(DBConnectionEntity vtConnRVO) {
		DatabaseParamInfo dpi = new DatabaseParamInfo();
		dpi.setConuid(null, SecurityUtil.loginInfo().getViewid(), new DatabaseInfo(vtConnRVO.getVconnid()
				, null
				, vtConnRVO.getVtype()
				, vtConnRVO.getVname()
				, vtConnRVO.getVdbschema()
				, vtConnRVO.getBasetableYn()
				, vtConnRVO.getLazyloadYn()
				, ConvertUtils.longValueOf(vtConnRVO.getVdbversion())
				, vtConnRVO.getSchemaViewYn()
				, ConvertUtils.intValue(vtConnRVO.getMaxSelectCount())
			)
		);

		return dpi;
	}
}