package com.varsql.web.app.setup.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.configuration.Configuration;
import com.varsql.web.app.setup.service.AppSetupService;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.setup.SetupConfigDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;

import lombok.RequiredArgsConstructor;



/**
 * app setup controller
 * 
 * @author ytkim
 *
 */
@Controller
@Conditional(AppSetupController.AppSetupControllerCondition.class)
@RequestMapping("/setup")
@RequiredArgsConstructor
public class AppSetupController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(AppSetupController.class);
	
	private final AppSetupService appSetupService;

	@GetMapping(value = {"","/"})
	public ModelAndView setup(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) throws Exception {
		logger.debug("setup page");
		ModelMap model = mav.getModelMap();
		model.addAttribute("installRoot", Configuration.getInstance().getInstallRoot());
		model.addAttribute("isInstall", Configuration.getInstance().existsAppConfigFile());
		
		return getModelAndView("/setup", VIEW_PAGE.SETUP, model);
	}

	@PostMapping(value = "/install", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseResult install(@RequestBody @Valid SetupConfigDTO setupConfigDTO, BindingResult result,HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  install validation {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		
		return appSetupService.install(setupConfigDTO);
	}
	
	public static class AppSetupControllerCondition implements Condition {
    	@Override
    	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    		return !Configuration.getInstance().existsAppConfigFile();
    	}
    }	
	
}
