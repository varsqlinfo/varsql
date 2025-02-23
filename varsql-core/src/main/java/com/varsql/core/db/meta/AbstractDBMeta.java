package com.varsql.core.db.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.db.servicemenu.DBObjectType;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.ConstraintInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.IndexInfo;
import com.varsql.core.db.valueobject.ObjectColumnInfo;
import com.varsql.core.db.valueobject.ObjectInfo;
import com.varsql.core.db.valueobject.ServiceObject;
import com.varsql.core.db.valueobject.TableInfo;
import com.varsql.core.sql.ConstraintType;
import com.varsql.core.sql.util.JdbcUtils;
import com.vartech.common.utils.VartechReflectionUtils;


/**
 *
 * @FileName : DBMetaImpl.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 13.
 * @프로그램설명: db meta 정보 가져오기.
 * @변경이력	:
 */
public abstract class AbstractDBMeta implements DBMeta{

	private final Logger logger = LoggerFactory.getLogger(AbstractDBMeta.class);

	protected MetaControlBean dbInstanceFactory;
	protected List<ServiceObject> serviceMenu =new LinkedList<ServiceObject>();
	protected List<DBVersionInfo> dbVersionInfo =new LinkedList<DBVersionInfo>();
	protected DBVersionInfo defaultVersion;

	private DBMetaDataUtil dBMetaDataUtil = new DBMetaDataUtil();

	protected AbstractDBMeta(MetaControlBean dbInstanceFactory, ServiceObject[] serviceObjectArr){
		this(dbInstanceFactory, serviceObjectArr, null);
	}
	
	protected AbstractDBMeta(MetaControlBean dbInstanceFactory, ServiceObject[] serviceObjectArr, DBVersionInfo[] versionArr){
		
		this.dbInstanceFactory = dbInstanceFactory;
		if(serviceObjectArr==null){
			addBaseMenu();
		}else {
			addServiceMenu(serviceObjectArr);
		}
		
		if(versionArr != null) {
			for (DBVersionInfo versionInfo : versionArr) {
				dbVersionInfo.add(versionInfo);
				
				if(versionInfo.isDefultFlag()) {
					this.defaultVersion = versionInfo;
				}
			}
			
			if(this.defaultVersion ==null) {
				this.defaultVersion = versionArr[versionArr.length -1];
			}
		}else {
			this.defaultVersion = DBVersionInfo.builder(-1, -1, -1).defultFlag(true).build();
		}
	}

	private void addBaseMenu() {
		addServiceMenu(
			new ServiceObject(DBObjectType.TABLE),
			new ServiceObject(DBObjectType.VIEW)
		);
	}

	private void addServiceMenu(ServiceObject... serviceObjectArr) {
		for (ServiceObject serviceObj : serviceObjectArr) {
			serviceMenu.add(serviceObj);
		}
	}
	
	public List<DBVersionInfo> getVenderVersionInfo() {
		return dbVersionInfo;
	}
	
	public DBVersionInfo getDefaultVenderVersion() {
		return defaultVersion;
	}

	public List<ServiceObject> getServiceMenu(){
		return serviceMenu;
	}

	@Override
	public List getVersion(DatabaseParamInfo dataParamInfo) throws Exception  {

		List<Map> result = new ArrayList();

		try(SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			DatabaseMetaData  meta = session.getConnection().getMetaData();
			result.add( Collections.singletonMap("VERSIONINFO", meta.getDatabaseProductVersion()));
			result.add( Collections.singletonMap("DRIVERINFO", meta.getDriverMajorVersion()));
		}

		return result;
	}

