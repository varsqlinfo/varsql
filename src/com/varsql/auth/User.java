package com.varsql.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 
 * User will be supplied from this class.
 * User class implements Spring-Security UserDetails interface for custom authentication feature
 * 
 * @author aakin
 * 
 */
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String uid;
	private String username;
	private String password;
	private String email;
	private String fullname;
	private String org_nm;
	private String dept_nm;
	private String accept_yn;

	/* Spring Security fields*/
	private List<Role> authorities;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	private Map<String,HashMap> databaseInfo = new HashMap<String,HashMap>();
	
	
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
	
	public Map<String,HashMap> getDatabaseInfo() {
		return databaseInfo;
	}

	public void setDatabaseInfo(Map<String,HashMap> databaseInfo) {
		this.databaseInfo = databaseInfo;
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
		builder.append("]");
		return builder.toString();
	}
}
