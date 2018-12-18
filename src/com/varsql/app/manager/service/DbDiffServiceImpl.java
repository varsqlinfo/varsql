package com.varsql.app.manager.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.beans.VtconnectionRVO;
import com.varsql.app.common.dao.CommonDAO;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.beans.DatabaseParamInfo;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.ResultConst;

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
	
	public ResponseResult objectList(ParamMap paramMap) {
		
		ResponseResult resultObject = new ResponseResult();
		
		VtconnectionRVO vtConnRVO = commonDAO.selectDetailObject(paramMap);
		
		if(vtConnRVO==null){
			resultObject.setStatus(ResultConst.CODE.ERROR.toInt());
			resultObject.setItemList(null);
		}else{
			DatabaseParamInfo dpi = new DatabaseParamInfo();
			
			/**
			 * todo  db object 가져오는 부분 처리 할것. 
			 */
			
			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(vtConnRVO.getVTYPE());
			//resultObject.setItemList(dbMetaEnum.getDBObjectList(dbObjType, paramInfo));
		}
		
		
		return resultObject;
	}
}