package com.varsql.web.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.varsql.core.auth.Privilege;

/**
 * -----------------------------------------------------------------------------
* @fileName		: VarsqlAuthority.java
* @desc		: varsql authority
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class Authority implements GrantedAuthority {

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
