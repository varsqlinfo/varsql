package com.varsql.core.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.varsql.core.common.beans.ClientPcInfo;
import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.core.common.util.UUIDUtil;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.beans.DatabaseInfo;
import com.varsql.core.db.encryption.EncryptionFactory;
import com.varsql.core.sql.util.SQLUtil;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.encryption.EncryptDecryptException;
import com.vartech.common.utils.VartechUtils;

/**
 * 로그인 사용자 체크. 
 * @FileName : AuthDAO.java
 * @Author   : ytkim
 * @Program desc : 인증 dao
 * @Hisotry :
 */
public final class AuthDAO {
	final private String role_delim = ";";
	
	private static final Logger logger = LoggerFactory.getLogger(AuthDAO.class);
	
	final static String SELECT_USER_QUERY =  new StringBuilder().append(" select * from VTUSER where UID= ? ").toString();
	
	final static String INSERT_LOG_QUERY =  new StringBuilder()
			.append(" insert into VTUSER_LOG_HIST (VIEWID,HIST_TIME,HIST_TYPE,USR_IP,BROWSER,DEVICE_TYPE,PLATFORM ) ")
			.append(" values( ?, current_timestamp, ?, ?, ?, ?, ? ) ")
			.toString();
	
	protected AuthDAO() {
		
	}
	
	/**
	 * 
	 * @Method Name  : loadUserByUsername
	 * @Method 설명 : 사용자 로그인 정보 추출.
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	public User loadUserByUsername(final String userInfoJson) throws UsernameNotFoundException {
		ParamMap userInfo = VartechUtils.stringToObject(userInfoJson);
		String username = userInfo.getString("username");
		String password = userInfo.getString("password");
		
		return loadUserByUsername(username, password);
	}
	
	public User loadUserByUsername(String username, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			pstmt  = conn.prepareStatement(SELECT_USER_QUERY);
			
			pstmt.setString(1, username);
			
			rs= pstmt.executeQuery();
			
			if(rs==null){
				return null; 
				//throw new UsernameNotFoundException("Wrong username or password :"+ username);
			}
			
			rs.next();
			
			if(!password.equals(EncryptionFactory.getInstance().decrypt(rs.getString("UPW")))){
				return null; 
				//throw new UsernameNotFoundException("Wrong username or password :"+ username);
			}
			
			User user = new User();
			user.setLoginUUID(UUIDUtil.generateUUID());
			user.setUid(rs.getString("VIEWID"));
			user.setUsername(rs.getString("UID"));
			user.setPassword("");
			user.setFullname(rs.getString("UNAME"));
			
			if("Y".equals(rs.getString("BLOCK_YN"))){ //차단된 사용자 체크. 
				user.setBlock_yn("Y");
				return user; 
			}
			
			user.setUserLocale(LocaleConstants.parseLocaleString(rs.getString("lang")));
			user.setOrg_nm(rs.getString("ORG_NM"));
			user.setDept_nm(rs.getString("DEPT_NM"));
			user.setEmail(rs.getString("UEMAIL"));
			user.setAccept_yn(rs.getString("ACCEPT_YN"));
			
			String userRole = rs.getString("USER_ROLE");
			
			String [] roleArr = StringUtil.split(userRole, role_delim);
			
			List<Role> roles = new ArrayList<Role>();
			Role r = new Role();
			
			Authority maxPriority = Authority.GUEST; 
			for (int i = 0; i < roleArr.length; i++) {
				String roleName = roleArr[i]; 
				Authority rolePriority = Authority.valueOf(roleName);
				r = new Role();
				r.setName(roleName);
				r.setPriority(rolePriority.getPriority());
				roles.add(r);
				
				maxPriority = rolePriority.getPriority() > maxPriority.getPriority() ? rolePriority : maxPriority;
			}
			
			user.setTopAuthority(maxPriority);
			user.setAuthorities(roles);
			
			return user;
		}catch (SQLException e) {
			logger.error(this.getClass().getName() , e);
			throw new UsernameNotFoundException(new StringBuilder().append("Wrong username or password :").append(username).append(" ").append(e.getMessage()).toString());
		} catch (EncryptDecryptException e) {
			logger.error(this.getClass().getName() , e);
			throw new UsernameNotFoundException(new StringBuilder().append("Wrong username or password :").append(username).append(" ").append(e.getMessage()).toString());
		}catch(Exception e){
			logger.error(this.getClass().getName() , e);
			throw new UsernameNotFoundException(new StringBuilder().append("Wrong username or password :").append(username).append(" ").append(e.getMessage()).toString());
		}finally{
			SQLUtil.close(conn , pstmt, rs);
		}
	}
	
	public void getUserDataBaseInfo() throws Exception {
		Connection conn =ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstmt= null; 
		ResultSet rs= null;
		try {
			getUserDataBaseInfo(conn,pstmt, rs, SecurityUtil.loginInfo());
		}finally{
			SQLUtil.close(conn , pstmt, rs);
		}
		
	}
	
	/**
	 * 
	 * @Method Name  : addLog
	 * @Method 설명 : add 로그인 & 로그아웃 로그. 
	 * @작성일   : 2019. 9. 20. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @throws Exception
	 */
	public void addLog(User user, String type, ClientPcInfo cpi) {
		Connection conn =ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstmt= null; 
		try {
			pstmt  = conn.prepareStatement(INSERT_LOG_QUERY);
			
			pstmt.setString(1, user.getUid());
			pstmt.setString(2, type);
			pstmt.setString(3, cpi.getIp());
			pstmt.setString(4, cpi.getBrowser());
			
			pstmt.setString(5, cpi.getDeviceType());
			pstmt.setString(6, cpi.getOsType());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(this.getClass().getName() +" addLog ", e.getMessage() ,e);
		}finally{
			SQLUtil.close(conn , pstmt, null);
		}
		
	}
	
