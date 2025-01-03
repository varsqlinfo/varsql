package com.varsql.web.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.varsql.core.auth.AuthorityTypeImpl;
import com.varsql.core.common.code.VarsqlSsoType;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.configuration.VarsqlWebConfig;
import com.varsql.web.common.filter.AppInitFilter;
import com.varsql.web.common.filter.VarsqlSsoFilter;
import com.varsql.web.common.sso.SsoBeanFactory;
import com.varsql.web.common.sso.SsoComponent;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.SecurityConstants;
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
	
	public static final String WEB_RESOURCES = "/webstatic/**";

	private VarsqlBasicAuthenticationEntryPoint varsqlBasicAuthenticationEntryPoint;

	private VarsqlAuthenticationFailHandler varsqlAuthenticationFailHandler;
	
	private VarsqlAuthenticationSuccessHandler varsqlAuthenticationSuccessHandler;

	private VarsqlAuthenticationLogoutHandler varsqlAuthenticationLogoutHandler;

	private VarsqlAuthenticationLogoutSuccessHandler varsqlAuthenticationLogoutSuccessHandler;
	
	private BeanFactory beanFactory;
	
	// web static resource path
	private OrRequestMatcher staticRequestMatcher = new OrRequestMatcher(
		new AntPathRequestMatcher(WEB_RESOURCES)
		, new AntPathRequestMatcher("/error/**")
		, new AntPathRequestMatcher("/**/favicon.ico")
		, new AntPathRequestMatcher("/favicon.ico")
	);
	
	private OrRequestMatcher csrfIgnoreRequestMatcher = new OrRequestMatcher(
		 new AntPathRequestMatcher("/ws/**")
		, new AntPathRequestMatcher("/login/**")
		, new AntPathRequestMatcher("/logout")
	);
	
	// ajax header matcher
	private RequestMatcher ajaxRequestMatcher = new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest");

	public SecurityConfigurer(
			VarsqlBasicAuthenticationEntryPoint varsqlBasicAuthenticationEntryPoint
			,VarsqlAuthenticationFailHandler varsqlAuthenticationFailHandler
			,VarsqlAuthenticationLogoutHandler varsqlAuthenticationLogoutHandler
			,VarsqlAuthenticationSuccessHandler varsqlAuthenticationSuccessHandler
			,VarsqlAuthenticationLogoutSuccessHandler varsqlAuthenticationLogoutSuccessHandler
			,BeanFactory beanFactory) {
		
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
            .requestMatchers(staticRequestMatcher)
         .and().httpFirewall(firewall);

    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		if(!Configuration.getInstance().isInit()) {
			System.out.println("is init : " +  Configuration.getInstance().isInit());
			System.out.println("app filter  ");
			
			http.addFilterBefore(new AppInitFilter(), BasicAuthenticationFilter.class);
			return ;
		}
		
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
			.ignoringRequestMatchers(staticRequestMatcher, csrfIgnoreRequestMatcher)
			.requireCsrfProtectionMatcher(ajaxRequestMatcher)
		.and() //session  
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 생성 방법 
			.sessionFixation().newSession() // sessionFixation 세션 id 처리 방법
			.sessionAuthenticationErrorUrl("/login")  // remmember me error 처리 페이지. 추가.
			//.maximumSessions(1)	// 중복 로그인 카운트
			//.sessionFixation().changeSessionId()	// session 공격시 session id 변경.
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
     		.antMatchers("/admin/**").hasAuthority(AuthorityTypeImpl.ADMIN.name())
     		.antMatchers("/manager/**").hasAnyAuthority(AuthorityTypeImpl.ADMIN.name(),AuthorityTypeImpl.MANAGER.name())
     		.antMatchers("/user/**","/database/**").hasAnyAuthority(AuthorityTypeImpl.ADMIN.name(),AuthorityTypeImpl.MANAGER.name(),AuthorityTypeImpl.USER.name())
     		.antMatchers("/guest/**").hasAuthority(AuthorityTypeImpl.GUEST.name())
     		.antMatchers("/login","/join/**","/lostPassword","/resetPassword").anonymous()
     		.antMatchers("/i18nMessage","/login_check","/index.jsp","/progress/**").permitAll()
     		.antMatchers("/**").authenticated()
     		.anyRequest().authenticated()
     	.and()
     		.exceptionHandling()
     		.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.NOT_FOUND), staticRequestMatcher)
     		.defaultAuthenticationEntryPointFor(restAuthenticationEntryPoint(), ajaxRequestMatcher)
     		.accessDeniedHandler(accessDeniedHandler())
     	.and() //log out
	     	.logout()
	        .logoutUrl("/logout")
	        .addLogoutHandler(varsqlAuthenticationLogoutHandler)
	        .logoutSuccessHandler(varsqlAuthenticationLogoutSuccessHandler)
	        .invalidateHttpSession(true)
	        .deleteCookies(SecurityConstants.JSESSION_ID_COOKIE_NAME, SecurityConstants.REMEMBERME_COOKIENAME).permitAll()
	        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

		.and()
			.httpBasic()
            .authenticationEntryPoint(varsqlBasicAuthenticationEntryPoint);
	}

	// ajax call entry point
	private AuthenticationEntryPoint restAuthenticationEntryPoint() {
		return  new AuthenticationEntryPoint () {

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
				request.getRequestDispatcher("/invalidLogin").forward(request, response);
			}
		};
	}
	
	private CsrfTokenRepository getCookieCsrfTokenRepository() {
		CookieCsrfTokenRepository csrf = new CookieCsrfTokenRepository();

		csrf.setCookieHttpOnly(true);
		csrf.setCookieName(CSRF_TOKEN_NAME);
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
    	PersistentTokenRepository tokenRepository = rememberMeTokenRepository();
    	RememberMeUserService rememberMeUserService = rememberMeUserService();
        http.rememberMe()
               .key(SecurityConstants.REMEMBERME_KEY)
               .rememberMeServices(rememberMeServices(tokenRepository, rememberMeUserService))
               .authenticationSuccessHandler(varsqlAuthenticationSuccessHandler)
               .tokenRepository(tokenRepository)
               .userDetailsService(rememberMeUserService).and();
	}

    @Bean(ResourceConfigConstants.REMEMBERME_USER_DETAIL_SERVICE)
    public RememberMeUserService rememberMeUserService() {
    	return new RememberMeUserService();
    }
    
    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices(PersistentTokenRepository tokenRepository, RememberMeUserService rememberMeUserService) {
        PersistentTokenBasedRememberMeServices rememberMeServices = new
                PersistentTokenBasedRememberMeServices(SecurityConstants.REMEMBERME_KEY, rememberMeUserService, tokenRepository);
        rememberMeServices.setParameter(SecurityConstants.REMEMBERME_PARAMETER);
        rememberMeServices.setAlwaysRemember(false);
        rememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 7);
        rememberMeServices.setCookieName(SecurityConstants.REMEMBERME_COOKIENAME);
        
        return rememberMeServices;
    }
    
    @Bean
    public RememberMeTokenRepository rememberMeTokenRepository() {
    	return new RememberMeTokenRepository();
    }
}
