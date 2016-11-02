package com.varsql.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.varsql.db.ConnectionFactory;
import com.varsql.common.util.StringUtil;
import com.varsql.constants.VarsqlConstants;
import com.varsql.sql.SQLUtil;
import com.varsql.web.app.admin.AdminController;

/**
 * 로그인 사용자 체크. 
 * @FileName : AuthDAO.java
 * @Author   : ytkim
 * @Program desc :
 * @Hisotry :
 */
public class AuthDAO {
	final private String role_delim = ";";
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
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
	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			pstmt  = conn.prepareStatement("select * from vtuser where uid= ?");
			
			pstmt.setString(1, username);
			
			rs= pstmt.executeQuery();
			
			if(rs==null){
				throw new UsernameNotFoundException("Wrong username or password :"+ username);
			}
			
			rs.next();
			
			User user = new User();
			user.setUid(rs.getString("viewid"));
			user.setUsername(rs.getString("uid"));
			user.setPassword(rs.getString("upw"));
			user.setFullname(rs.getString("uname"));
			user.setOrg_nm(rs.getString("org_nm"));
			user.setDept_nm(rs.getString("dept_nm"));
			user.setEmail(rs.getString("uemail"));
			user.setAccept_yn(rs.getString("accept_yn"));
			
			String userRole = rs.getString("role");
			
			String [] roleArr = StringUtil.split(userRole, role_delim);
			
			List<Role> roles = new ArrayList<Role>();
			Role r = new Role();
			for (int i = 0; i < roleArr.length; i++) {
				 r = new Role();
				 r.setName(roleArr[i]);
				 roles.add(r);
			}
			user.setAuthorities(roles);
			user.setDatabaseInfo(getUserDataBaseInfo(conn,pstmt, rs , user));
			
			return user;
		}catch (SQLException e) {
			logger.error(this.getClass().getName() , e);
			throw new UsernameNotFoundException(new StringBuilder().append("Wrong username or password :").append(username).append(" ").append(e.getMessage()).toString());
		}catch(Exception e){
			logger.error(this.getClass().getName() , e);
			throw new UsernameNotFoundException(new StringBuilder().append("Wrong username or password :").append(username).append(" ").append(e.getMessage()).toString());
		}finally{
			SQLUtil.close(conn , pstmt, rs);
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
	private Map<String, HashMap> getUserDataBaseInfo(Connection conn, PreparedStatement pstmt,ResultSet rs, User user) throws SQLException {
		
	  	StringBuffer query = new StringBuffer();
	  	
	  	query.append("select  VCONNID, VTYPE, VNAME from VTCONNECTION a where");
	  	
	  	Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		
		String tmpAuthority = "";
		for (GrantedAuthority grantedAuthority : authorities) {
			tmpAuthority = grantedAuthority.getAuthority();
			if (tmpAuthority.equals(Authority.USER.name())) {
				query.append(" A.VCONNID IN (SELECT VCONNID from VTDATABASE_USER_ROLE WHERE VIEWID = '"+user.getUid()+"' ) ");
			}else if (tmpAuthority.equals(Authority.MANAGER.name())) {
				query.append(" A.VCONNID IN (SELECT VCONNID from VTDATABASE_MANAGER_ROLE WHERE VIEWID ='"+user.getUid()+"' ) ");
			}else if (tmpAuthority.equals(Authority.ADMIN.name())) {
				query.append(" 1 = 1 ");
			}else{
				query.append(" 1 != 1 ");
			}
			break; 
		}
		
		Map<String, HashMap> userDatabaseInfo = new HashMap<String, HashMap>(); 
		if(!tmpAuthority.equals(Authority.GUEST.name())){
			pstmt  = conn.prepareStatement(query.toString());
			rs= pstmt.executeQuery();
			String vconnid, vtype, vname;
			
			HashMap databaseInfo = null; 
			while(rs.next()){
				databaseInfo = new HashMap();
				vconnid = rs.getString(VarsqlConstants.CONN_ID);
				
				databaseInfo.put(VarsqlConstants.CONN_ID, vconnid);
				databaseInfo.put(VarsqlConstants.CONN_TYPE, rs.getString(VarsqlConstants.CONN_TYPE));
				databaseInfo.put(VarsqlConstants.CONN_NAME, rs.getString(VarsqlConstants.CONN_NAME));
				
				userDatabaseInfo.put(vconnid, databaseInfo);
			}
		}
		
		return userDatabaseInfo;
		
	}
}