	/**
	 * 
	 * @Method Name  : getUserDataBaseInfo
	 * @Method 설명 : 사용자 권한 맵핑된 데이타 베이스 정보 추출.
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param conn
	 * @param pstmt
	 * @param rs
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	private Map<String, DatabaseInfo> getUserDataBaseInfo(Connection conn, PreparedStatement pstmt,ResultSet rs, User user) throws SQLException {
		
	  	StringBuffer query = new StringBuffer();
	  	
	  	query.append(" select VCONNID, VTYPE, VNAME, VDBSCHEMA, VDBVERSION, BASETABLE_YN, LAZYLOAD_YN,SCHEMA_VIEW_YN, MAX_SELECT_COUNT from VTCONNECTION a where USE_YN ='Y' and DEL_YN = 'N' AND ");
	  	
		Authority tmpAuthority = user.getTopAuthority();
		
		if (tmpAuthority.equals(Authority.ADMIN)) {
			query.append(" 1 = 1 ");
		}else if(tmpAuthority.equals(Authority.GUEST)){
			query.append(" 1 != 1 ");
		}else {
			query.append(" A.VCONNID IN ( ");					
			query.append(" 	select distinct VCONNID ");
			query.append(" 	from VTDATABASE_GROUP a ,VTDATABASE_GROUP_DB b , VTDATABASE_GROUP_USER c ");
			query.append(" 	where a.GROUP_ID = b.GROUP_ID ");
			query.append(" 	and b.GROUP_ID = c.GROUP_ID ");
			query.append(" 	and c.VIEWID = '"+user.getUid()+"' ");
			query.append(" 	and b.VCONNID not in ( select VCONNID from VTDATABASE_BLOCK_USER where VIEWID ='"+user.getUid()+"' )  ");
			query.append(" ) ");
			
			if(tmpAuthority.equals(Authority.MANAGER)){
				query.append("  or a.VCONNID in ( select VCONNID from VTDATABASE_MANAGER where VIEWID = '"+user.getUid()+"' ) ");
			}
		}
		
		Map<String, DatabaseInfo> userDatabaseInfo = new LinkedHashMap<String, DatabaseInfo>();
		
		if(!tmpAuthority.equals(Authority.GUEST)){
			pstmt  = conn.prepareStatement(query.toString());
			rs= pstmt.executeQuery();
			
			String vconnid;
			String uuid = "";
			
			String loginUUID = user.getLoginUUID();
			Map<String, DatabaseInfo> beforeDatabaseInfo = user.getDatabaseInfo();
			
			List<String> newVconnidList = new ArrayList<String>();
			boolean flag = Configuration.getInstance().useConnUID();
			while(rs.next()){
				
				vconnid = rs.getString(VarsqlConstants.CONN_ID);
				newVconnidList.add(vconnid);
				
				if(flag){
					uuid = UUIDUtil.nameUUIDFromBytes(loginUUID+vconnid);
				}else{
					uuid = vconnid;
				}
				
				userDatabaseInfo.put(uuid, new DatabaseInfo(vconnid
						, uuid
						, rs.getString(VarsqlConstants.CONN_TYPE)
						, rs.getString(VarsqlConstants.CONN_NAME)
						, rs.getString(VarsqlConstants.CONN_DBSCHEMA)
						, rs.getString(VarsqlConstants.CONN_BASETABLE_YN)
						, rs.getString(VarsqlConstants.CONN_LAZYLOAD_YN)
						, rs.getString(VarsqlConstants.CONN_VDBVERSION)
						, rs.getString(VarsqlConstants.CONN_SCHEMA_VIEW_YN)
						, rs.getInt(VarsqlConstants.CONN_MAX_SELECT_COUNT)
						)
				);
			}
			
			List<String> beforeVconnidList = new ArrayList<String>();
			
			if(beforeDatabaseInfo != null) {
				for(Map.Entry<String, DatabaseInfo> databaseInfo: beforeDatabaseInfo.entrySet()) {
					beforeVconnidList.add(databaseInfo.getValue().getVconnid());
				}
			}
		    
			/*
	        Collections.sort(beforeVconnidList);
	        Collections.sort(newVconnidList);
		         
	        boolean isEqual = newVconnidList.equals(beforeVconnidList);      //false
	        if(!isEqual) {
	        	user.setDatabaseInfo(userDatabaseInfo);
	        };
	        */
			user.setDatabaseInfo(userDatabaseInfo);
		}
		return userDatabaseInfo;
	}
}
