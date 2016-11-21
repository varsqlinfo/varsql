package com.varsql.web.app.database.oracle;

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

import com.varsql.common.util.SecurityUtil;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/database/oracle")
public class OracleController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(OracleController.class);
	
	@Autowired
	private OracleServiceImpl oracleService;

	@RequestMapping({""})
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("redirect:/database/oracle/");
	}
	
	@RequestMapping({"/","/main"})
	public ModelAndView mainpage(@RequestParam(value = "vconnid", required = true, defaultValue = "" )  String vconnid, ModelAndView mav) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.VCONNID, vconnid);
		
		ModelMap model = mav.getModelMap();
		model.addAttribute(VarsqlParamConstants.LEFT_DB_OBJECT, oracleService.schemas(paramMap));
		return  new ModelAndView("/database/oracle/oracleMain",model);
	}
	
	@RequestMapping(value = "/serviceMenu")
	public @ResponseBody String schemas(HttpServletRequest req 
			,@RequestParam(value = "vconnid", required = false, defaultValue = "" )  String vconnid
			) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.VCONNID, vconnid);
		
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		return oracleService.serviceMenu(paramMap);
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
			return oracleService.tables(paramMap); 
		}else if("views".equals(gubun)){
			return oracleService.views(paramMap);
		}else if("procedures".equals(gubun)){
			return oracleService.procedures(paramMap);
		}else if("functions".equals(gubun)){
			return oracleService.functions(paramMap);
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
			return oracleService.tableMetadata(paramMap); 
		}else if("view".equals(gubun)){
			return oracleService.viewMetadata(paramMap);
		}else if("procedure".equals(gubun)){
			return oracleService.procedureMetadata(paramMap);
		}else if("function".equals(gubun)){
			return oracleService.functionMetadata(paramMap);
		}
		
		return "";
	}
	
	@RequestMapping(value = "/createDDL")
	public @ResponseBody String createDDL(HttpServletRequest req 
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
			return oracleService.ddlTableScript(paramMap); 
		}else if("view".equals(gubun)){
			return oracleService.ddlViewScript(paramMap);
		}else if("procedure".equals(gubun)){
			return oracleService.ddlProcedureScript(paramMap);
		}else if("function".equals(gubun)){
			return oracleService.ddlFunctionScript(paramMap);
		}
		
		return "";
	}
}
