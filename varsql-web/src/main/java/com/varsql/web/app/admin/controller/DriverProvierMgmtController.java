package com.varsql.web.app.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.admin.service.DriverProvierMgmtServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.dto.db.DBTypeDriverProviderRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;

@Controller
@RequestMapping({ "/admin/driverMgmt" })
public class DriverProvierMgmtController extends AbstractController {

	private DriverProvierMgmtServiceImpl driverProvierMgmtServiceImpl;
	
	public DriverProvierMgmtController(DriverProvierMgmtServiceImpl driverMgmtServiceImpl) {
		this.driverProvierMgmtServiceImpl = driverMgmtServiceImpl; 
	}

	@RequestMapping(value = { "/list" }, method = { RequestMethod.POST })
	@ResponseBody
	public ResponseResult list(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		return this.driverProvierMgmtServiceImpl.list(searchParameter);
	}

	@PostMapping({ "/dbTypeList" })
	@ResponseBody
	public ResponseResult dbDriver() throws Exception {
		return this.driverProvierMgmtServiceImpl.dbTypeList();
	}

	@PostMapping({ "/detail" })
	@ResponseBody
	public ResponseResult detail(@RequestParam(value = "driverProviderId", required = true) String driverProviderId) throws Exception {
		return this.driverProvierMgmtServiceImpl.detail(driverProviderId);
	}

	@RequestMapping(value = { "/driverCheck" }, method = { RequestMethod.POST })
	@ResponseBody
	public ResponseResult driverCheck(@Valid DBTypeDriverProviderRequestDTO dto, BindingResult result,
			HttpServletRequest req) throws Exception {
		if (result.hasErrors()) {
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		return this.driverProvierMgmtServiceImpl.driverCheck(dto);
	}

	@RequestMapping(value = { "/save" }, method = { RequestMethod.POST })
	@ResponseBody
	public ResponseResult save(@Valid DBTypeDriverProviderRequestDTO dto, BindingResult result,
			HttpServletRequest req) throws Exception {
		if (result.hasErrors()) {
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		return this.driverProvierMgmtServiceImpl.saveInfo(dto);
	}

	@RequestMapping(value = { "/delete" }, method = { RequestMethod.POST })
	@ResponseBody
	public ResponseResult delete(@RequestParam(value = "driverProviderId", required = true) String driverProviderId) throws Exception {
		return VarsqlUtils.getResponseResultItemOne(driverProvierMgmtServiceImpl.deleteInfo(driverProviderId));
	}

	@PostMapping({ "/driverList" })
	@ResponseBody
	public ResponseResult dbDriver(@RequestParam(value = "dbtype", required = true) String dbtype) throws Exception {
		return this.driverProvierMgmtServiceImpl.driverList(dbtype);
	}
}
