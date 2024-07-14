package com.varsql.web.common.controller.handler;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.varsql.core.configuration.Configuration;
import com.varsql.web.tags.VarsqlFn;

/**
 * -----------------------------------------------------------------------------
* @fileName		: CommonControllerAdvice.java
* @desc		: 공통  request attirbute 처리
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 6. 30. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@ControllerAdvice
public class CommonControllerAdvice {

  @ModelAttribute
  public void handleRequest(HttpServletRequest request, Model model) {

	  String contextPath = request.getContextPath();

      model.addAttribute("fileUploadSize", Configuration.getInstance().getFileUploadSize());
      model.addAttribute("fileUploadSizePerFile", Configuration.getInstance().getFileUploadSizePerFile());
      model.addAttribute("pageContextPath", contextPath);
      model.addAttribute("pubjs_ver", VarsqlFn.pubJsVersion());
      model.addAttribute("codeEditor_ver", VarsqlFn.staticResourceVersion("codeEditor_ver"));
      model.addAttribute("css_ver", VarsqlFn.staticResourceVersion("css"));
      model.addAttribute("prettify_ver", VarsqlFn.staticResourceVersion("prettify"));
      model.addAttribute("static_random_ver", VarsqlFn.randomVal(10000));
  }
}