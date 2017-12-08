package com.varsql.web.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.varsql.auth.UserService;
import com.varsql.auth.VarsqlAccessDeniedHandler;
import com.varsql.auth.VarsqlAuthenticationProvider;
import com.varsql.auth.VarsqlAuthenticationSuccessHandler;
import com.varsql.configuration.VarsqlWebConfig;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: gain
* @NAME		: SecurityConfig.java
* @DESC		: 스프링 시큐리티 처리.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 3. 16.			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		configureLogin(http);
		configureHttpSecurity(http);
	    configureLogout(http);
	    configureAuth(http);
	    configureSession(http);
	    configureSSOFilter(http);
	    
        
    }
	private void configureHttpSecurity(HttpSecurity http) throws Exception {
		http.headers()
			.frameOptions().sameOrigin().httpStrictTransportSecurity()
			.disable()
			.and()
			.csrf().disable()
			.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
			.and().httpBasic();
	}
	private void configureSession(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
		/*
		.invalidSessionUrl("/gainCommon/sessioninvalidation?error_code=1")
		.sessionAuthenticationErrorUrl("/gainCommon/sessionin validation?error_code=2")
		.maximumSessions(1)
		.expiredUrl("/gainCommon/sessioninvalidation?error_code=3")
		.maxSessionsPreventsLogin(true);
		*/
		
	}
	/**
	 * 
	* @Method	: configureLogin
	* @Method설명	: 로그인 관련 처리.
	* @작성일		: 2017. 3. 16.
	* @AUTHOR	: ytkim
	* @변경이력	: 
	* @param http
	* @throws Exception
	 */
	private void configureLogin(HttpSecurity http) throws Exception {
		http.formLogin()
	        .loginPage("/login")
	        .loginProcessingUrl("/login_check")
	        .usernameParameter("id")
	        .passwordParameter("password")
	        .failureUrl("/login?mode=fail")
	        .successHandler(new VarsqlAuthenticationSuccessHandler())
	        .permitAll().and();
		
	}
	
	/**
	 * 
	* @Method	: configureAuth
	* @Method설명	: url 관련 권한 처리.
	* @작성일		: 2017. 3. 16.
	* @AUTHOR	: ytkim
	* @변경이력	: 
	* @param http
	* @throws Exception
	* 
	* 
	* <security:intercept-url pattern="/error/**" access="permitAll" />
	            <security:intercept-url pattern="/admin/**" access="hasRole('ADMIN')" />
	            <security:intercept-url pattern="/manage/**" access="hasAnyRole('MANAGER','ADMIN')"/>
	            <security:intercept-url pattern="/user/**" access="hasAnyRole('USER','MANAGER','ADMIN')"/>
	            <security:intercept-url pattern="/database/**" access="hasAnyRole('USER','MANAGER','ADMIN')"/>
	            <security:intercept-url pattern="/guest/**" access="!hasAnyRole('USER','MANAGER','ADMIN')"/>
	            <security:intercept-url pattern="/**" access="isAuthenticated()"/>
	 */
	private void configureAuth(HttpSecurity http) throws Exception {
		http.authorizeRequests()
     		.antMatchers("/admin/**").hasAuthority("ADMIN")
     		.antMatchers("/manage/**").hasAnyAuthority("ADMIN","MANAGER")
     		.antMatchers("/user/**","/database/**").hasAnyAuthority("ADMIN","MANAGER","USER")
     		.antMatchers("/guest/**").hasAuthority("GUEST")
     		.antMatchers("/login","/join/**").anonymous()
     		.antMatchers("/login_check","/api/**/error/**", "/favicon.ico","/webstatic/**","/index.jsp").permitAll()
     		.antMatchers("/**").authenticated()
     		.anyRequest().authenticated().and()
     		.exceptionHandling().accessDeniedPage(VarsqlWebConfig.newIntance().getPage403());
	}
	
	/**
	 * 
	* @Method	: configureLogout
	* @Method설명	: 로그아웃 처리.
	* @작성일		: 2017. 3. 16.
	* @AUTHOR	: ytkim
	* @변경이력	: 
	* @param http
	* @throws Exception
	 */
	private void configureLogout(HttpSecurity http) throws Exception {
		http.logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID").permitAll().and();
		
	}
	
	@Bean
	public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}
	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}

    @Override
    protected UserDetailsService userDetailsService() {
    	return new UserService();
    }
    
    @Bean
	public UserService userService() {
		return new UserService();
	}
    
    @Bean
    public VarsqlAuthenticationProvider varsqlAuthenticationProvider() {
    	return new VarsqlAuthenticationProvider();
    }
    
    @Bean
    public VarsqlAccessDeniedHandler gainAccessDeniedHandler() {
    	return new VarsqlAccessDeniedHandler();
    }
    
    private void configureSSOFilter(HttpSecurity http) {
    	
	}
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	 auth.authenticationProvider(varsqlAuthenticationProvider());
    }
}
