package com.varsql.web.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SsoAuthToken extends AbstractAuthenticationToken{
	private static final long serialVersionUID = 1L;
	
	private final String principal;
    private Object credentials;

    public SsoAuthToken(String principal){
        super(null);
        this.principal = principal;
        this.credentials = null;
    }


    @Override
    public Object getCredentials() {
        return credentials;
    }


    @Override
    public Object getPrincipal() {
        return principal;
    }
}