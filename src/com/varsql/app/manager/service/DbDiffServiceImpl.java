package com.varsql.app.manager.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.beans.VtconnectionRVO;
import com.varsql.app.common.dao.CommonDAO;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.DBObjectType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.beans.BaseObjectInfo;
import com.varsql.core.db.beans.DatabaseInfo;
import com.varsql.core.db.beans.DatabaseParamInfo;
import com.varsql.core.db.beans.ddl.DDLCreateOption;
import com.vartech.common.app.beans.ParamMap;
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
	
	@Autowired
	CommonDAO commonDAO;
	
	/**
	 * 
	 * @Method Name  : objectTypeList
	 * @Method 설명 : db object type list (table , view 등등) 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 2. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult objectTypeList(ParamMap paramMap) {
		
		ResponseResult resultObject = new ResponseResult();
		
		VtconnectionRVO vtConnRVO = commonDAO.selectDetailObject(paramMap);
		
		if(vtConnRVO==null){
			resultObject.setStatus(ResultConst.CODE.ERROR.toInt());
			resultObject.setItemList(null);
		}else{
			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(vtConnRVO.getVTYPE());
			resultObject.setItemList(dbMetaEnum.getServiceMenu());
			
			DatabaseParamInfo dpi =  getDatabaseParamInfo(vtConnRVO);
					
			resultObject.addCustoms("schemaInfo",dbMetaEnum.getSchemas(dpi));
		}
		
		return resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : objectList
	 * @Method 설명 : object list
	 * @작성자   : ytkim
	 * @작성일   : 2018. 12. 19. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult objectList(ParamMap paramMap) {
		
		ResponseResult resultObject = new ResponseResult();
		
		VtconnectionRVO vtConnRVO = commonDAO.selectDetailObject(paramMap);
		
		if(vtConnRVO==null){
			resultObject.setStatus(ResultConst.CODE.ERROR.toInt());
			resultObject.setItemList(null);
		}else{
			DatabaseParamInfo dpi = getDatabaseParamInfo(vtConnRVO);
			dpi.setSchema(paramMap.getString("schema", dpi.getSchema()));
			
			String objectType = paramMap.getString("objectType"); 
			dpi.setObjectType(objectType);
			
			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(vtConnRVO.getVTYPE());
			String objectId = DBObjectType.getDBObjectType(objectType).getObjectTypeId(); 
			if(DBObjectType.TABLE.getObjectTypeId().equals(objectId)){
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
	
	
	private DatabaseParamInfo getDatabaseParamInfo(VtconnectionRVO vtConnRVO) {
		DatabaseParamInfo dpi = new DatabaseParamInfo();
		dpi.setConuid(null, SecurityUtil.loginUser().getUid(), new DatabaseInfo(vtConnRVO.getVCONNID()
				, null
				, vtConnRVO.getVTYPE()
				, vtConnRVO.getVNAME()
				, vtConnRVO.getVDBSCHEMA()
				, vtConnRVO.getBASETABLE_YN()
				, vtConnRVO.getLAZYLOAD_YN()
				, vtConnRVO.getVDBVERSION()
				, vtConnRVO.getSCHEMA_VIEW_YN()
				, vtConnRVO.getMAX_SELECT_COUNT()
			)
		);
		
		return dpi; 
	}
}