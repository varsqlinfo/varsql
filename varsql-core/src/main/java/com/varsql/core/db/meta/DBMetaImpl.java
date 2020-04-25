package com.varsql.core.db.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.IndexInfo;
import com.varsql.core.db.valueobject.ObjectColumnInfo;
import com.varsql.core.db.valueobject.ObjectInfo;
import com.varsql.core.db.valueobject.ServiceObject;
import com.varsql.core.db.valueobject.TableInfo;
import com.varsql.core.sql.util.SQLUtil;
import com.vartech.common.utils.VartechReflectionUtils;


/**
 * 
 * @FileName : DBMetaImpl.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 13.
 * @프로그램설명: db meta 정보 가져오기.
 * @변경이력	: 
 */
public abstract class DBMetaImpl implements DBMeta{
	
	private static Logger logger = LoggerFactory.getLogger(DBMetaImpl.class);
	
	protected MetaControlBean dbInstanceFactory;
	protected List<ServiceObject> serviceMenu =new LinkedList<ServiceObject>();
	
	private DBMetaDataUtil dBMetaDataUtil = new DBMetaDataUtil();
	
	protected DBMetaImpl(MetaControlBean dbInstanceFactory, ServiceObject... serviceObjectArr){
		this(dbInstanceFactory, true, serviceObjectArr);
	}
	
	protected DBMetaImpl(MetaControlBean dbInstanceFactory, boolean baseMenuFlag, ServiceObject... serviceObjectArr){
		this.dbInstanceFactory = dbInstanceFactory;
		if(baseMenuFlag){
			addBaseMenu();
		}
		
		addServiceMenu(serviceObjectArr);
	}
	
	private void addBaseMenu() {
		addServiceMenu(
			new ServiceObject(ObjectType.TABLE),
			new ServiceObject(ObjectType.VIEW),
			new ServiceObject(ObjectType.FUNCTION),
			new ServiceObject(ObjectType.INDEX)
		);
	}
	
	private void addServiceMenu(ServiceObject... serviceObjectArr) {
		for (ServiceObject serviceObj : serviceObjectArr) {
			serviceMenu.add(serviceObj);
		}		
	}
	
	public List<ServiceObject> getServiceMenu(){
		return serviceMenu;
	}
	
