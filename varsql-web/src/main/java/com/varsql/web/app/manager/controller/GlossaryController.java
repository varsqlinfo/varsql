package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.manager.service.GlossaryServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.GlossaryRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;

import lombok.RequiredArgsConstructor;



/**
 *
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: GlossaryController.java
* @DESC		: 용어집
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager/glossary")
@RequiredArgsConstructor
public class GlossaryController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(GlossaryController.class);

	private final GlossaryServiceImpl glossaryServiceImpl;
	
	
	/**
	 *
	 * @Method Name  : glossaryMgmt
	 * @Method 설명 : 용어집
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = {"", "/","/main"})
	public ModelAndView glossaryMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "glossaryMgmt");
		return getModelAndView("/glossaryMgmt", VIEW_PAGE.MANAGER,model);
	}

	/**
	 *
	 * @Method Name  : list
	 * @Method 설명 : 목록
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19.
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public @ResponseBody ResponseResult list(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		return glossaryServiceImpl.selectGlossaryList(searchParameter);
	}

	/**
	 *
	 * @Method Name  : save
	 * @Method 설명 : 추가.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19.
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseResult save(@Valid GlossaryRequestDTO glossaryInfo, BindingResult result,HttpServletRequest req) throws Exception {
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  GlossaryController save check {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		return glossaryServiceImpl.saveGlossaryInfo(glossaryInfo);
	}

	/**
	 *
	 * @Method Name  : delete
	 * @Method 설명 : 삭제
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19.
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody ResponseResult delete(@RequestParam(value = "wordIdx" , required = true) String wordIdx) throws Exception {
		return glossaryServiceImpl.deleteGlossaryInfo(wordIdx);
	}
}
