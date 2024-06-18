package com.varsql.core.changeset;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.changeset.beans.ChangeLogDTO;
import com.varsql.core.changeset.beans.ChangeSetInfo;
import com.varsql.core.changeset.beans.ChangeSql;
import com.varsql.core.changeset.data.ChangeSetData;
import com.varsql.core.changeset.data.ChangeSetDataDB;
import com.varsql.core.changeset.data.ChangeSetDataFile;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.util.JdbcUtils;
import com.vartech.common.io.RESOURCE_TYPE;
import com.vartech.common.io.Resource;
import com.vartech.common.utils.HashUtils;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

/**
 * db 변경 정보 적용 
 * 
 * @author ytkim
 *
 */
public class DatabaseChangeExecutor {
	
	private final Logger logger = LoggerFactory.getLogger(DatabaseChangeExecutor.class);
	
	private String resourcePath = RESOURCE_TYPE.CLASSPATH.getPath("config/db/changeset/%s/*.xml");
	private String logPath;
	private ConnectionInfo connectionInfo;
	private boolean failRevertFlag;
	
	public DatabaseChangeExecutor(ConnectionInfo connectionInfo) {
		this(connectionInfo, true, null);
	}
	
	public DatabaseChangeExecutor(ConnectionInfo connectionInfo, boolean failRevertFlag) {
		this(connectionInfo, failRevertFlag, null);
	}
	
	public DatabaseChangeExecutor(ConnectionInfo connectionInfo, boolean failRevertFlag, String logPath) {
		this(connectionInfo, failRevertFlag, logPath, null);
	}
	public DatabaseChangeExecutor(ConnectionInfo connectionInfo, boolean failRevertFlag, String logPath, String resourcePath) {
		this.connectionInfo = connectionInfo;
		this.failRevertFlag = failRevertFlag; 
		if(!StringUtils.isBlank(logPath)) this.logPath = logPath;
		if(!StringUtils.isBlank(resourcePath)) this.resourcePath = resourcePath;
	}
	