	@Override
	public List getVersion(DatabaseParamInfo dataParamInfo)  {
		
		HashMap reMap = new HashMap();
		
		Connection conn = SQLManager.getInstance().getConnection(dataParamInfo.getVconnid()); 
		
		List result = new ArrayList();
		try {
			DatabaseMetaData  meta = conn.getMetaData();
			
			result.add( new HashMap() {{
				put("VERSIONINFO",meta.getDatabaseProductVersion());
			}});
			
			result.add( new HashMap() {{
				put("DRIVERINFO",meta.getDriverMajorVersion());
			}});
		
		}catch(SQLException e){
			logger.error("getVersion {} ",e.getMessage());
			logger.error("getVersion ",e);
		}finally{
			SQLUtil.close(conn, null, null);
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
	 * @throws Exception
	 */
	@Override
	public List<String> getSchemas(DatabaseParamInfo dataParamInfo){
		List<String> reLst = new ArrayList<String>();
		
		String dbAlias =  dataParamInfo.getVconnid();
		
		logger.debug("getSchemas connid: {}",dbAlias);
	
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = SQLManager.getInstance().getConnection(dbAlias);
			
			DatabaseMetaData dbmd = conn.getMetaData();
			
			String uSchema =dataParamInfo.getSchema();
			try {
				uSchema = conn.getSchema();
			}catch(Throwable e1) {
				try {
					uSchema = dbmd.getUserName();
				}catch(Throwable e3) {};
			}
			if(SecurityUtil.isSchemaView(dataParamInfo)) {
				
				rs =dbmd.getSchemas();
				
				while (rs.next()) {
					reLst.add(rs.getString(MetaColumnConstants.TABLE_SCHEM));
				}
				if (rs != null) rs.close();
			}else{
				reLst.add(uSchema);
			}
		} catch (SQLException e) {
			logger.error("getSchemas {} ",e.getMessage());
			logger.error("getSchemas",e);
		} finally{
			SQLUtil.close(conn, null ,rs); 
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
		
		Connection conn = SQLManager.getInstance().getConnection(dataParamInfo.getVconnid()); 
		
		try {
			return dBMetaDataUtil.tableInfo(dataParamInfo,conn, "TABLE");
		}finally{
			SQLUtil.close(conn, null, null);
		}
	}
	
	public List<TableInfo> getTableMetadata(DatabaseParamInfo dataParamInfo, String... tableArr) throws Exception {
		
		logger.debug("DBMetaImpl getTableMetadata {}  tableArr :: {}", VartechReflectionUtils.reflectionToString(dataParamInfo), tableArr);
		
		Connection conn = SQLManager.getInstance().getConnection(dataParamInfo.getVconnid());
		try {
			List<TableInfo> tableList = null; 
			if(tableArr !=null  && tableArr.length > 0){
				tableList = new ArrayList<TableInfo>();
				
				TableInfo tableInfo = null; 
				for (String nm :tableArr) {
					tableInfo = new TableInfo();
					
					tableInfo.setName(nm);
					tableInfo.setRemarks("");
					
					tableList.add(tableInfo);
				}
			}else{
				tableList = dBMetaDataUtil.tableInfo(dataParamInfo, conn ,"TABLE");
			}
			return dBMetaDataUtil.tableAndColumnsInfo(dataParamInfo,conn, dbInstanceFactory,"TABLE" ,tableList);
		}finally{
			SQLUtil.close(conn, null, null);
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
		Connection conn = SQLManager.getInstance().getConnection(dataParamInfo.getVconnid());
		
		try {
			return dBMetaDataUtil.tableInfo(dataParamInfo,conn, "VIEW");
		}finally{
			SQLUtil.close(conn, null, null);
		}
	}
	
	@Override
	public List<TableInfo> getViewMetadata(DatabaseParamInfo dataParamInfo, String... viewArr) throws Exception{
		Connection conn = SQLManager.getInstance().getConnection(dataParamInfo.getVconnid());
		try {
			List<TableInfo> tableList = null; 
			if(viewArr !=null  && viewArr.length > 0){
				tableList = new ArrayList<TableInfo>();
				
				TableInfo tableInfo = null; 
				for (String nm :viewArr) {
					tableInfo = new TableInfo();
					
					tableInfo.setName(nm);
					tableList.add(tableInfo);
				}
			}else{
				tableList = dBMetaDataUtil.tableInfo(dataParamInfo,conn, "VIEW");
			}
			return dBMetaDataUtil.tableAndColumnsInfo(dataParamInfo, conn, dbInstanceFactory, "VIEW" ,tableList);
		}finally{
			SQLUtil.close(conn, null, null);
		}
	}
	
	/**
	 * 
	 * @Method Name  : getProcedures
	 * @Method 설명 : 프로시저 정보 목록 보기.
	 * @Method override : @see com.varsql.core.db.meta.DBMeta#getProcedures(com.varsql.core.db.beans.DatabaseParamInfo, java.lang.String[])
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
		Connection conn = SQLManager.getInstance().getConnection(dataParamInfo.getVconnid());
		try {
			return dBMetaDataUtil.proceduresInfo(dataParamInfo,conn);
		}finally{
			SQLUtil.close(conn, null, null);
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
	public List<ObjectInfo> getProcedureMetadata(DatabaseParamInfo dataParamInfo, String... proceduresArr) throws Exception{
		
		Connection conn = SQLManager.getInstance().getConnection(dataParamInfo.getVconnid());
		try {
			List<ObjectInfo> objectInfoList = null; 
			if(proceduresArr==null){
				objectInfoList = dBMetaDataUtil.proceduresInfo(dataParamInfo,conn);
			}else{
				objectInfoList = new ArrayList<ObjectInfo>();
				
				ObjectInfo objInfo = null; 
				for (String nm :proceduresArr) {
					objInfo = new ObjectInfo();
					objInfo.setName(nm);
					objectInfoList.add(objInfo);
				}
			}
			
			return dBMetaDataUtil.proceduresAndMetadatas(dataParamInfo, conn, dbInstanceFactory,objectInfoList);
		}finally{
			SQLUtil.close(conn, null, null);
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
		Connection conn = SQLManager.getInstance().getConnection(dataParamInfo.getVconnid());
		try {
			return dBMetaDataUtil.functionInfo(dataParamInfo,conn);
		}finally{
			SQLUtil.close(conn, null, null);
		}
	}
	
	/**
	 * 
	 * @Method Name  : getFunctionMetadata
	 * @Method 설명 : function 정보 보기
	 * @Method override : @see com.varsql.core.db.meta.DBMeta#getFunctionMetadata(com.varsql.core.db.beans.DatabaseParamInfo, java.lang.String[])
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 29. 
	 * @변경이력  :
	 * @param dataParamInfo
	 * @param functionNm
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ObjectInfo> getFunctionMetadata(DatabaseParamInfo dataParamInfo, String... functionNm)
			throws Exception {
		Connection conn = SQLManager.getInstance().getConnection(dataParamInfo.getVconnid());
		try {
			List<ObjectInfo> objectInfoList = null; 
			if(functionNm!=null && functionNm.length > 0){
				objectInfoList = new ArrayList<ObjectInfo>();
				
				ObjectInfo objInfo = null; 
				for (String nm :functionNm) {
					objInfo = new ObjectInfo();
					objInfo.setName(nm);
					objectInfoList.add(objInfo);
				}
			}else{
				objectInfoList = dBMetaDataUtil.functionInfo(dataParamInfo,conn);
			}
			
			return dBMetaDataUtil.functionAndMetadatas(dataParamInfo, conn, dbInstanceFactory,objectInfoList);
		}finally{
			SQLUtil.close(conn, null, null);
		}
	}
	
	@Override
	public List getUsers(DatabaseParamInfo dataParamInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List getSequences(DatabaseParamInfo dataParamInfo)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List getSequenceMetadata(DatabaseParamInfo dataParamInfo, String ...sequenceArr)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List getIndexs(DatabaseParamInfo dataParamInfo) throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		List<Map> keyColumn = new ArrayList<Map>();
		
		try {
			conn = SQLManager.getInstance().getConnection(dataParamInfo.getVconnid());
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
		}finally{
			SQLUtil.close(conn, null ,rs);
		}
		return keyColumn; 
	}
	
	@Override
	public List<IndexInfo> getIndexMetadata(DatabaseParamInfo dataParamInfo, String... indexNmArr) throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		List<IndexInfo> indexInfoList = new ArrayList<IndexInfo>();
		String dbAlias =  dataParamInfo.getVconnid();
		String schema = dataParamInfo.getSchema();
		
		try {
			conn = SQLManager.getInstance().getConnection(dbAlias);
			DatabaseMetaData dbmd = conn.getMetaData();
			
			List<TableInfo> tableList;
			if(indexNmArr != null  && indexNmArr.length > 0 ){
				tableList = new ArrayList<TableInfo>();
				
				TableInfo tableInfo = null; 
				for (String nm :indexNmArr) {
					tableInfo = new TableInfo();
					
					tableInfo.setName(nm);
					tableList.add(tableInfo);
				}
			}else{
				tableList = dBMetaDataUtil.tableInfo(dataParamInfo,conn, "TABLE");
				
			}
			
			ObjectColumnInfo oci = null;
			
			List<ObjectColumnInfo> columnList = null;
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
		}finally{
			SQLUtil.close(conn, null ,rs);
		}
		return indexInfoList; 
	}
	
	@Override
	public List getTriggers(DatabaseParamInfo dataParamInfo) throws Exception {
		return null; 
	}
	
	@Override
	public List getTriggerMetadata(DatabaseParamInfo dataParamInfo, String... triggerArr) throws Exception {
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
			SQLUtil.close(pstmt ,rs);
		}
		return reval;
	}
}
