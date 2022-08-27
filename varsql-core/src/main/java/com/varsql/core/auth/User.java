package com.varsql.core.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.varsql.core.db.valueobject.DatabaseInfo;

/**
 *
 * @FileName  : VarsqlUser.java
 * @프로그램 설명 : login user info
 * @Date      : 2017. 3. 20.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	final public static String ANONYMOUS_USERNAME = "anonymous";
	final public static String ANONYMOUS_ROLE = "ROLE_ANONYMOUS";

	private boolean loginRememberMe;
	private String viewid;
	private String username;
	private String password;
	private String email;
	private Locale userLocale;
	private String fullname;
	private String orgNm;
	private String deptNm;
	private boolean acceptYn;
	private boolean blockYn;

	/* Spring Security fields*/
	private List<Authority> authorities;
	private AuthorityType topAuthority;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	private Map<String, DatabaseInfo> databaseInfo = new HashMap<String, DatabaseInfo>();

	private Map<String, String> vconnidNconuid = new HashMap<String, String>();
	
	public String getViewid() {
		return viewid;
	}

	public void setViewid(String viewid) {
		this.viewid = viewid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}


	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}


	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, DatabaseInfo> getDatabaseInfo() {
		return databaseInfo;
	}

	public void setDatabaseInfo(Map<String,DatabaseInfo> databaseInfo) {
		this.databaseInfo = databaseInfo;
	}

	public AuthorityType getTopAuthority() {
		return topAuthority;
	}

	public void setTopAuthority(AuthorityType topAuthority) {
		this.topAuthority = topAuthority;
	}

	public Locale getUserLocale() {
		return userLocale;
	}

	public void setUserLocale(Locale userLocale) {
		this.userLocale = userLocale;
	}

	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	public String getDeptNm() {
		return deptNm;
	}

	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}

	public boolean isAcceptYn() {
		return acceptYn;
	}

	public void setAcceptYn(boolean acceptYn) {
		this.acceptYn = acceptYn;
	}

	public boolean isBlockYn() {
		return blockYn;
	}

	public void setBlockYn(boolean blockYn) {
		this.blockYn = blockYn;
	}

	public boolean isLoginRememberMe() {
		return loginRememberMe;
	}

	public void setLoginRememberMe(boolean loginRememberMe) {
		this.loginRememberMe = loginRememberMe;
	}

	public Map<String,String> getVconnidNconuid() {
		return vconnidNconuid;
	}

	public void setVconnidNconuid(Map<String,String> vconnidNconuid) {
		this.vconnidNconuid = vconnidNconuid;
	}

	public static class AnonymousUser {
		public AnonymousUser() {
		}
		
		public User build() {
			User user = new User();
			user.setViewid(ANONYMOUS_USERNAME);
			user.setUsername(ANONYMOUS_USERNAME);
			return user;
		}
	}
}
