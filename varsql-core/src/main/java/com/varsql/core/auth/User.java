package com.varsql.core.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.varsql.core.db.beans.DatabaseInfo;

/**
 * 
 * @FileName  : User.java
 * @프로그램 설명 : login user info
 * @Date      : 2017. 3. 20. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String loginUUID;
	private String uid;
	private String username;
	private String password;
	private String email;
	private Locale userLocale;
	private String fullname;
	private String org_nm;
	private String dept_nm;
	private String accept_yn;
	private String block_yn;

	/* Spring Security fields*/
	private List<Role> authorities;
	private Authority topAuthority;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	private Map<String,DatabaseInfo> databaseInfo = new HashMap<String,DatabaseInfo>();
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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
	
	public String getOrg_nm() {
		return org_nm;
	}

	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}

	public String getDept_nm() {
		return dept_nm;
	}

	public void setDept_nm(String dept_nm) {
		this.dept_nm = dept_nm;
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
	
	public void setAuthorities(List<Role> authorities) {
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
	
	public String getAccept_yn() {
		return accept_yn;
	}

	public void setAccept_yn(String accept_yn) {
		this.accept_yn = accept_yn;
	}
	
	public Map<String,DatabaseInfo> getDatabaseInfo() {
		return databaseInfo;
	}

	public void setDatabaseInfo(Map<String,DatabaseInfo> databaseInfo) {
		this.databaseInfo = databaseInfo;
	}
	
	public Authority getTopAuthority() {
		return topAuthority;
	}

	public void setTopAuthority(Authority topAuthority) {
		this.topAuthority = topAuthority;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=");
		builder.append(username);
		builder.append(", email=");
		builder.append(email);
		builder.append(", password=");
		builder.append(password);
		builder.append(", fullname=");
		builder.append(fullname);
		builder.append(", topAuthority=");
		builder.append(topAuthority);
		builder.append(", authorities=");
		builder.append(authorities);
		builder.append(", accountNonExpired=");
		builder.append(accountNonExpired);
		builder.append(", accountNonLocked=");
		builder.append(accountNonLocked);
		builder.append(", credentialsNonExpired=");
		builder.append(credentialsNonExpired);
		builder.append(", enabled=");
		builder.append(enabled);
		builder.append(", accept_yn=");
		builder.append(accept_yn);
		builder.append(", userLocale=");
		builder.append(userLocale);
		builder.append("]");
		return builder.toString();
	}

	public String getBlock_yn() {
		return block_yn;
	}

	public void setBlock_yn(String block_yn) {
		this.block_yn = block_yn;
	}

	public String getLoginUUID() {
		return loginUUID;
	}

	public void setLoginUUID(String loginUUID) {
		this.loginUUID = loginUUID;
	}

	public Locale getUserLocale() {
		return userLocale;
	}

	public void setUserLocale(Locale userLocale) {
		this.userLocale = userLocale;
	}
}
