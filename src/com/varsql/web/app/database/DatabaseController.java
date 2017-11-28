package com.varsql.web.app.database;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import  com.varsql.db.beans.DatabaseParamInfo;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.vartech.common.app.beans.ResponseResult;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/database")
public class DatabaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);
	
	@Autowired
	private DatabaseServiceImpl databaseServiceImpl;
	
	@RequestMapping({"/","/main"})
	public ModelAndView mainpage(DatabaseParamInfo databaseParamInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute(VarsqlParamConstants.LEFT_DB_OBJECT, databaseServiceImpl.schemas(databaseParamInfo));
		return  new ModelAndView("/database/main",model);
	}
	
	/**
	 * 
	 * @Method Name  : schemas
	 * @Method 설명 : servicemenu
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 6. 
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/serviceMenu")
	public @ResponseBody ResponseResult serviceMenu(DatabaseParamInfo databaseParamInfo, HttpServletRequest req) throws Exception {
		return databaseServiceImpl.serviceMenu(databaseParamInfo);
	}
	
	/**
	 * 
	 * @Method Name  : dbObjectList
	 * @Method 설명 : db object list 
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 6. 
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbObjectList")
	public @ResponseBody ResponseResult dbObjectList(DatabaseParamInfo databaseParamInfo, HttpServletRequest req) throws Exception {
		return databaseServiceImpl.dbObjectList(databaseParamInfo);
	}
	
	/**
	 * 
	 * @Method Name  : dbObjectMetadataList
	 * @Method 설명 : service menu 클릭했을때 그 메뉴에 대한 메타 데이타 가져오기. 
	 * @작성일   : 2015. 4. 13. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param req
	 * @param vconnid
	 * @param schema
	 * @param gubun
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbObjectMetadataList")
	public @ResponseBody ResponseResult dbObjectMetadataList(DatabaseParamInfo databaseParamInfo, HttpServletRequest req) throws Exception {
		
		return databaseServiceImpl.dbObjectMetadataList(databaseParamInfo);
	}
	
	/**
	 * 
	 * @Method Name  : createDDL
	 * @Method 설명 : 생성 스크립트.  
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 6. 
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createDDL")
	public @ResponseBody ResponseResult createDDL(DatabaseParamInfo databaseParamInfo, HttpServletRequest req) throws Exception {
		
		return databaseServiceImpl.createDDL(databaseParamInfo);
		
	}
}
