package com.varsql.app.common.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.auth.Authority;



/**
 * The Class OutsideController.
 */
@Controller
public class LoginController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null && !"anonymousUser".equals(auth.getPrincipal())){	
			final Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
			
			String url  = Authority.GUEST.mainPage();
			String tmpAuthority = "";
			for (final GrantedAuthority grantedAuthority : authorities) {
				tmpAuthority = grantedAuthority.getAuthority();
				if (tmpAuthority.equals(Authority.USER.name())) {
					url  =  Authority.USER.mainPage();
					break; 
				}else if (tmpAuthority.equals(Authority.GUEST.name())) {
					url  =  Authority.GUEST.mainPage();
					break;
				}else if (tmpAuthority.equals(Authority.MANAGER.name())) {
					url  =  Authority.USER.mainPage();
					break;
				}else if (tmpAuthority.equals(Authority.ADMIN.name())) {
					url  =  Authority.USER.mainPage();
					break;
				}
			}
			
			return new ModelAndView("redirect:"+url); // Go to admin home if already
		}

		return new ModelAndView("/login/loginForm");
	}
	
	@RequestMapping(value="/login", params="fail")
	public ModelAndView loginFailed(HttpServletRequest request, HttpServletResponse response , ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("login", "fail");
		
		return  new ModelAndView("/login/loginForm",model);
	}
	
	@RequestMapping(value = "/accessDenied")
	public String accessDenied() throws Exception {
		return "redirect:/login";
	}
}
