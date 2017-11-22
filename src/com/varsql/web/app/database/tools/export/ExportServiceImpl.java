package com.varsql.web.app.database.tools.export;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.varsql.db.report.VarsqlReportConfig;
import com.varsql.db.util.DbInstanceFactory;
import com.varsql.web.app.database.bean.PreferencesInfo;
import com.varsql.web.app.database.tools.setting.PreferencesDAO;
import com.varsql.web.common.constants.PreferencesConstants;
import com.varsql.web.util.VarsqlUtil;
import com.vartech.common.app.beans.EnumMapperValue;

/**
 * 
 * @FileName  : AdminServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class ExportServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);
	
	@Autowired
	private ExportDAO toolsDAO ;
	
	@Autowired
	private PreferencesDAO preferencesDAO ;
	/**
	 * 
	 * @Method Name  : selectConfigInfo
	 * @Method 설명 : table export Info
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 22. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param model
	 * @throws Exception
	 */
	public void selectTableExportConfigInfo(PreferencesInfo preferencesInfo, ModelMap model) throws Exception {
		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.TABLE_EXPORT.key());
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getConnidToDbInstanceFactory(preferencesInfo.getConuid());
		
		model.addAttribute("tableInfo",dbMetaEnum.getDBMeta().getTables(preferencesInfo));
		model.addAttribute("columnInfo",Arrays.stream(VarsqlReportConfig.TABLE.values()).map(EnumMapperValue::new).collect(Collectors.toList()));
		model.addAttribute("userSettingInfo",preferencesDAO.selectPreferencesInfo(preferencesInfo));
	}
	
}