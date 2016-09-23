package com.varsql.web.app.database.db2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.SecurityUtil;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/database/db2")
public class Db2Controller {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Db2Controller.class);
	
	@Autowired
	private Db2Service db2Service;

	@RequestMapping({""})
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("redirect:/database/db2/");
	}
	
	@RequestMapping({"/","/main"})
	public ModelAndView mainpage(@RequestParam(value = "vconnid", required = true, defaultValue = "" )  String vconnid, ModelAndView mav) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.VCONNID, vconnid);
		
		ModelMap model = mav.getModelMap();
		model.addAttribute(VarsqlParamConstants.LEFT_DB_OBJECT, db2Service.schemas(paramMap));
		return  new ModelAndView("/database/db2/db2Main",model);
	}
	
	@RequestMapping(value = "/serviceMenu")
	public @ResponseBody String schemas(HttpServletRequest req 
			,@RequestParam(value = "vconnid", required = false, defaultValue = "" )  String vconnid
			) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.VCONNID, vconnid);
		
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		return db2Service.serviceMenu(paramMap);
	}
	
	@RequestMapping(value = "/dbObjectList")
	public @ResponseBody String dbObjectList(HttpServletRequest req 
			,@RequestParam(value = VarsqlParamConstants.VCONNID, required = false, defaultValue = "" )  String vconnid
			,@RequestParam(value = VarsqlParamConstants.DB_SCHEMA, required = false, defaultValue = "" )  String schema
			,@RequestParam(value = VarsqlParamConstants.DB_GUBUN, required = false, defaultValue = "" )  String gubun
		) throws Exception {
	
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.VCONNID, vconnid);
		paramMap.put(VarsqlParamConstants.DB_SCHEMA, schema);
	
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		if("tables".equals(gubun)){
			return db2Service.tables(paramMap); 
		}else if("views".equals(gubun)){
			return db2Service.views(paramMap);
		}else if("procedures".equals(gubun)){
			return db2Service.procedures(paramMap);
		}else if("functions".equals(gubun)){
			return db2Service.functions(paramMap);
		}
		
		return "";
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
	public @ResponseBody String dbObjectMetadataList(HttpServletRequest req 
			,@RequestParam(value = VarsqlParamConstants.VCONNID, required = false, defaultValue = "" )  String vconnid
			,@RequestParam(value = VarsqlParamConstants.DB_SCHEMA, required = false, defaultValue = "" )  String schema
			,@RequestParam(value = VarsqlParamConstants.DB_GUBUN, required = false, defaultValue = "" )  String gubun
			,@RequestParam(value = VarsqlParamConstants.DB_OBJECT_NAME, required = false, defaultValue = "" )  String object_nm
			) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.VCONNID, vconnid);
		paramMap.put(VarsqlParamConstants.DB_SCHEMA, schema);
		paramMap.put(VarsqlParamConstants.DB_OBJECT_NAME, object_nm);
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		if("table".equals(gubun)){
			return db2Service.tableMetadata(paramMap); 
		}else if("view".equals(gubun)){
			return db2Service.viewMetadata(paramMap);
		}else if("procedure".equals(gubun)){
			return db2Service.procedureMetadata(paramMap);
		}else if("function".equals(gubun)){
			return db2Service.functionMetadata(paramMap);
		}
		
		return "";
	}
}
