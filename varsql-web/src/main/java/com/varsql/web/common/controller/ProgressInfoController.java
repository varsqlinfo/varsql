package com.varsql.web.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.constants.HttpSessionConstants;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;

/**
 * -----------------------------------------------------------------------------
* @fileName : ProgressInfoController.java
* @desc		: progress info
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2022. 2. 4. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/progress")
public class ProgressInfoController extends AbstractController  {

	@PostMapping(value = { "/info" })
	@ResponseBody
	public ResponseResult progress(@RequestParam(value = "requid", required = true) String requestUid,
			@RequestParam(value = "type", required = true) String type, HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String sessAttrKey = HttpSessionConstants.progressKey(requestUid);
		Object obj = req.getSession().getAttribute(sessAttrKey);
		
		if (obj == "complete") {
			req.getSession(true).removeAttribute(sessAttrKey);
		}

		return VarsqlUtils.getResponseResultItemOne(obj);

	}

}
