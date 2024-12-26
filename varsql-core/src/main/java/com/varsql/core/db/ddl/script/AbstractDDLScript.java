package com.varsql.core.db.ddl.script;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.ddl.conversion.ConversionType;
import com.varsql.core.db.ddl.conversion.DDLConversionFactory;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.util.DataTypeUtils;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.CommentInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.TableInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.db.valueobject.ddl.DDLTemplateParam;
import com.varsql.core.sql.CommentType;
import com.varsql.core.sql.DDLTemplateCode;
import com.varsql.core.sql.template.DDLTemplateFactory;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : AbstractDDLScript.java
 * @프로그램 설명 : script 생성 클래스
 * @Date      : 2015. 6. 18.
 * @작성자      : ytkim
 * @변경이력 :
 */
public abstract class AbstractDDLScript implements DDLScript{
	
	protected MetaControlBean dbInstanceFactory;

	protected DBVenderType dbType;
	
	// db 별 기본값을 표준 값으로 지정
	protected Map<String,String> STANDARD_DEFAULT_VALUE = new HashMap<String,String>(){
		private static final long serialVersionUID = 1L;
	{
		put("sysdate", "CURRENT_TIMESTAMP");
		put("getdate()", "CURRENT_TIMESTAMP");
		put("now()", "CURRENT_TIMESTAMP");
	}};
	
	// 기본 값을 db vender 값으로 지정
	protected Map<String,String> DEFAULT_VALUE_TO_VENDER_VALUE = new HashMap<String,String>();

	protected AbstractDDLScript(MetaControlBean dbInstanceFactory, DBVenderType dbType){
		this.dbInstanceFactory =  dbInstanceFactory;
		this.dbType =  dbType;
	}
	
	protected void addStandardDefaultValue(String key, String value) {
		STANDARD_DEFAULT_VALUE.put(key, value);
	}
	
	protected void addDefaultValueToVenderValue(String key, String value) {
		DEFAULT_VALUE_TO_VENDER_VALUE.put(key, value);
	}
	
	/**
	 *
	 * @Method Name  : getDefaultValue
	 * @Method 설명 :
	 * @작성일   : 2017. 11. 1.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param columnInfo
	 * @param key
	 * @param dataTypeInfo
	 * @param b
	 * @return
	 */
	protected String getDefaultValue(String columnDef, DataType dataTypeInfo) {
		return getDefaultValue(columnDef, dataTypeInfo, false);
	}
	protected String getDefaultValue(String columnDef ,DataType dataTypeInfo, boolean onlyChar) {
		return DbMetaUtils.getDefaultValue(columnDef, dataTypeInfo, onlyChar);
	}
	/**
	 *
	 * @Method Name  : getNotNullValue
	 * @Method 설명 :
	 * @작성일   : 2017. 11. 1.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param source
	 * @param key
	 * @param dataTypeInfo
	 * @return
	 */
	protected String getNotNullValue(String nullable) {
		return DbMetaUtils.getNotNullValue(nullable);
	}

	protected DDLTemplateParam getDefaultTemplateParam(DDLCreateOption ddlOption, DatabaseParamInfo dataParamInfo, List listMap) {
		return DDLTemplateParam.builder()
			.dbType(dataParamInfo.getDbType())
			.ddlOpt(ddlOption)
			.schema(dataParamInfo.getSchema())
			.objectName(dataParamInfo.getObjectName())
			.items(listMap)
			.build();
	}

