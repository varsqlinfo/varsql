package com.varsql.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.varsql.core.auth.Authority;
import com.varsql.core.auth.UserService;
import com.varsql.core.auth.VarsqlAccessDeniedHandler;
import com.varsql.core.auth.VarsqlAuthenticationFailHandler;
import com.varsql.core.auth.VarsqlAuthenticationProvider;
import com.varsql.core.auth.VarsqlAuthenticationSuccessHandler;
import com.varsql.core.configuration.VarsqlWebConfig;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/webstatic/**","/error/**","/favicon.ico");
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		configureHttpSecurity(http);
    }
	
	private void configureHttpSecurity(HttpSecurity http) throws Exception {
		
		http.headers()
			.frameOptions().sameOrigin().httpStrictTransportSecurity()
			.disable()
		.and()
			.csrf()
			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.ignoringAntMatchers("/login/**","/logout","/webstatic/**","/error/**","/favicon.ico")
			.requireCsrfProtectionMatcher(new CsrfRequestMatcher())
		.and()
			//.addFilterBefore(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
			.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
		.and() //session
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
			//.maximumSessions(1)	// 중복 로그인 카운트
			.sessionFixation().changeSessionId()	// session 공격시 session id 변경.
		.and() // login
			.formLogin()
	        .loginPage("/login")
	        .loginProcessingUrl("/login_check")
	        .usernameParameter("vsql_login_id")
	        .passwordParameter("vsql_login_password")
	        //.failureUrl("/login?mode=fail")
	        .successHandler(new VarsqlAuthenticationSuccessHandler())
	        .failureHandler(new VarsqlAuthenticationFailHandler())
	        .permitAll()
	    .and() // auth
		    .authorizeRequests()
     		.antMatchers("/admin/**").hasAuthority(Authority.ADMIN.name())
     		.antMatchers("/manage/**").hasAnyAuthority(Authority.ADMIN.name(),Authority.MANAGER.name())
     		.antMatchers("/user/**","/database/**").hasAnyAuthority(Authority.ADMIN.name(),Authority.MANAGER.name(),Authority.USER.name())
     		.antMatchers("/guest/**").hasAuthority(Authority.GUEST.name())
     		.antMatchers("/login","/join/**").anonymous()
     		.antMatchers("/login_check","/index.jsp").permitAll()
     		.antMatchers("/**").authenticated()
     		.anyRequest().authenticated().and()
     		.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
     	.and() //log out
	     	.logout()
	        .logoutUrl("/logout")
	        .logoutSuccessUrl("/login")
	        .invalidateHttpSession(true)
	        .deleteCookies("JSESSIONID").permitAll()
	        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.and()
			.httpBasic();				
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
    public VarsqlAccessDeniedHandler accessDeniedHandler() {
    	return new VarsqlAccessDeniedHandler(VarsqlWebConfig.newIntance().getPage403());
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	 auth.authenticationProvider(varsqlAuthenticationProvider());
    }
}
