package com.varsql.web.app.database;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.common.util.SecurityUtil;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.VarsqlUtil;



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
	public ModelAndView mainpage(HttpServletRequest req,@RequestParam(value =VarsqlParamConstants.VCONNID, required = true, defaultValue = "" )  String connuuid, ModelAndView mav) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.CONN_UUID, connuuid);
		paramMap.put(VarsqlParamConstants.VCONNID, VarsqlUtil.getVconnID(req));
		
		ModelMap model = mav.getModelMap();
		model.addAttribute(VarsqlParamConstants.LEFT_DB_OBJECT, databaseServiceImpl.schemas(paramMap));
		return  new ModelAndView("/database/main",model);
	}
	
	@RequestMapping(value = "/serviceMenu")
	public @ResponseBody Map schemas(HttpServletRequest req 
			,@RequestParam(value =VarsqlParamConstants.VCONNID , required = true, defaultValue = "" )  String connuuid
			,@RequestParam(value = "schema", required = true, defaultValue = "" )  String schema
			) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.CONN_UUID, connuuid);
		paramMap.put(VarsqlParamConstants.VCONNID, VarsqlUtil.getVconnID(req));
		
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		return databaseServiceImpl.serviceMenu(paramMap);
	}
	
	@RequestMapping(value = "/dbObjectList")
	public @ResponseBody Map dbObjectList(HttpServletRequest req 
			,@RequestParam(value = VarsqlParamConstants.VCONNID, required = false, defaultValue = "" )  String connuuid
			,@RequestParam(value = VarsqlParamConstants.DB_SCHEMA, required = false, defaultValue = "" )  String schema
			,@RequestParam(value = VarsqlParamConstants.DB_GUBUN, required = false, defaultValue = "" )  String gubun
		) throws Exception {
	
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.CONN_UUID, connuuid);
		paramMap.put(VarsqlParamConstants.VCONNID, VarsqlUtil.getVconnID(req));
		paramMap.put(VarsqlParamConstants.DB_SCHEMA, schema);
	
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		if("tables".equals(gubun)){
			return databaseServiceImpl.tables(paramMap); 
		}else if("views".equals(gubun)){
			return databaseServiceImpl.views(paramMap);
		}else if("procedures".equals(gubun)){
			return databaseServiceImpl.procedures(paramMap);
		}else if("functions".equals(gubun)){
			return databaseServiceImpl.functions(paramMap);
		}
		
		return null;
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
	public @ResponseBody Map dbObjectMetadataList(HttpServletRequest req 
			,@RequestParam(value = VarsqlParamConstants.VCONNID, required = false, defaultValue = "" )  String connuuid
			,@RequestParam(value = VarsqlParamConstants.DB_SCHEMA, required = false, defaultValue = "" )  String schema
			,@RequestParam(value = VarsqlParamConstants.DB_GUBUN, required = false, defaultValue = "" )  String gubun
			,@RequestParam(value = VarsqlParamConstants.DB_OBJECT_NAME, required = false, defaultValue = "" )  String object_nm
			) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.CONN_UUID, connuuid);
		paramMap.put(VarsqlParamConstants.VCONNID, VarsqlUtil.getVconnID(req));
		paramMap.put(VarsqlParamConstants.DB_SCHEMA, schema);
		paramMap.put(VarsqlParamConstants.DB_OBJECT_NAME, object_nm);
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		if("table".equals(gubun)){
			return databaseServiceImpl.tableMetadata(paramMap); 
		}else if("view".equals(gubun)){
			return databaseServiceImpl.viewMetadata(paramMap);
		}else if("procedure".equals(gubun)){
			return databaseServiceImpl.procedureMetadata(paramMap);
		}else if("function".equals(gubun)){
			return databaseServiceImpl.functionMetadata(paramMap);
		}
		
		return null;
	}
	
	@RequestMapping(value = "/createDDL")
	public @ResponseBody Map createDDL(HttpServletRequest req 
			,@RequestParam(value = VarsqlParamConstants.VCONNID, required = false, defaultValue = "" )  String connuuid
			,@RequestParam(value = VarsqlParamConstants.DB_SCHEMA, required = false, defaultValue = "" )  String schema
			,@RequestParam(value = VarsqlParamConstants.DB_GUBUN, required = false, defaultValue = "" )  String gubun
			,@RequestParam(value = VarsqlParamConstants.DB_OBJECT_NAME, required = false, defaultValue = "" )  String object_nm
			) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.CONN_UUID, connuuid);
		paramMap.put(VarsqlParamConstants.VCONNID, VarsqlUtil.getVconnID(req));
		paramMap.put(VarsqlParamConstants.DB_SCHEMA, schema);
		paramMap.put(VarsqlParamConstants.DB_OBJECT_NAME, object_nm);
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		if("table".equals(gubun)){
			return databaseServiceImpl.ddlTableScript(paramMap); 
		}else if("view".equals(gubun)){
			return databaseServiceImpl.ddlViewScript(paramMap);
		}else if("procedure".equals(gubun)){
			return databaseServiceImpl.ddlProcedureScript(paramMap);
		}else if("function".equals(gubun)){
			return databaseServiceImpl.ddlFunctionScript(paramMap);
		}
		
		return null;
	}
}
