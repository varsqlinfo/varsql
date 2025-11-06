package com.varsql.web.app.database.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.db.valueobject.ddl.DDLTemplateParam;
import com.varsql.core.sql.DDLTemplateCode;
import com.varsql.core.sql.template.DDLTemplateFactory;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.util.SecurityUtil;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.utils.VartechUtils;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DatabaseSourceGenImpl.java
* @DESC		: database source generate service
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 8. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class DatabaseSourceGenImpl{
	private final Logger logger = LoggerFactory.getLogger(DatabaseSourceGenImpl.class);
	
	/**
	 * 테이블 다른 db 테이블로 변경. 
	 * 
	 * @param preferencesInfo
	 * @param schema 스키마 정보
	 * @param databaseName	테이터 베이스명
	 * @param convertDb	변경 DB Type
	 * @return 변경 DDL 정보.
	 */
	public List<DDLInfo> convertTableDDL(PreferencesRequestDTO preferencesInfo, String schema, String convertDb) {
		DatabaseParamInfo dpi = new DatabaseParamInfo(SecurityUtil.userDBInfo(preferencesInfo.getConuid()));
		dpi.setSchema(schema);
		
		String jsonString = preferencesInfo.getPrefVal();
		DataMap settingInfo = VartechUtils.jsonStringToObject(jsonString, DataMap.class, true);

		List<Map> tables = (List<Map>)settingInfo.get("tables");

		String[] tableNmArr =  Arrays.stream(tables.toArray(new HashMap[tables.size()])).map(tmp -> tmp.get("name")).toArray(String[]::new);
		DDLCreateOption ddlOption = new DDLCreateOption();
		
		return MetaControlFactory.getDbInstanceFactory(dpi.getDbType()).tableDdlConvertDB(dpi, ddlOption, DBVenderType.getDBType(convertDb), tableNmArr);
	}
	
	/**
	 * create table ddl 생성
	 * @param preferencesInfo
	 * @param schema
	 * @param convertDb
	 * @param templateParam
	 * @return
	 */
	public List<DDLInfo> createTableDDL(PreferencesRequestDTO preferencesInfo, String convertDb, String templateParam) {
		DDLTemplateParam ddlTemplateParam = VartechUtils.jsonStringToObject(templateParam, DDLTemplateParam.class, true);
		
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		
		DDLInfo ddlInfo = new DDLInfo();
		ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(DBVenderType.getDBType(convertDb), DDLTemplateCode.TABLE.create, ddlTemplateParam));
		reval.add(ddlInfo);
		
		return reval;
	}
}