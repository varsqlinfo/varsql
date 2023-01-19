package com.varsql.core.db.ddl.script;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.TableInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.sql.SQLTemplateCode;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.varsql.core.sql.util.JdbcUtils;

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

	protected AbstractDDLScript(MetaControlBean dbInstanceFactory, DBVenderType dbType){
		this.dbInstanceFactory =  dbInstanceFactory;
		this.dbType =  dbType;
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

	protected Map getDefaultTemplateParam(DDLCreateOption ddlOption, DatabaseParamInfo dataParamInfo, List listMap) {
		Map param =  new HashMap();
		param.put("ddlOpt", ddlOption);
		param.put("dbType", dataParamInfo.getDbType());
		param.put("schema", dataParamInfo.getSchema());
		param.put("objectName", dataParamInfo.getObjectName());
		param.put("items", listMap);

		return param;
	}

	protected Map getDefaultTableTemplateParam(DDLCreateOption ddlOption, DatabaseParamInfo dataParamInfo, List columnList, List keyList, List commentsList) {
		Map param =  new HashMap();
		param.put("ddlOpt", ddlOption);
		param.put("dbType", dataParamInfo.getDbType());
		param.put("schema", dataParamInfo.getSchema());
		param.put("objectName", dataParamInfo.getObjectName());
		param.put("columnList", columnList);
		param.put("keyList", keyList);
		param.put("commentsList", commentsList);

		return param;
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
	 * @param objNm
	 * @return
	 * @throws Exception
	 */
	public List<DDLInfo> getTables(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {

		List<TableInfo> tableInfoList = dbInstanceFactory.getDBObjectMeta(ObjectType.TABLE.getObjectTypeId(),dataParamInfo,objNmArr );

		StringBuilder ddlStrBuf;

		List<DDLInfo> reval = new ArrayList<DDLInfo>();

		DDLInfo ddlInfo;
		for(TableInfo tableInfo: tableInfoList){

			String name = tableInfo.getName();
			dataParamInfo.setObjectName(name);

			ddlInfo  = new DDLInfo();
			ddlInfo.setName(name);
			ddlStrBuf = new StringBuilder();

			if(ddlOption.isAddDropClause()){
				ddlStrBuf.append("/* DROP TABLE " + name+ " CASCADE CONSTRAINT; */ ").append(BlankConstants.NEW_LINE_TWO);
			}

			List<ColumnInfo> columnList =tableInfo.getColList();

			ddlStrBuf.append("CREATE TABLE " + name + "(").append( BlankConstants.NEW_LINE);

			DataType dataTypeInfo;
			ColumnInfo columnInfo;
			for (int i = 0; i < columnList.size(); i++) {
				columnInfo =  columnList.get(i);

				dataTypeInfo = dbInstanceFactory.getDataTypeImpl().getDataType(columnInfo.getTypeName());

				ddlStrBuf.append(BlankConstants.TAB);
				if (i > 0){
					ddlStrBuf.append(",");
				}

				ddlStrBuf.append(columnInfo.getName()).append(" ");

				ddlStrBuf.append(columnInfo.getTypeAndLength());

				ddlStrBuf.append(getDefaultValue(columnInfo.getDefaultVal(), dataTypeInfo));

				ddlStrBuf.append(getNotNullValue(columnInfo.getNullable()));

				ddlStrBuf.append(BlankConstants.NEW_LINE);
			}

			List<Map> keyInfo = getTablePrimaryKeyInfo(dataParamInfo, name);

			if(keyInfo.size() > 0){
				Map source;

				ddlStrBuf.append(BlankConstants.TAB).append(",CONSTRAINT ")
				.append(keyInfo.get(0).get(MetaColumnConstants.PK_NAME))
				.append(" PRIMARY KEY  ( ");
				for (int i = 0; i < keyInfo.size(); i++) {
					source =  keyInfo.get(i);
					ddlStrBuf.append(i==0?"":", ").append(source.get(MetaColumnConstants.COLUMN_NAME));
				}
				ddlStrBuf.append(")").append(BlankConstants.NEW_LINE);
			}

			ddlStrBuf.append(")").append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);

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
	 * @param objNm
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getViews(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {

		StringBuilder ddlStrBuf;

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());
		for (String viewNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(viewNm);

			ddlStrBuf = new StringBuilder();

			dataParamInfo.setObjectName(viewNm);

			if(ddlOption.isAddDropClause()){
				ddlStrBuf.append("/* DROP VIEW " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}
			ddlStrBuf.append("CREATE TABLE " + viewNm + " as ").append( BlankConstants.NEW_LINE);

			List<String> srcProcList = sqlSesseion.selectList("viewScript", dataParamInfo);
			for (int j = 0; j < srcProcList.size(); j++) {
				ddlStrBuf.append( srcProcList.get(j));
			}

			ddlStrBuf.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);

			ddlInfo.setCreateScript(ddlStrBuf.toString());
			reval.add(ddlInfo);
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
	 * @param objNm
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getIndexs(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {
		StringBuilder ddlStrBuf;

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());
		for (String viewNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(viewNm);

			ddlStrBuf = new StringBuilder();

			dataParamInfo.setObjectName(viewNm);

			if(ddlOption.isAddDropClause()){
				ddlStrBuf.append("/* DROP INDEX " + dataParamInfo.getObjectName() + "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			List<String> srcProcList = sqlSesseion.selectList("indexScript", dataParamInfo);
			for (int j = 0; j < srcProcList.size(); j++) {
				ddlStrBuf.append( srcProcList.get(j));
			}

			ddlStrBuf.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);

			ddlInfo.setCreateScript(ddlStrBuf.toString());
			reval.add(ddlInfo);
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
	 * @param objNm
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getFunctions(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {

		StringBuilder ddlStrBuf;

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());
		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			ddlStrBuf = new StringBuilder();

			dataParamInfo.setObjectName(objNm);
			if(ddlOption.isAddDropClause()){
				ddlStrBuf.append("/* DROP FUNCTION " + objNm + "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			List<String> srcProcList = sqlSesseion.selectList("functionScript", dataParamInfo);
			for (int j = 0; j < srcProcList.size(); j++) {
				ddlStrBuf.append(srcProcList.get(j));
			}
			ddlStrBuf.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);

			ddlInfo.setCreateScript(ddlStrBuf.toString());
			reval.add(ddlInfo);
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
	 * @param objNm
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getProcedures(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr)	throws Exception {

		StringBuilder ddlStrBuf;

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());
		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			ddlStrBuf = new StringBuilder();

			dataParamInfo.setObjectName(objNm);

			if(ddlOption.isAddDropClause()){
				ddlStrBuf.append("/* DROP PROCEDURE " + objNm+ "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			List<String> srcProcList = sqlSesseion.selectList("procedureScript", dataParamInfo);
			for (int j = 0; j < srcProcList.size(); j++) {
				ddlStrBuf.append(srcProcList.get(j));
			}
			ddlStrBuf.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);

			ddlInfo.setCreateScript(ddlStrBuf.toString());
			reval.add(ddlInfo);
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
	 * @param objNm
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getTriggers(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {

		StringBuilder ddlStrBuf;

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());
		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			ddlStrBuf = new StringBuilder();

			dataParamInfo.setObjectName(objNm);

			if(ddlOption.isAddDropClause()){
				ddlStrBuf.append("/* DROP TRIGGER " + objNm + "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			List<String> srcProcList = sqlSesseion.selectList("triggerScript", dataParamInfo);
			for (int j = 0; j < srcProcList.size(); j++) {
				ddlStrBuf.append(srcProcList.get(j));
			}
			ddlStrBuf.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);

			ddlInfo.setCreateScript(ddlStrBuf.toString());
			reval.add(ddlInfo);
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
	 * @param objNm
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DDLInfo> getSequences(DatabaseParamInfo dataParamInfo, DDLCreateOption ddlOption, String ...objNmArr) throws Exception {
		StringBuilder ddlStrBuf;

		List<DDLInfo> reval = new ArrayList<DDLInfo>();
		DDLInfo ddlInfo;
		SqlSession sqlSesseion = SQLManager.getInstance().sqlSessionTemplate(dataParamInfo.getVconnid());
		for (String objNm : objNmArr) {

			ddlInfo = new DDLInfo();
			ddlInfo.setName(objNm);
			ddlStrBuf = new StringBuilder();

			dataParamInfo.setObjectName(objNm);

			if(ddlOption.isAddDropClause()){
				ddlStrBuf.append("/* DROP SEQUENCE " + objNm + "; */").append(BlankConstants.NEW_LINE_TWO);
			}

			Map param = sqlSesseion.selectOne("sequenceScript", dataParamInfo);

			param.put("schema", dataParamInfo.getSchema());

			param.put("ddlOption", ddlOption);

			ddlStrBuf.append(SQLTemplateFactory.getInstance().sqlRender(DBVenderType.OTHER, SQLTemplateCode.SEQUENCE.create, param));

			ddlStrBuf.append(ddlOption.isAddLastSemicolon()?";":"").append(BlankConstants.NEW_LINE_TWO);

			ddlInfo.setCreateScript(ddlStrBuf.toString());
			reval.add(ddlInfo);
		}

		return reval;
	}

	private List<Map> getTablePrimaryKeyInfo(DatabaseParamInfo dataParamInfo, String tableNm) throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		List<Map> keyColumn = new ArrayList<Map>();
		String dbAlias =  dataParamInfo.getVconnid();
		String schema = dataParamInfo.getSchema();

		SqlSession session  = SQLManager.getInstance().openSession(dbAlias);

		try {
			conn = session.getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();

			rs = dbmd.getPrimaryKeys(null, schema, tableNm);

			Map tc = null;
			while(rs.next()){
				tc =new HashMap();
				tc.put(MetaColumnConstants.COLUMN_NAME, rs.getString(MetaColumnConstants.COLUMN_NAME));
				tc.put(MetaColumnConstants.PK_NAME, rs.getString(MetaColumnConstants.PK_NAME));
				tc.put(MetaColumnConstants.KEY_SEQ, rs.getString(MetaColumnConstants.KEY_SEQ));
				keyColumn.add(tc);
			}
		} catch (SQLException e) {
			throw e;
		}finally{
			SQLManager.getInstance().closeSession(dbAlias, session);
			JdbcUtils.close(conn, null ,rs);
		}
		return keyColumn;
	}

}