	/**
	 *
	 * @Method Name  : getTable
	 * @Method 설명 : table ddl
	 * @Method override : @see com.varsql.core.db.ddl.script.DDLScript#getTable(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 18.
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param objNames
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> getTables(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNames) throws Exception {

		List<TableInfo> tableInfoList = dbInstanceFactory.getDBObjectMeta(ObjectType.TABLE.getObjectTypeId(), dataParamInfo,objNames);

		StringBuilder ddlStrBuf;

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		
		DDLInfo ddlInfo;
		
		for(TableInfo tableInfo: tableInfoList){

			String name = tableInfo.getName();
			dataParamInfo.setObjectName(name);

			ddlInfo  = new DDLInfo();
			ddlInfo.setName(name);
			ddlStrBuf = new StringBuilder();
			
			List<CommentInfo> commentsList = new LinkedList<>();
			
			if(!StringUtils.isBlank(tableInfo.getRemarks())) {
				commentsList.add(CommentInfo.builder().type(CommentType.TABLE.getType()).name(name).item(tableInfo).comment(tableInfo.getRemarks()).build());
			}
			
			List<ColumnInfo> columnList =tableInfo.getColList();
			
			columnList.forEach(item->{
				if(!StringUtils.isBlank(item.getComment())) {
					commentsList.add(CommentInfo.builder().type(CommentType.COLUMN.getType()).name(item.getName()).item(item).comment(item.getComment()).build());
				}
			});
			
			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(dataParamInfo.getDbType())
				.schema(dataParamInfo.getSchema())
				.objectName(name)
				.ddlOpt(ddlOption)
				.columnList(columnList)
				.keyList(dbInstanceFactory.getConstraintsKeys(dataParamInfo,name))
				.commentsList(commentsList)
			.build();
			
			ddlStrBuf.append(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.TABLE.create, param));

			ddlStrBuf.append(BlankConstants.NEW_LINE_TWO);

			ddlInfo.setCreateScript(ddlStrBuf.toString());

			reval.add(ddlInfo);
		}

		return reval;
	}

	/**
	 *
	 * @Method Name  : getView
	 * @Method 설명 : view ddl
	 * @Method override : @see com.varsql.core.db.ddl.script.DDLScript#getView(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 18.
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param objNames
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getViews(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNames) throws Exception {

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		try(SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			for (String viewNm : objNames) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(viewNm);
	
				dataParamInfo.setObjectName(viewNm);
	
				StringBuilder sourceSb = new StringBuilder();
				List<String> srcProcList = sqlSesseion.selectList("viewScript", dataParamInfo);
				for (int j = 0; j < srcProcList.size(); j++) {
					sourceSb.append( srcProcList.get(j));
				}
	
				DDLTemplateParam param = DDLTemplateParam.builder()
						.dbType(dataParamInfo.getDbType())
						.schema(dataParamInfo.getSchema())
						.objectName(viewNm)
						.ddlOpt(ddlOption)
						.sourceText(sourceSb.toString())
					.build();
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.VIEW.create, param));
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	/**
	 *
	 * @Method Name  : getIndex
	 * @Method 설명 : index ddl
	 * @Method override : @see com.varsql.core.db.ddl.script.DDLScript#getIndex(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 18.
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param objNames
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNames)	throws Exception {
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		try(SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			for (String objNm : objNames) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
	
				StringBuilder sourceSb = new StringBuilder();
				List<String> srcList = sqlSesseion.selectList("indexScript", dataParamInfo);
				
				for (int j = 0; j < srcList.size(); j++) {
					sourceSb.append( srcList.get(j));
				}
	
				DDLTemplateParam param = DDLTemplateParam.builder()
						.dbType(dataParamInfo.getDbType())
						.schema(dataParamInfo.getSchema())
						.objectName(objNm)
						.ddlOpt(ddlOption)
						.sourceText(sourceSb.toString())
					.build();
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.INDEX.create, param));
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	/**
	 *
	 * @Method Name  : getFunction
	 * @Method 설명 : function ddl
	 * @Method override : @see com.varsql.core.db.ddl.script.DDLScript#getFunction(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 18.
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param objNames
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNames) throws Exception {

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		try(SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			for (String objNm : objNames) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
				
				StringBuilder sourceSb = new StringBuilder();
				List<String> srcProcList = sqlSesseion.selectList("functionScript", dataParamInfo);
				for (int j = 0; j < srcProcList.size(); j++) {
					sourceSb.append( srcProcList.get(j));
				}
	
				DDLTemplateParam param = DDLTemplateParam.builder()
						.dbType(dataParamInfo.getDbType())
						.schema(dataParamInfo.getSchema())
						.objectName(objNm)
						.ddlOpt(ddlOption)
						.sourceText(sourceSb.toString())
					.build();
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.FUNCTION.create, param));
				
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	/**
	 *
	 * @Method Name  : getProcedure
	 * @Method 설명 : Procedure ddl
	 * @Method override : @see com.varsql.core.db.ddl.script.DDLScript#getProcedure(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 18.
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param objNames
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNames)	throws Exception {

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		try(SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			for (String objNm : objNames) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
				
				StringBuilder sourceSb = new StringBuilder();
				List<String> srcProcList = sqlSesseion.selectList("procedureScript", dataParamInfo);
				for (int j = 0; j < srcProcList.size(); j++) {
					sourceSb.append( srcProcList.get(j));
				}
	
				DDLTemplateParam param = DDLTemplateParam.builder()
						.dbType(dataParamInfo.getDbType())
						.schema(dataParamInfo.getSchema())
						.objectName(objNm)
						.ddlOpt(ddlOption)
						.sourceText(sourceSb.toString())
					.build();
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.PROCEDURE.create, param));
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	/**
	 *
	 * @Method Name  : getTrigger
	 * @Method 설명 : trigger ddl
	 * @Method override : @see com.varsql.core.db.ddl.script.DDLScript#getTrigger(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 18.
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param objNames
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getTriggers(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNames) throws Exception {
		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		try(SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			for (String objNm : objNames) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
				
				StringBuilder sourceSb = new StringBuilder();
				List<String> srcProcList = sqlSesseion.selectList("triggerScript", dataParamInfo);
				for (int j = 0; j < srcProcList.size(); j++) {
					sourceSb.append( srcProcList.get(j));
				}
	
				DDLTemplateParam param = DDLTemplateParam.builder()
						.dbType(dataParamInfo.getDbType())
						.schema(dataParamInfo.getSchema())
						.objectName(objNm)
						.ddlOpt(ddlOption)
						.sourceText(sourceSb.toString())
					.build();
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.TRIGGER.create, param));
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	/**
	 *
	 * @Method Name  : getSequence
	 * @Method 설명 : sequence ddl
	 * @Method override : @see com.varsql.core.db.ddl.script.DDLScript#getSequence(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 18.
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param objNames
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getSequences(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNames) throws Exception {

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		try(SqlSession sqlSesseion = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
		
			for (String objNm : objNames) {
	
				ddlInfo = new DDLInfo();
				ddlInfo.setName(objNm);
				dataParamInfo.setObjectName(objNm);
				
				DDLTemplateParam param = DDLTemplateParam.builder()
					.dbType(dataParamInfo.getDbType())
					.schema(dataParamInfo.getSchema())
					.objectName(objNm)
					.ddlOpt(ddlOption)
					.item(sqlSesseion.selectOne("sequenceScript", dataParamInfo))
				.build();
	
				ddlInfo.setCreateScript(DDLTemplateFactory.getInstance().render(this.dbType, DDLTemplateCode.SEQUENCE.create, param));
				reval.add(ddlInfo);
			}
		}

		return reval;
	}

	/**
	 *
	 * @Method Name  : getTable
	 * @Method 설명 : table ddl
	 * @Method override : @see com.varsql.core.db.ddl.script.DDLScript#getTable(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 18.
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param objNames
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> tableDdlConvertDB(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, DBVenderType convertDb, String ...objNames) throws Exception {

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		
		List<TableInfo> tableInfoList = dbInstanceFactory.getDBObjectMeta(ObjectType.TABLE.getObjectTypeId(), dataParamInfo,objNames);

		DDLInfo ddlInfo;
		
		StringBuilder ddlStrBuf;
		
		DataTypeFactory dataTypeFactory = dbInstanceFactory.getDataTypeImpl();
		
		MetaControlBean convertDbDataTypeFactory = MetaControlFactory.getDbInstanceFactory(convertDb);
		
		for(TableInfo tableInfo: tableInfoList){

			String name = tableInfo.getName();
			
			ddlInfo = new DDLInfo();
			ddlInfo.setName(name);
			dataParamInfo.setObjectName(name);
			
			ddlStrBuf = new StringBuilder();

			List<CommentInfo> commentsList = new LinkedList<>();
			
			if(!StringUtils.isBlank(tableInfo.getRemarks())) {
				commentsList.add(CommentInfo.builder().type(CommentType.TABLE.getType()).name(name).item(tableInfo).comment(tableInfo.getRemarks()).build());
			}
			
			List<ColumnInfo> columnList =tableInfo.getColList();
			
			columnList.forEach(item->{
				
				String typeName = DbMetaUtils.getTypeName(item.getTypeName()); 
				
				ConversionType conversionType = DDLConversionFactory.getInstance().getConversionType(this.dbType, convertDb, typeName);
				
				DataType dataType = dataTypeFactory.getDataType(item.getTypeCode(), typeName);
				
				if(conversionType != null) {
					String conversionTypeName = conversionType.getType();
					// Postgresql,sqlserver varchar 사이즈 없을 경우 처리.
					if(dataType.getJDBCDataTypeMetaInfo().isSize() && dataType.getDefaultSize() > -1 && ( item.getLength()== null || item.getLength().compareTo(BigDecimal.ZERO) < 0)) {
						conversionTypeName = conversionType.getMax() != null ? conversionType.getMax() : conversionTypeName; 
					}
					
					item.setTypeName(conversionTypeName);
					
					BigDecimal columnSize = item.getLength();
					
					DataType dataTypeInfo = DataTypeUtils.getDataType(conversionTypeName, convertDb, item);
					
					if(DataTypeUtils.isTypeAndLengthPattern(conversionTypeName)) {
						item.setTypeAndLength(conversionTypeName);
					}else {
						item.setTypeAndLength(dataTypeInfo.getJDBCDataTypeMetaInfo().getTypeAndLength(conversionTypeName, dataTypeInfo, null, (columnSize == null? -1 : columnSize.longValue()),
							item.getDataPrecision(), item.getDecimalDigits()));
					}
					
					item.setTypeCode(dataTypeInfo.getTypeCode());
				}else {
					// Postgresql의 varchar 같은 경우 사이즈 가 없을 경우 default size로 셋팅
					if(dataType.getJDBCDataTypeMetaInfo().isSize() && dataType.getDefaultSize() > -1 && ( item.getLength()== null || item.getLength().compareTo(BigDecimal.ZERO) < 0)) {
						item.setLength(dataType.getDefaultSize());
					}
					
					item.setTypeName(null);
					item.setTypeAndLength(null);
					item.setTypeCode(dataType.getTypeCode());
				}
				
				String defaultVal = item.getDefaultVal(); 
				if(!StringUtils.isBlank(defaultVal)) {
					item.setDefaultVal(convertDbDataTypeFactory.getDefaultValueToVenderValue(getStandardDefaultValue(defaultVal, dataType), dataType));
				}
				
				String comment = item.getComment(); 
				if(!StringUtils.isBlank(comment)) {
					commentsList.add(CommentInfo.builder().type(CommentType.COLUMN.getType()).name(item.getName()).item(item).comment(comment).build());
				}
			});
			
			DDLTemplateParam param = DDLTemplateParam.builder()
				.dbType(convertDb.getDbVenderName())
				.schema(dataParamInfo.getSchema())
				.objectName(name)
				.ddlOpt(ddlOption)
				.columnList(columnList)
				.keyList(dbInstanceFactory.getConstraintsKeys(dataParamInfo,name))
				.commentsList(commentsList)
			.build();
			
			param.setSourceText(DDLTemplateFactory.getInstance().render(convertDb, DDLTemplateCode.TABLE.constraintKey, param));
			
			ddlStrBuf.append(DDLTemplateFactory.getInstance().render(convertDb, DDLTemplateCode.TABLE.createConversion, param, DDLTemplateCode.TABLE.create));
			
			ddlInfo.setCreateScript(ddlStrBuf.toString());
			reval.add(ddlInfo);
		}

		return reval;
	}

	@Override
	public String getStandardDefaultValue(String val, DataType type) {
		if(com.vartech.common.utils.StringUtils.isBlank(val)) {
			return val ;
		}
		String lowerVal = val.toLowerCase().trim();
		
		if(STANDARD_DEFAULT_VALUE.containsKey(lowerVal)) {
			return STANDARD_DEFAULT_VALUE.get(lowerVal); 
		}
		
		return val;
	}
	
	@Override
	public String getDefaultValueToVenderValue(String val, DataType type) {
		return DEFAULT_VALUE_TO_VENDER_VALUE.containsKey(val) ? DEFAULT_VALUE_TO_VENDER_VALUE.get(val) : val; 
	}
}
