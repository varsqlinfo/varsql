package com.varsql.web.app.manager.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.servicemenu.DBObjectType;
import com.varsql.core.db.valueobject.BaseObjectInfo;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.dto.db.DBConnectionResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.model.mapper.db.DBConnectionMapper;
import com.varsql.web.model.mapper.user.UserMapper;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.spec.DBConnectionSpec;
import com.varsql.web.repository.spec.UserSpec;
import com.varsql.web.repository.user.UserMgmtRepository;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;

/**
 * -----------------------------------------------------------------------------
* @fileName		: ManagerCommonServiceImpl.java
* @desc		: 매니저 공통 처리.
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 1. 23. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class ManagerCommonServiceImpl extends AbstractService{

	@Autowired
	private UserMgmtRepository userMgmtRepository;

	@Autowired
	private DBConnectionEntityRepository dbConnectionModelRepository;


	public List<DBConnectionResponseDTO> selectdbList() {
		return dbConnectionModelRepository.findAll(
			DBConnectionSpec.mgmtDbList(SecurityUtil.userViewId() , "")
		).stream().map(item -> DBConnectionMapper.INSTANCE.toDto(item)).collect(Collectors.toList());
	}

	/**
	 * @method  : selectdbList
	 * @desc : db 목록.
	 * @author   : ytkim
	 * @date   : 2018. 1. 23
	 * @param searchParameter
	 * @param allFlag
	 * @return
	 */
	public ResponseResult selectdbList(SearchParameter searchParameter) {
		Page<DBConnectionEntity> result = dbConnectionModelRepository.findAll(
			DBConnectionSpec.mgmtDbList(SecurityUtil.userViewId() , searchParameter.getKeyword())
			, VarsqlUtils.convertSearchInfoToPage(searchParameter, Sort.by(DBConnectionEntity.VNAME))
		);
		return VarsqlUtils.getResponseResult(result, searchParameter, DBConnectionMapper.INSTANCE);
	}

	/**
	 * @method  : selectUserList
	 * @desc : 사용자 목록
	 * @author   : ytkim
	 * @date   : 2019. 8. 16.
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectUserList(SearchParameter searchParameter) {
		Page<UserEntity> result = userMgmtRepository.findAll(
			UserSpec.likeUnameOrUid(SecurityUtil.isAdmin(), searchParameter.getKeyword())
			, VarsqlUtils.convertSearchInfoToPage(searchParameter, Sort.by(UserEntity.UNAME))
		);

		return VarsqlUtils.getResponseResult(result, searchParameter, UserMapper.INSTANCE);
	}

	/**
	 * object list
	 *  
	 * @method : objectList
	 * @param vconnid
	 * @param objectType
	 * @param schema
	 * @param databaseName
	 * @return
	 */
	public ResponseResult objectMetaList(String vconnid, String objectType, String schema) {

		ResponseResult resultObject = new ResponseResult();

		DatabaseInfo databaseInfo = dbConnectionModelRepository.findDatabaseInfo(vconnid);

		if(databaseInfo==null){
			resultObject.setResultCode(RequestResultCode.ERROR);
			return resultObject;
		}else{
			DatabaseParamInfo dpi = new DatabaseParamInfo(databaseInfo);
			dpi.setSchema(schema);
			dpi.setObjectType(objectType);

			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dpi.getDbType());
			String objectId = DBObjectType.getDBObjectType(objectType).getObjectTypeId();
			if(DBObjectType.TABLE.getObjectTypeId().equals(objectId)){ //object type "table" 인 경우는 column 정보도 같이 전송
				resultObject.setList(dbMetaEnum.getDBObjectMeta(objectId, dpi));
			}else{ // 테이블이 아닌 경우는 ddl를 비교.
				List<BaseObjectInfo> objectList = dbMetaEnum.getDBObjectList(objectId, dpi);
				resultObject.setList(dbMetaEnum.getDDLScript(objectId, dpi, new DDLCreateOption(), objectList.stream().map(tmp-> tmp.getName()).toArray(String[]::new)));
			}
		}

		return resultObject;
	}
	
	/**
	 * object list
	 * @param vconnid
	 * @param objectType
	 * @param schema
	 * @return
	 */
	public ResponseResult objectList(String vconnid, String objectType, String schema) {
		
		ResponseResult resultObject = new ResponseResult();
		
		DatabaseInfo databaseInfo = dbConnectionModelRepository.findDatabaseInfo(vconnid);
		
		if(databaseInfo==null){
			resultObject.setResultCode(RequestResultCode.ERROR);
			return resultObject;
		}else{
			DatabaseParamInfo dpi = new DatabaseParamInfo(databaseInfo);
			dpi.setSchema(schema);
			dpi.setObjectType(objectType);
			
			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dpi.getDbType());
			String objectId = DBObjectType.getDBObjectType(objectType).getObjectTypeId();
			if(DBObjectType.TABLE.getObjectTypeId().equals(objectId)){ //object type "table" 인 경우는 column 정보도 같이 전송
				resultObject.setList(dbMetaEnum.getDBObjectList(objectId, dpi));
			}else{
				resultObject.setList(dbMetaEnum.getDBObjectList(objectId, dpi));
			}
		}
		
		return resultObject;
	}
}