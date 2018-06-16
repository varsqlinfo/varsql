package com.varsql.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.varsql.core.auth.UserService;
import com.varsql.core.auth.VarsqlAccessDeniedHandler;
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
			.ignoringAntMatchers("/login/**","/logout")
			.requireCsrfProtectionMatcher(new CsrfRequestMatcher())
		.and()
			//.addFilterBefore(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
			.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
		.and() //session
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
		.and() // login
			.formLogin()
	        .loginPage("/login")
	        .loginProcessingUrl("/login_check")
	        .usernameParameter("id")
	        .passwordParameter("password")
	        .failureUrl("/login?mode=fail")
	        .successHandler(new VarsqlAuthenticationSuccessHandler())
	        .permitAll()
	    .and() // auth
		    .authorizeRequests()
     		.antMatchers("/admin/**").hasAuthority("ADMIN")
     		.antMatchers("/manage/**").hasAnyAuthority("ADMIN","MANAGER")
     		.antMatchers("/user/**","/database/**").hasAnyAuthority("ADMIN","MANAGER","USER")
     		.antMatchers("/guest/**").hasAuthority("GUEST")
     		.antMatchers("/login","/join/**").anonymous()
     		.antMatchers("/login_check","/api/**","/error/**", "/favicon.ico","/webstatic/**","/index.jsp").permitAll()
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
