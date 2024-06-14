package com.varsql.core.changeset.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.varsql.core.changeset.beans.ChangeLogDTO;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.sql.util.SQLResultSetUtils;

public class ChangeSetDataDB implements ChangeSetData{
	private final String selectQuery = "select ID, TYPE, VERSION, HASH, STATE from VTDATABASE_CHANGELOG order by APPLIED_DT desc ";
	
	private final String insertQuery = "insert into VTDATABASE_CHANGELOG ( "
			+ "    ID, "
			+ "    TYPE, "
			+ "    VERSION, "
			+ "    HASH, "
			+ "    APPLIED_DT, "
			+ "    DESCRIPTION, "
			+ "    APPLY_SQL, "
			+ "    REVERT_SQL, "
			+ "    STATE, "
			+ "    SQL_LOG "
			+ " ) values ( "
			+ "    ?, "
			+ "    ?, "
			+ "    ?, "
			+ "    ?, "
			+ "    CURRENT_TIMESTAMP, "
			+ "    ?, "
			+ "    ?, "
			+ "    ?, "
			+ "    ?, "
			+ "    ? "
			+ ")";
	
	private ConnectionInfo connectionInfo;
	public ChangeSetDataDB(ConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}
	
	private Connection getConnection() throws ClassNotFoundException, SQLException {

		DBVenderType dbType = DBVenderType.getDBType(connectionInfo.getType());
		Class.forName(dbType.getDriverClass());

		return DriverManager.getConnection(connectionInfo.getUrl(), connectionInfo.getUsername(),
				connectionInfo.getPassword());
	}


	@Override
	public Map history() throws Exception {
		Map changeLogInfo = new LinkedHashMap<>();
		try(Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(selectQuery);
			ResultSet rs = pstmt.executeQuery()){
			
			List<Map> result = SQLResultSetUtils.resultSetHandler(rs, connectionInfo.getType());
			
			result.forEach(item->{
				changeLogInfo.put(item.get("HASH"), item);
			});
		}
		
		return changeLogInfo; 
	}


	@Override
	public boolean addLog(ChangeLogDTO dto) throws Exception {
		try(Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(insertQuery)){
			pstmt.setObject(1, dto.getId());
			pstmt.setObject(2, dto.getType());
			pstmt.setObject(3, dto.getVersion());
			pstmt.setObject(4, dto.getHash());
			pstmt.setObject(5, dto.getDescription());
			pstmt.setObject(6, dto.getApplySql());
			pstmt.setObject(7, dto.getRevertSql());
			pstmt.setObject(8, dto.getState());
			pstmt.setObject(9, dto.getSqlLog());
			
			return pstmt.execute();
		}
	}
	
	
}
