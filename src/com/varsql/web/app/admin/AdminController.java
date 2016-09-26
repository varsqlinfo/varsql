package com.varsql.web.app.admin;

import java.util.HashMap;
import java.util.Map;

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

import com.varsql.web.common.constants.DBMenuConstants;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.HttpUtil;
import com.varsql.web.util.VarsqlUtil;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/admin")
public class AdminController{

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	AdminService adminService; 
	
	@RequestMapping({""})
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("redirect:/admin/");
	}
	
	@RequestMapping({"/","/main"})
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		model.addAttribute("dbtype", adminService.selectAllDbType());
		return  new ModelAndView("/admin/adminMain",model);
	}
	
	@RequestMapping(value = "/report")
	public ModelAndView report(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		return new ModelAndView("/admin/report",model);
	}
	
	@RequestMapping(value = "/managerMgmt")
	public ModelAndView managerMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		return new ModelAndView("/admin/managerMgmt",model);
	}
	
	@RequestMapping(value = "/databaseUserMgmt")
	public ModelAndView databaseUserMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		return new ModelAndView("/admin/databaseUserMgmt",model);
	}
	
	@RequestMapping(value = "/userMenuMgmt")
	public ModelAndView userMenuMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		model.addAttribute("dbtype", adminService.selectAllDbType());
		return new ModelAndView("/admin/userMenuMgmt",model);
	}
	
	/**
	 * 
	 * @Method Name  : dblist
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : db 목록 보기
	 * @param searchval
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/main/dblist")
	public @ResponseBody String dblist(
			@RequestParam(value = VarsqlParamConstants.SEARCHVAL, required = false, defaultValue = "" )  String searchval
			,@RequestParam(value = VarsqlParamConstants.SEARCH_NO, required = false, defaultValue = "1" )  int pageNo
			,@RequestParam(value = VarsqlParamConstants.SEARCH_ROW, required = false, defaultValue = "10" )  int rows
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put(VarsqlParamConstants.SEARCHVAL, searchval);
		paramMap.put(VarsqlParamConstants.SEARCH_NO, pageNo);
		paramMap.put(VarsqlParamConstants.SEARCH_ROW, rows);
		
		return adminService.selectPageList(paramMap);
	}
	
	/**
	 *
	 * @Method Name  : dbDetail
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : db정보 상세보기
	 * @param vconid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/main/dbDetail")
	public @ResponseBody String dbDetail(@RequestParam(value = "vconid") String vconid) throws Exception {
		
		DataCommonVO dcv = new DataCommonVO();
		
		dcv.put("vconid", vconid);
		
		return adminService.selectDetailObject(dcv);
	}
	
	/**
	 * 
	 * @Method Name  : dbConnectionCheck
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 :커넥션 체크
	 * @param vconid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/main/dbConnectionCheck")
	public @ResponseBody String dbConnectionCheck(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		DataCommonVO dcv = HttpUtil.getAllParameter(req);
		
		return adminService.connectionCheck(dcv);
	}
	
	/**
	 * 
	 * @Method Name  : dbSave
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 정보 저장
	 * @param vconid
	 * @param vname
	 * @param vurl
	 * @param vdriver
	 * @param vtype
	 * @param vid
	 * @param vpw
	 * @param vconnopt
	 * @param vpoolopt
	 * @param vsql
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/main/dbSave")
	public @ResponseBody String dbSave(@RequestParam(value = "vconid", required = false, defaultValue = "")  String vconid
			,@RequestParam(value = "vname")  String vname
			,@RequestParam(value = "vurl")  String vurl
			,@RequestParam(value = "vdriver")  String vdriver
			,@RequestParam(value = "vtype")  String vtype
			,@RequestParam(value = "vid")  String vid
			,@RequestParam(value = "vpw")  String vpw
			,@RequestParam(value = "vconnopt")  String vconnopt
			,@RequestParam(value = "vpoolopt")  String vpoolopt
			,@RequestParam(value = "vsql")  String vsql
			,@RequestParam(value = "pollinit")  String pollinit
			)throws Exception {
			
		DataCommonVO dcv = new DataCommonVO();
		
		dcv.put("vconid", vconid);
		dcv.put("vname", vname);
		dcv.put("vurl", vurl);
		dcv.put("vdriver", vdriver);
		dcv.put("vtype", vtype);
		dcv.put("vid", vid);
		dcv.put("vpw", vpw);
		dcv.put("vconnopt", vconnopt);
		dcv.put("vpoolopt", vpoolopt);
		dcv.put("vsql", vsql);
		dcv.put("pollinit", pollinit);
		
		Map json = new HashMap();
			
		if("".equals(vconid)){
			json.put("result", adminService.insertVtconnectionInfo(dcv));
		}else{
			json.put("result", adminService.updateVtconnectionInfo(dcv));
		}
			
		return VarsqlUtil.objectToString(json);
	}
	
	/**
	 * 
	 * @Method Name  : dbDelete
	 * @작성일   : 2014. 8. 18. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : db 정보 삭제 
	 * @param vconid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/main/dbDelete")
	public @ResponseBody String dbDelete(@RequestParam(value = "vconid")  String vconid) throws Exception {
		
		DataCommonVO dcv = new DataCommonVO();
		
		dcv.put("vconid", vconid);
		
		Map json = new HashMap();
		
		json.put("result", adminService.deleteVtconnectionInfo(dcv));
		
		return VarsqlUtil.objectToString(json);
	}
}