	public static void main(String[] args) {
		try {
			System.setProperty("com.varsql.resource.root", "c:/zzz/aavarsql");
			new DatabaseChangeExecutor(Configuration.getInstance().getVarsqlDB()).changeApply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void changeApply() throws IOException, Exception {
		ChangeSetData changeLogSQL;
		if(this.logPath != null) {
			changeLogSQL = new ChangeSetDataFile(this.logPath);
		}else {
			changeLogSQL = new ChangeSetDataDB(this.connectionInfo);
		}
		
		Resource[] changeXmls = null;
		
		String resourcePath = String.format(this.resourcePath, connectionInfo.getType().toLowerCase()); 
		try {
			changeXmls = ResourceUtils.getResources(resourcePath);
		} catch (IOException e) {
			logger.error("database changeset not found : {}, message : {}", resourcePath, e.getMessage());
			return ;
		}
		
		if(changeXmls == null || changeXmls.length < 1) {
			logger.info("change apply resource not found : {} ", resourcePath);
			return ; 
		}
		
		List<ChangeSetInfo> changeSetsList = new LinkedList<ChangeSetInfo>();
		
		Map<String, Map> changeLogInfo;
		try {
			changeLogInfo = changeLogSQL.history();
		}catch(Exception e) {
			changeLogInfo = new HashMap();
			logger.error("change log select error : {} ", e.getMessage());
		}
		
		ChangeLogParser changeLogParser = new ChangeLogParser();
		
		for (int i = 0; i < changeXmls.length; i++) {
			Resource xml = changeXmls[i];
			String[] fileNameArr =xml.getFileName().replaceAll("\\.(xml|json|yaml|sql)$", "").split("_"); 
	    	String type = fileNameArr[0];
	    	int version = Integer.parseInt(fileNameArr[1]);
	    	
	    	String hash = HashUtils.sha256Hash(xml.getFileName());
	    	
	    	if(changeLogInfo.containsKey(hash)) {
	    		continue; 
	    	}
	    		
	    	changeSetsList.add(changeLogParser.getChangeSetInfo(xml, type, version, hash));
		}
		
		
		logger.info("change log : {} ", changeSetsList.size());
		
		if(changeSetsList.size() < 1) {
			return ; 
		}
		
		ChangeSetInfo[] changeSets = changeSetsList.toArray(new ChangeSetInfo[0]);
		
		
		if (changeXmls != null && changeXmls.length > 0) {
			Arrays.sort(changeSets, new Comparator<ChangeSetInfo>() {
			    @Override
			    public int compare(ChangeSetInfo i1, ChangeSetInfo i2) {
			    	
			    	if ((i1.getType()).equals(i2.getType())) {  
	                    return i1.getVersion()-i2.getVersion();
	                }else{
	                    return i1.getType().compareTo(i2.getType());
	                }
			    }
			});
		}
		
		PreparedStatement pstmt = null;
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			boolean stopFlag = executeApplySql(conn, pstmt, changeSets); 
			
			if(stopFlag && this.failRevertFlag) {
				executeRevertSql(conn, pstmt, changeSets); 
			}
			
			for (int i = 0; i < changeSets.length; i++) {
				ChangeSetInfo xml = changeSets[i];
				
				StringBuffer sqlSb = new StringBuffer();
				StringBuffer logSb = new StringBuffer();
				
				for(ChangeSql item : xml.getApplySqls()) {
					sqlSb.append("<!-- start name ").append(item.getDescription()).append(" -->\n");
					sqlSb.append(item.getSql());
					sqlSb.append("\n<!-- // end -->\n");
					
					if(StringUtils.hasLength(item.getLog())) {
						logSb.append(item.getLog()).append("\n;");
					}
				}
				
				String applySql = sqlSb.toString();
				
				sqlSb.setLength(0);
				for(ChangeSql item : xml.getRevertSqls()) {
					
					sqlSb.append("<!-- start name ").append(item.getDescription()).append(" -->\n");
					sqlSb.append(item.getSql());
					sqlSb.append("\n<!-- // end -->");
					
					if(StringUtils.hasLength(item.getLog())) {
						logSb.append(item.getLog()).append("\n;");
					}
				}
				
				String revertSql = sqlSb.toString();
				
				changeLogSQL.addLog(ChangeLogDTO.builder()
					.id(VartechUtils.generateUUID())
					.fileName(xml.getFileName())
					.type(xml.getType())
					.version(xml.getVersion()+"")
					.hash(xml.getHash())
					.description(xml.getDescription())
					.applySql(applySql)
					.revertSql(revertSql)
					.state(stopFlag?"fail":"success")
					.sqlLog(logSb.toString())
					.build());
			}
			
			if(!conn.getAutoCommit()) conn.setAutoCommit(true);
			
			conn.close();
			
			if(stopFlag) {
				throw new VarsqlRuntimeException(VarsqlAppCode.EC_DB_CONNECTION, "Changed information cannot be applied. Please check the log");
			}
		}finally {
			if(conn != null && !conn.isClosed() && !conn.getAutoCommit()) conn.setAutoCommit(true);
			JdbcUtils.close(conn, pstmt, null);
		}
	}
	
	public void executeSQL(String sql, Object... params) throws ClassNotFoundException, SQLException {
		Connection connChk = null;
		PreparedStatement pstmt = null;
		try {
			connChk = getConnection();
			
			pstmt = connChk.prepareStatement(sql);
			
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i+1, params[i]);
			}
			pstmt.execute();
			
			connChk.close();
		}finally {
			JdbcUtils.close(connChk, pstmt, null);
		}
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {

		DBVenderType dbType = DBVenderType.getDBType(connectionInfo.getType());
		Class.forName(dbType.getDriverClass());

		return DriverManager.getConnection(connectionInfo.getUrl(), connectionInfo.getUsername(),
				connectionInfo.getPassword());
	}
	/**
	 * 적용 sql 실행
	 * 
	 * @param conn
	 * @param pstmt
	 * @param changeSets
	 * @return
	 * @throws SQLException
	 */
	private boolean executeApplySql(Connection conn, PreparedStatement pstmt, ChangeSetInfo[] changeSets) throws SQLException {
		boolean stopFlag = false; 
		
		for (int i = 0; i < changeSets.length; i++) {
			ChangeSetInfo xml = changeSets[i];
			
			String changeSetInfo = xml.getFileName()+", type : " +xml.getType() +", version : "+xml.getVersion();
			
			conn.setAutoCommit(false);
			
			for(ChangeSql item : xml.getApplySqls()) {
				String executeSql = item.getSql(); 
				
				logger.info("execute sql info : {} delimeter : {} desc : {}", changeSetInfo, item.getDelimiter() ,item.getDescription());
				logger.info(executeSql);

				String[] sqlArr = null; 
				if(StringUtils.hasLength(item.getDelimiter())) {
					sqlArr = StringUtils.split(executeSql, item.getDelimiter());
				}else {
					sqlArr = new String[]{executeSql};
				}
				
				for (String sql: sqlArr) {
					
					if(!StringUtils.hasLength(sql)) {
						continue; 
					}
					
					try {
						pstmt = conn.prepareStatement(sql);
						pstmt.execute();
						
					}catch(Exception e) {
						item.setLog(e.getMessage());
						logger.error("error info :{} sql : {}", changeSetInfo, sql, e);
						conn.rollback();
						stopFlag = true;
						break;
					}
				}
				
				if(stopFlag) {
					break; 	
				}
			}
			
			if(stopFlag) {
				break; 
			}
			
			conn.setAutoCommit(true);
		}
		
		return stopFlag;
	}
	
	/**
	 * 적용된 sql 되돌리기
	 * 
	 * @param conn
	 * @param pstmt
	 * @param changeSets
	 * @param applyLogs 
	 */
	private void executeRevertSql(Connection conn, PreparedStatement pstmt, ChangeSetInfo[] changeSets) {
		for (int i = 0; i < changeSets.length; i++) {
			ChangeSetInfo xml = changeSets[i];
			
			String changeSetInfo = xml.getFileName()+", type " +xml.getType() +", version "+xml.getVersion();
			
			for(ChangeSql item : xml.getRevertSqls()) {
				String executeSql = item.getSql(); 
				
				String[] sqlArr = null; 
				if(StringUtils.hasLength(item.getDelimiter())) {
					sqlArr = StringUtils.split(executeSql, item.getDelimiter());
				}else {
					sqlArr = new String[]{executeSql};
				}
				
				for (String sql: sqlArr) {
					
					if(!StringUtils.hasLength(sql)) {
						continue; 
					}
					
					try {
						pstmt = conn.prepareStatement(sql);
						pstmt.execute();
						conn.setAutoCommit(true);
					}catch(Exception e) {
						item.setLog(e.getMessage());
						logger.error("error info :{}, sql : {}, error message: {}", changeSetInfo, sql, e.getMessage());
					}
				}
				
			}
		}
	}

	
}