	/**
	 *
	 * @Method Name  : getSchemas
	 * @Method 설명 : 스크마 정보 가져오기.
	 * @Method override : @see com.varsql.db.meta.DBMeta#getSchemas(java.lang.String)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 19.
	 * @변경이력  :
	 * @param dbAlias
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	@Override
	public List<String> getSchemas(DatabaseParamInfo dataParamInfo) throws SQLException{
		List<String> reLst = new ArrayList<String>();

		String dbAlias =  dataParamInfo.getVconnid();

		logger.debug("getSchemas connid: {} , shcema : {}",dbAlias , dataParamInfo.getSchema());

		Connection conn = null;
		ResultSet rs = null;

		try (SqlSession session = SQLManager.getInstance().getSqlSession(dbAlias)){
			conn = session.getConnection();

			DatabaseMetaData dbmd = conn.getMetaData();

			String uSchema =dataParamInfo.getSchema();
			try {
				uSchema = conn.getSchema();
			}catch(Throwable e1) {
				try {
					uSchema = dbmd.getUserName();
				}catch(Throwable e3) {};
			}
			if(DbMetaUtils.isSchemaView(dataParamInfo)) {

				rs =dbmd.getSchemas();

				while (rs.next()) {
					reLst.add(rs.getString(MetaColumnConstants.TABLE_SCHEM));
				}
				if (rs != null) rs.close();
			}else{
				reLst.add(uSchema);
			}
		}
		
		return reLst;
	}
	
	@Override
	public List<String> getDatabases(DatabaseParamInfo dataParamInfo) throws SQLException {
		List<String> reLst = new ArrayList<String>();

		String dbAlias =  dataParamInfo.getVconnid();

		logger.debug("getDatabases connid: {}, shcema : {}, databaseName : {} ", dbAlias, dataParamInfo.getSchema(), dataParamInfo.getDatabaseName());

		Connection conn = null;
		ResultSet rs = null;

		try (SqlSession session = SQLManager.getInstance().getSqlSession(dbAlias);){
			conn = session.getConnection();

			DatabaseMetaData dbmd = conn.getMetaData();

			String databaseName =dataParamInfo.getDatabaseName();
			try {
				databaseName = conn.getCatalog();
			}catch(Throwable e1) {
				
			}
			
			if(DbMetaUtils.isSchemaView(dataParamInfo)) {

				rs =dbmd.getCatalogs();
				if(rs!=null) {
					while (rs.next()) {
						reLst.add(rs.getString(MetaColumnConstants.TABLE_CAT));
					}
					if (rs != null) rs.close();
				}
			}else{
				reLst.add(databaseName);
			}
		}finally {
			JdbcUtils.close(rs);
		}
		
		return reLst;
	}

	/**
	 *
	 * @Method Name  : getTables
	 * @Method 설명 : 테이블 정보가져오기.
	 * @Method override : @see com.varsql.db.meta.DBMeta#getTables(java.lang.String)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 19.
	 * @변경이력  :
	 * @param dbAlias
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TableInfo> getTables(DatabaseParamInfo dataParamInfo) throws Exception{

		logger.debug("DBMetaImpl getTables {}  ", VartechReflectionUtils.reflectionToString(dataParamInfo));

		try(SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			Connection conn = session.getConnection();
			return dBMetaDataUtil.tableInfo(dataParamInfo, conn, "TABLE");
		}
	}

	public List<TableInfo> getTableMetadata(DatabaseParamInfo dataParamInfo, String... tableNames) throws Exception {

		logger.debug("DBMetaImpl getTableMetadata {}  tableArr :: {}", VartechReflectionUtils.reflectionToString(dataParamInfo), tableNames);
		try(SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			Connection conn = session.getConnection();

			List<TableInfo> tableList = null;
			if(tableNames !=null  && tableNames.length > 0){
				tableList = new ArrayList<TableInfo>();

				TableInfo tableInfo = null;
				for (String nm :tableNames) {
					tableInfo = new TableInfo();

					tableInfo.setName(nm);
					tableInfo.setRemarks("");

					tableList.add(tableInfo);
				}
			}else{
				tableList = dBMetaDataUtil.tableInfo(dataParamInfo, conn ,"TABLE");
			}

			return dBMetaDataUtil.tableAndColumnsInfo(dataParamInfo,conn, dbInstanceFactory,"TABLE" ,tableList);
		}
	}

	/**
	 *
	 * @Method Name  : getViews
	 * @Method 설명 : view 정보 가져오기.
	 * @Method override : @see com.varsql.db.meta.DBMeta#getViews(java.lang.String)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 19.
	 * @변경이력  :
	 * @param dbAlias
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TableInfo> getViews(DatabaseParamInfo dataParamInfo) throws Exception{
		try(SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			Connection conn = session.getConnection();
			return dBMetaDataUtil.tableInfo(dataParamInfo, conn, "VIEW");
		}
	}

	@Override
	public List<TableInfo> getViewMetadata(DatabaseParamInfo dataParamInfo, String... viewNames) throws Exception{
		try (SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());) {
			Connection conn = session.getConnection();
			List<TableInfo> tableList = null;
			if (viewNames != null && viewNames.length > 0) {
				tableList = new ArrayList<TableInfo>();

				TableInfo tableInfo = null;
				for (String nm : viewNames) {
					tableInfo = new TableInfo();

					tableInfo.setName(nm);
					tableList.add(tableInfo);
				}
			} else {
				tableList = dBMetaDataUtil.tableInfo(dataParamInfo, conn, "VIEW");
			}
			return dBMetaDataUtil.tableAndColumnsInfo(dataParamInfo, conn, dbInstanceFactory, "VIEW", tableList);
		}
	}

	/**
	 *
	 * @Method Name  : getProcedures
	 * @Method 설명 : 프로시저 정보 목록 보기.
	 * @Method override : @see com.varsql.core.db.meta.DBMeta#getProcedures(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 29.
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param proceduresArr
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ObjectInfo> getProcedures(DatabaseParamInfo dataParamInfo) throws Exception {
		try(SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			Connection conn = session.getConnection();
			return dBMetaDataUtil.proceduresInfo(dataParamInfo, conn);
		}
	}

	/**
	 *
	 * @Method Name  : getProcedureMetadata
	 * @Method 설명 : 프로시져 정보 가져오기.
	 * @Method override : @see com.varsql.db.meta.DBMeta#getProcedures(java.lang.String)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 19.
	 * @변경이력  :
	 * @param dbAlias
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ObjectInfo> getProcedureMetadata(DatabaseParamInfo dataParamInfo, String... procedureNames) throws Exception{
		try(SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			Connection conn = session.getConnection();
			List<ObjectInfo> objectInfoList = null;
			if(procedureNames==null){
				objectInfoList = dBMetaDataUtil.proceduresInfo(dataParamInfo,conn);
			}else{
				objectInfoList = new ArrayList<ObjectInfo>();

				ObjectInfo objInfo = null;
				for (String nm :procedureNames) {
					objInfo = new ObjectInfo();
					objInfo.setName(nm);
					objectInfoList.add(objInfo);
				}
			}

			return dBMetaDataUtil.proceduresAndMetadatas(dataParamInfo, conn, dbInstanceFactory,objectInfoList);
		}
	}

	/**
	 *
	 * @Method Name  : getFunctions
	 * @Method 설명 : 스키마에 해당하는 function name 가져오기.
	 * @Method override : @see com.varsql.db.meta.DBMeta#getFunctions(java.lang.String, java.lang.String)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 6. 19.
	 * @변경이력  :
	 * @param dbAlias
	 * @param schema
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ObjectInfo> getFunctions(DatabaseParamInfo dataParamInfo) throws Exception {
		try(SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){
			Connection conn = session.getConnection();
			return dBMetaDataUtil.functionInfo(dataParamInfo,conn);
		}

	}

	/**
	 *
	 * @Method Name  : getFunctionMetadata
	 * @Method 설명 : function 정보 보기
	 * @Method override : @see com.varsql.core.db.meta.DBMeta#getFunctionMetadata(com.varsql.core.db.valueobject.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 29.
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param functionNames
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ObjectInfo> getFunctionMetadata(DatabaseParamInfo dataParamInfo, String... functionNames)
			throws Exception {
		try(SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			Connection conn = session.getConnection();
			List<ObjectInfo> objectInfoList = null;
			if(functionNames!=null && functionNames.length > 0){
				objectInfoList = new ArrayList<ObjectInfo>();

				ObjectInfo objInfo = null;
				for (String nm :functionNames) {
					objInfo = new ObjectInfo();
					objInfo.setName(nm);
					objectInfoList.add(objInfo);
				}
			}else{
				objectInfoList = dBMetaDataUtil.functionInfo(dataParamInfo,conn);
			}

			return dBMetaDataUtil.functionAndMetadatas(dataParamInfo, conn, dbInstanceFactory,objectInfoList);
		}
	}

	@Override
	public List getUsers(DatabaseParamInfo dataParamInfo) throws Exception {
		return null;
	}

	@Override
	public List getSequences(DatabaseParamInfo dataParamInfo)
			throws Exception {
		return null;
	}

	@Override
	public List getSequenceMetadata(DatabaseParamInfo dataParamInfo, String ...sequenceNames)
			throws Exception {
		return null;
	}

	@Override
	public List getIndexs(DatabaseParamInfo dataParamInfo) throws Exception {

		try(SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			Connection conn = null;
			ResultSet rs = null;
			List<Map> keyColumn = new ArrayList<Map>();

			try {
				conn = session.getConnection();
				DatabaseMetaData dbmd = conn.getMetaData();

				rs = dbmd.getIndexInfo(null,dataParamInfo.getSchema(), "%" , false , false);

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
			}
			return keyColumn;
		}
	}

	@Override
	public List<IndexInfo> getIndexMetadata(DatabaseParamInfo dataParamInfo, String... indexNames) throws Exception {
		try(SqlSession session = SQLManager.getInstance().getSqlSession(dataParamInfo.getVconnid());){

			Connection conn = null;
			ResultSet rs = null;
			List<IndexInfo> indexInfoList = new ArrayList<IndexInfo>();
			String schema = dataParamInfo.getSchema();

			try {
				conn = session.getConnection();
				DatabaseMetaData dbmd = conn.getMetaData();

				List<TableInfo> tableList;
				if(indexNames != null  && indexNames.length > 0 ){
					tableList = new ArrayList<TableInfo>();

					TableInfo tableInfo = null;
					for (String nm :indexNames) {
						tableInfo = new TableInfo();

						tableInfo.setName(nm);
						tableList.add(tableInfo);
					}
				}else{
					tableList = dBMetaDataUtil.tableInfo(dataParamInfo,conn, "TABLE");

				}

				ObjectColumnInfo oci = null;

				List<ObjectColumnInfo> columnList = new ArrayList<ObjectColumnInfo>();
				String tblName;
				for (int i = 0 , len = tableList.size(); i < len; i++) {
					tblName = tableList.get(i).getName();

					rs = dbmd.getIndexInfo(null,schema,  tblName , false , false);

					IndexInfo indexInfo;
					String indexName =null;
					String beforeName = "";
					while(rs.next()){

						indexName = rs.getString(MetaColumnConstants.INDEX_NAME);

						if(indexName != null  &&  !"".equals(indexName)){

							if(!beforeName.equals(indexName)){
								columnList = new ArrayList<ObjectColumnInfo>();
								indexInfo =new IndexInfo();
								indexInfo.setName(rs.getString(MetaColumnConstants.INDEX_NAME));
								indexInfo.setTblName(tblName);
								indexInfo.setType(rs.getString(MetaColumnConstants.INDEX_TYPE));
								indexInfo.setColList(columnList);

								indexInfoList.add(indexInfo);
							}

							beforeName = indexName;
							oci = new ObjectColumnInfo();

							oci.setName(rs.getString(MetaColumnConstants.COLUMN_NAME));
							oci.setNo(rs.getInt(MetaColumnConstants.ORDINAL_POSITION));
							oci.setAscOrdesc(rs.getString(MetaColumnConstants.ASC_OR_DESC));
							columnList.add(oci);
						}
					}
				}
			} catch (SQLException e) {
				throw e;
			}
			return indexInfoList;
		}
	}

	@Override
	public List getTriggers(DatabaseParamInfo dataParamInfo) throws Exception {
		return null;
	}

	@Override
	public List getTriggerMetadata(DatabaseParamInfo dataParamInfo, String... triggerNames) throws Exception {
		return null;
	}

	@Override
	public <T>T getExtensionMetadata(DatabaseParamInfo dataParamInfo, String serviceName, Map param) throws Exception {
		return null;
	}
	@Override
	public <T>T getExtensionObject(DatabaseParamInfo dataParamInfo, String serviceName, Map param) throws Exception {
		return null;
	}

	protected Map getComments(Connection conn, String query) throws SQLException {
		return getComments(conn, query, null);
	}
	
	protected Map getComments(Connection conn, String query, String nm) throws SQLException {
		Map reval = new HashMap();
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		try {
			pstmt = conn.prepareStatement(query);
			rs =pstmt.executeQuery();
			while(rs.next()){
				reval.put(rs.getString("COMMENTS_KEY"), rs.getString("COMMENTS_DESC"));
			}
			rs.close();
		}finally{
			JdbcUtils.close(pstmt, rs);
		}
		return reval;
	}

	protected DatabaseParamInfo setObjectNameList(DatabaseParamInfo dataParamInfo, String ... names) {
		if(names != null && names.length > 0){
			StringBuilder sb =new StringBuilder();

			List<String> nameList = new ArrayList<String>();

			boolean  addFlag = false;
			for (int i = 0; i < names.length; i++) {
				sb.append(addFlag ? ",":"" ).append("'").append(names[i]).append("'");

				addFlag = true;
				if(i!=0 && (i+1)%1000==0){
					nameList.add(sb.toString());
					sb =new StringBuilder();
					addFlag = false;
				}
			}

			if(sb.length() > 0){
				nameList.add(sb.toString());
			}

			dataParamInfo.addCustom(OBJECT_NAME_LIST_KEY, nameList);
		}
		return dataParamInfo;
	}

	@Override
	public List<ConstraintInfo> getConstraintsKeys(DatabaseParamInfo dataParamInfo, String tableNm) throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		List<ConstraintInfo> keyColumn = new LinkedList<ConstraintInfo>();
		String dbAlias =  dataParamInfo.getVconnid();
		String schema = dataParamInfo.getSchema();

		try(SqlSession session  = SQLManager.getInstance().getSqlSession(dbAlias);) {
			conn = session.getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();

			rs = dbmd.getPrimaryKeys(null, schema, tableNm);

			while(rs.next()){
				keyColumn.add(ConstraintInfo.builder()
				.type(ConstraintType.PRIMARY.getType())
				.columnName(rs.getString(MetaColumnConstants.COLUMN_NAME))
				.constraintName(rs.getString(MetaColumnConstants.PK_NAME))
				.seq(rs.getInt(MetaColumnConstants.KEY_SEQ))
				.build());
			}
		} catch (SQLException e) {
			throw e;
		}finally{
			JdbcUtils.close(conn, null, rs);
		}
		return keyColumn;
	}
}
