package com.varsql.web.configuration;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.common.code.VarsqlSsoType;
import com.varsql.core.configuration.VarsqlWebConfig;
import com.varsql.core.sso.SimpleSsoHandler;
import com.varsql.web.common.filter.VarsqlSsoFilter;
import com.varsql.web.common.sso.SsoBeanFactory;
import com.varsql.web.common.sso.SsoComponent;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.SecurityConstants;
import com.varsql.web.security.RestAuthenticationEntryPoint;
import com.varsql.web.security.UserService;
import com.varsql.web.security.VarsqlAccessDeniedHandler;
import com.varsql.web.security.VarsqlAuthenticationFailHandler;
import com.varsql.web.security.VarsqlAuthenticationLogoutHandler;
import com.varsql.web.security.VarsqlAuthenticationLogoutSuccessHandler;
import com.varsql.web.security.VarsqlAuthenticationProvider;
import com.varsql.web.security.VarsqlAuthenticationSuccessHandler;
import com.varsql.web.security.VarsqlBasicAuthenticationEntryPoint;
import com.varsql.web.security.rememberme.RememberMeTokenRepository;
import com.varsql.web.security.rememberme.RememberMeUserService;

/**
 * -----------------------------------------------------------------------------
* @fileName		: SecurityConfig.java
* @desc		: security configuration
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	final private String CSRF_TOKEN_NAME = "varsql_ct";

	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	private VarsqlBasicAuthenticationEntryPoint varsqlBasicAuthenticationEntryPoint;

	private VarsqlAuthenticationFailHandler varsqlAuthenticationFailHandler;
	
	private VarsqlAuthenticationSuccessHandler varsqlAuthenticationSuccessHandler;

	private VarsqlAuthenticationLogoutHandler varsqlAuthenticationLogoutHandler;

	private VarsqlAuthenticationLogoutSuccessHandler varsqlAuthenticationLogoutSuccessHandler;
	
	private BeanFactory beanFactory;
	
	public SecurityConfigurer(RestAuthenticationEntryPoint restAuthenticationEntryPoint
			,VarsqlBasicAuthenticationEntryPoint varsqlBasicAuthenticationEntryPoint
			,VarsqlAuthenticationFailHandler varsqlAuthenticationFailHandler
			,VarsqlAuthenticationLogoutHandler varsqlAuthenticationLogoutHandler
			,VarsqlAuthenticationSuccessHandler varsqlAuthenticationSuccessHandler
			,VarsqlAuthenticationLogoutSuccessHandler varsqlAuthenticationLogoutSuccessHandler
			,BeanFactory beanFactory) {
		
		this.restAuthenticationEntryPoint = restAuthenticationEntryPoint; 
		this.varsqlBasicAuthenticationEntryPoint = varsqlBasicAuthenticationEntryPoint; 
		this.varsqlAuthenticationFailHandler = varsqlAuthenticationFailHandler; 
		this.varsqlAuthenticationLogoutHandler = varsqlAuthenticationLogoutHandler; 
		this.varsqlAuthenticationSuccessHandler = varsqlAuthenticationSuccessHandler; 
		this.varsqlAuthenticationLogoutSuccessHandler = varsqlAuthenticationLogoutSuccessHandler; 
		this.beanFactory = beanFactory; 
	}

	@Override
    public void configure(WebSecurity web) throws Exception {

		// 404 error 처리 하기위해서 추가.
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedDoubleSlash(true);

        web.ignoring()
            .antMatchers("/webstatic/**","/error/**","/favicon.ico")
         .and().httpFirewall(firewall);

    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		configureRememberMe(http);
		configureHttpSecurity(http);
    }

	private void configureHttpSecurity(HttpSecurity http) throws Exception {

		// sso 처리.
		if(VarsqlSsoType.ALWAYS.equals(VarsqlWebConfig.getInstance().getSsoConfig().getMode())) { // sso interceptor 등록.
			http.addFilterBefore(new VarsqlSsoFilter(beanFactory.getBean(ResourceConfigConstants.APP_SSO_BEAN_FACTORY, SsoBeanFactory.class)
					,beanFactory.getBean(ResourceConfigConstants.APP_SSO_COMPONENT, SsoComponent.class)), BasicAuthenticationFilter.class);
		}

		http.headers()
			.frameOptions().sameOrigin().httpStrictTransportSecurity()
			.disable()
		.and()
			.csrf()
			.csrfTokenRepository(getCookieCsrfTokenRepository())
			.ignoringAntMatchers("/login/**","/logout","/webstatic/**","/error/**","/favicon.ico")
			.requireCsrfProtectionMatcher(new CsrfRequestMatcher())
		.and()
			//.addFilterBefore(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
			.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
		.and() //session
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
			.sessionAuthenticationErrorUrl("/login")  // remmember me error 처리 페이지. 추가.
			//.maximumSessions(1)	// 중복 로그인 카운트
			.sessionFixation().changeSessionId()	// session 공격시 session id 변경.
		.and() // login
			.formLogin()
	        .loginPage("/login")
	        .loginProcessingUrl("/login_check")
	        .usernameParameter("vsql_login_id")
	        .passwordParameter("vsql_login_password")
	        .successHandler(varsqlAuthenticationSuccessHandler)
	        .failureHandler(varsqlAuthenticationFailHandler)
	        .permitAll()
	    .and() // auth
		    .authorizeRequests()
		    .antMatchers("/sso/proc").permitAll()
     		.antMatchers("/admin/**").hasAuthority(AuthorityType.ADMIN.name())
     		.antMatchers("/manage/**").hasAnyAuthority(AuthorityType.ADMIN.name(),AuthorityType.MANAGER.name())
     		.antMatchers("/user/**","/database/**").hasAnyAuthority(AuthorityType.ADMIN.name(),AuthorityType.MANAGER.name(),AuthorityType.USER.name())
     		.antMatchers("/guest/**").hasAuthority(AuthorityType.GUEST.name())
     		.antMatchers("/login","/join/**").anonymous()
     		.antMatchers("/login_check","/index.jsp","/progress/**").permitAll()
     		.antMatchers("/**").authenticated()
     		.anyRequest().authenticated()
     	.and()
     		.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
     	.and() //log out
	     	.logout()
	        .logoutUrl("/logout")
	        .logoutSuccessUrl("/login")
	        .addLogoutHandler(varsqlAuthenticationLogoutHandler)
	        .logoutSuccessHandler(varsqlAuthenticationLogoutSuccessHandler)
	        .invalidateHttpSession(true)
	        .deleteCookies("JSESSIONID",  SecurityConstants.REMEMBERME_COOKIENAME).permitAll()
	        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

		.and()
			.httpBasic()
            .authenticationEntryPoint(varsqlBasicAuthenticationEntryPoint);
	}

	private CsrfTokenRepository getCookieCsrfTokenRepository() {
		CookieCsrfTokenRepository csrf = new CookieCsrfTokenRepository();

		csrf.setCookieHttpOnly(true);
		csrf.setCookieName(CSRF_TOKEN_NAME);
		csrf.setHeaderName(CSRF_TOKEN_NAME);
		csrf.setHeaderName(CSRF_TOKEN_NAME);
		csrf.setParameterName(CSRF_TOKEN_NAME);

		return csrf;
	}

	@Bean("varsqlRequestCache")
	public RequestCache requestCache() {
	   return new HttpSessionRequestCache();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}

	@Bean(ResourceConfigConstants.APP_PASSWORD_ENCODER)
    public PasswordEncoder varsqlPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public UserService userService() {
    	return new UserService();
    }

    @Bean
    public VarsqlAccessDeniedHandler accessDeniedHandler() {
    	return new VarsqlAccessDeniedHandler(VarsqlWebConfig.getInstance().getPageConfig().getPage403());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	 auth.authenticationProvider(new VarsqlAuthenticationProvider(userService()));
    }

    private void configureRememberMe(HttpSecurity http) throws Exception {
        http.rememberMe()
               .key(SecurityConstants.REMEMBERME_KEY)
               .rememberMeParameter(SecurityConstants.REMEMBERME_PARAMETER)
               .rememberMeCookieName(SecurityConstants.REMEMBERME_COOKIENAME)
               .tokenValiditySeconds(60 * 60 * 24 * 7)
               .authenticationSuccessHandler(varsqlAuthenticationSuccessHandler)
               .alwaysRemember(false)
               .tokenRepository(rememberMeTokenRepository())
               .userDetailsService(rememberMeUserService()).and();
	}

    @Bean(ResourceConfigConstants.APP_SSO_SIMPLE_COMPONENT)
    public SimpleSsoHandler simpleSsoComponent() {
    	return new SimpleSsoHandler();
    }
    
    @Bean(ResourceConfigConstants.REMEMBERME_USER_DETAIL_SERVICE)
    public RememberMeUserService rememberMeUserService() {
    	return new RememberMeUserService();
    }
    
    @Bean
    public RememberMeTokenRepository rememberMeTokenRepository() {
    	return new RememberMeTokenRepository();
    }
}
