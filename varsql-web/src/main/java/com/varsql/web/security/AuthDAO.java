package com.varsql.web.security;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.varsql.core.auth.Authority;
import com.varsql.core.auth.AuthorityType;
import com.varsql.core.auth.User;
import com.varsql.core.common.beans.ClientPcInfo;
import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.constants.VarsqlKeyConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.core.common.util.UUIDUtil;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.sql.util.SqlUtils;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.model.entity.user.UserLogHistEntity;
import com.varsql.web.security.repository.UserLogHistRepository;
import com.varsql.web.security.repository.UserRepository;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.utils.VartechUtils;

/**
 * 로그인 사용자 체크.
 * @FileName : AuthDAO.java
 * @Author   : ytkim
 * @Program desc : 인증 dao
 * @Hisotry :
 */
@Service(value = "authDao")
public final class AuthDAO {
	final private String role_delim = ";";

	private static final Logger logger = LoggerFactory.getLogger(AuthDAO.class);

	@Autowired
	private UserRepository userRepository;// = (UserRepository)BeanUtils.getBean("userRepository");

	@Autowired
	private UserLogHistRepository userLogHistRepository;// = (UserLogHistRepository)BeanUtils.getBean("userLogHistRepository");
	
	@Autowired
	@Qualifier("varsqlPasswordEncoder")
	private PasswordEncoder passwordEncoder;

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
		try {
			UserEntity userModel= userRepository.findByUid(username);

			if(userModel==null){
				return null;
				//throw new UsernameNotFoundException("Wrong username or password ");
			}
			
			if(!passwordEncoder.matches(password, userModel.getUpw())){
				return null;
				//throw new UsernameNotFoundException("Wrong username or password ");
			}

			User user = new User();
			user.setLoginUUID(UUIDUtil.generateUUID());
			user.setViewid(userModel.getViewid());
			user.setUsername(userModel.getUid());
			user.setPassword("");
			user.setFullname(userModel.getUname());

			if(userModel.isBlockYn()){ //차단된 사용자 체크.
				user.setBlockYn(true);
				return user;
			}

			user.setUserLocale(LocaleConstants.parseLocaleString(userModel.getLang()));
			user.setOrgNm(userModel.getOrgNm());
			user.setDeptNm(userModel.getDeptNm());
			user.setEmail(userModel.getUemail());
			user.setAcceptYn(userModel.isAcceptYn());

			String userRole = userModel.getUserRole();

			String [] roleArr = StringUtil.split(userRole, role_delim);

			List<Authority> roles = new ArrayList<Authority>();
			Authority r = new Authority();

			AuthorityType maxPriority = AuthorityType.GUEST;
			for (int i = 0; i < roleArr.length; i++) {
				String roleName = roleArr[i];
				AuthorityType rolePriority = AuthorityType.valueOf(roleName);
				r = new Authority();
				r.setName(roleName);
				r.setPriority(rolePriority.getPriority());
				roles.add(r);

				maxPriority = rolePriority.getPriority() > maxPriority.getPriority() ? rolePriority : maxPriority;
			}

			user.setTopAuthority(maxPriority);
			user.setAuthorities(roles);

			return user;
		}catch(Exception e){
			logger.error(this.getClass().getName() , e);
			throw new UsernameNotFoundException(new StringBuilder().append("Wrong username or password :").append(username).append(" ").append(e.getMessage()).toString());
		}
	}

	public void getUserDataBaseInfo() throws Exception {
		Connection conn =ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstmt= null;
		ResultSet rs= null;
		try {
			getUserDataBaseInfo(conn,pstmt, rs, SecurityUtil.loginInfo());
		}finally{
			SqlUtils.close(conn , pstmt, rs);
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
		try {
			userLogHistRepository.save(UserLogHistEntity.builder()
					.viewid(user.getViewid())
					.histType(type)
					.usrIp(cpi.getIp())
					.browser(cpi.getBrowser())
					.deviceType(cpi.getDeviceType())
					.platform(cpi.getOsType()).build());
		} catch (Exception e) {
			logger.error(this.getClass().getName() +" addLog ", e);
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

		AuthorityType tmpAuthority = user.getTopAuthority();

		if (tmpAuthority.equals(AuthorityType.ADMIN)) {
			query.append(" 1 = 1 ");
		}else if(tmpAuthority.equals(AuthorityType.GUEST)){
			query.append(" 1 != 1 ");
		}else {
			query.append(" A.VCONNID IN ( ");
			query.append(" 	select distinct VCONNID ");
			query.append(" 	from VTDATABASE_GROUP a ,VTDATABASE_GROUP_DB b , VTDATABASE_GROUP_USER c ");
			query.append(" 	where a.GROUP_ID = b.GROUP_ID ");
			query.append(" 	and b.GROUP_ID = c.GROUP_ID ");
			query.append(" 	and c.VIEWID = '"+user.getViewid()+"' ");
			query.append(" 	and b.VCONNID not in ( select VCONNID from VTDATABASE_BLOCK_USER where VIEWID ='"+user.getViewid()+"' )  ");
			query.append(" ) ");

			if(tmpAuthority.equals(AuthorityType.MANAGER)){
				query.append("  or a.VCONNID in ( select VCONNID from VTDATABASE_MANAGER where VIEWID = '"+user.getViewid()+"' ) ");
			}
		}

		Map<String, DatabaseInfo> userDatabaseInfo = new LinkedHashMap<String, DatabaseInfo>();

		if(!tmpAuthority.equals(AuthorityType.GUEST)){
			pstmt  = conn.prepareStatement(query.toString());
			rs= pstmt.executeQuery();

			String vconnid;
			String uuid = "";

			String loginUUID = user.getLoginUUID();
			Map<String, DatabaseInfo> beforeDatabaseInfo = user.getDatabaseInfo();

			List<String> newVconnidList = new ArrayList<String>();
			boolean flag = Configuration.getInstance().useConnUID();
			while(rs.next()){

				vconnid = rs.getString(VarsqlKeyConstants.CONN_ID);
				newVconnidList.add(vconnid);

				if(flag){
					uuid = UUIDUtil.nameUUIDFromBytes(loginUUID+vconnid);
				}else{
					uuid = vconnid;
				}

				userDatabaseInfo.put(uuid, new DatabaseInfo(vconnid
						, uuid
						, rs.getString(VarsqlKeyConstants.CONN_TYPE)
						, rs.getString(VarsqlKeyConstants.CONN_NAME)
						, rs.getString(VarsqlKeyConstants.CONN_DBSCHEMA)
						, rs.getString(VarsqlKeyConstants.CONN_BASETABLE_YN)
						, rs.getString(VarsqlKeyConstants.CONN_LAZYLOAD_YN)
						, rs.getLong(VarsqlKeyConstants.CONN_VDBVERSION)
						, rs.getString(VarsqlKeyConstants.CONN_SCHEMA_VIEW_YN)
						, rs.getInt(VarsqlKeyConstants.CONN_MAX_SELECT_COUNT)
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
