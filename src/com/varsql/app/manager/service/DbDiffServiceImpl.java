package com.varsql.app.manager.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.beans.VtconnectionRVO;
import com.varsql.app.common.dao.CommonDAO;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.UUIDUtil;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.db.DBObjectType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.beans.DatabaseInfo;
import com.varsql.core.db.beans.DatabaseParamInfo;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.VartechReflectionUtils;
import com.vartech.common.utils.VartechUtils;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DbnUserServiceImpl.java
* @DESC		: db 사용자 관리. 
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
	 * @Method Name  : objectTypeList
	 * @Method 설명 : db 사용자 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
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
			DatabaseParamInfo dpi = new DatabaseParamInfo();
			dpi.setConuid(null, SecurityUtil.loginUser().getUid(), new DatabaseInfo(vtConnRVO.getVCONNID()
					, null
					, vtConnRVO.getVTYPE()
					, vtConnRVO.getVNAME()
					, vtConnRVO.getVDBSCHEMA()
					, vtConnRVO.getBASETABLE_YN()
					, vtConnRVO.getLAZYLOAD_YN()
					, vtConnRVO.getVDBVERSION()));
			
			dpi.setGubun(paramMap.getString("objectType"));
			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(vtConnRVO.getVTYPE());
			resultObject.setItemList(dbMetaEnum.getDBObjectMeta(DBObjectType.getDBObjectType(dpi.getGubun()).getObjName(), dpi));
		}
		
		return resultObject;
	}
}