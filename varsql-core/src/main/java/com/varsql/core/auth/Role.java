package com.varsql.core.auth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * User's Roles will be supplied from this class.
 * Role class implements Spring-Security GrantedAuthority interface for custom authentication feature
 * 
 * @author aakin
 * 
 */
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	private String name;
	private int priority;

	private List<Privilege> privileges;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthority() {
		return this.name;
	}

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Role [name=");
		builder.append(name);
		builder.append(", privileges=");
		builder.append(privileges);
		builder.append(", priority=");
		//builder.append(priority);
		builder.append("]");
		return builder.toString();
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
