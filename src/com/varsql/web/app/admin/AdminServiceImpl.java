package com.varsql.web.app.admin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.common.util.CommUtil;
import com.varsql.db.ConnectionFactory;
import com.varsql.sql.SQLUtil;
import com.varsql.web.common.constants.ResultConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.PagingUtil;
import com.varsql.web.util.VarsqlUtil;

/**
 * 
 * @FileName  : AdminServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class AdminServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
	
	@Autowired
	AdminDAO adminDAO ;
	
	public Map selectPageList(DataCommonVO paramMap) {
		
		int totalcnt = adminDAO.selectPageTotalCnt(paramMap);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			int page = paramMap.getInt("page",0);
			int rows = paramMap.getInt("rows",10);
			
			int first = (page-1)*rows ;
			
			paramMap.put("first", first);
			paramMap.put("rows", rows);
			
			json.put("paging", PagingUtil.getPageObject(totalcnt, page,rows));
			json.put("result", adminDAO.selectPageList(paramMap));
		}
		
		return json;
	}
	
	public Map selectDetailObject(DataCommonVO paramMap) {
		
		Map json = new HashMap();
		json.put("result", adminDAO.selectDetailObject(paramMap));
		return json;
	}
	
	public Map connectionCheck(DataCommonVO dcv) {
		
		Map json = new HashMap();
		
		logger.debug("connection check object :  {}",dcv);
		
		String driver = dcv.getString("vdriver");
		String url = dcv.getString("vurl");
		String username = dcv.getString("vid");
		String pwd = dcv.getString("vpw");
		String dbtype = dcv.getString("vtype");
		
		String connection_opt = dcv.getString("vconnopt");
		String validation_sql = dcv.getString("vsql","");
		
		Properties p =new Properties();
		
		p.setProperty("user", username);
		p.setProperty("password", pwd);
		
		if(null != connection_opt && !"".equals(connection_opt)){
			String [] tmpOpt = CommUtil.split(connection_opt, ";");
			
			String [] optVal = null;
			String tmpKey = "";
			for (int i = 0; i < tmpOpt.length; i++) {
				
				tmpKey=tmpOpt[i];
				
				if("".equals(tmpKey.trim())){
					continue; 
				}
				
				optVal = CommUtil.split(tmpKey, "=");
				
				p.setProperty(optVal[0], ( optVal.length > 1 ? optVal[1]:"" ));
			}
		}
		
		String result = "";
		String failMessage = "";
		
		PreparedStatement pstmt = null;
		Connection connChk = null;
		try {
			Class.forName(driver);
			connChk = DriverManager.getConnection(url, p);
			validation_sql = validation_sql != null && !"".equals(validation_sql.trim()) ?validation_sql :com.varsql.db.validation.Validation.getInstance().validationQuery(dbtype);
			
			pstmt=connChk.prepareStatement(validation_sql);
			pstmt.executeQuery();
			
			result="success";
			connChk.close();
		} catch (ClassNotFoundException e) {
			result="fail";
			failMessage = e.getMessage();
			logger.error(this.getClass().getName() , e);
		} catch (SQLException e) {
			result="fail";
			failMessage = e.getMessage();
			logger.error(this.getClass().getName() , e);
		}finally{
			SQLUtil.close(connChk , pstmt, null);
		}
		
		json.put("result", result);
		json.put("msg", failMessage);
		
		return json;
		
	}

	public boolean insertVtconnectionInfo(DataCommonVO paramMap) {
		
		String vconid = adminDAO.selectVtconnectionMaxVal();
		
		try{
			vconid=String.format("%05d", Integer.parseInt(vconid)+1);
		}catch(Exception e){
			vconid=String.format("%05d", 1);
		}
		
		paramMap.put("vconid", vconid);
		
		return adminDAO.insertVtconnectionInfo(paramMap) > 0;
	}

	public Map updateVtconnectionInfo(DataCommonVO paramMap) {
		Map json = new HashMap();
		try {
			
			int result =adminDAO.updateVtconnectionInfo(paramMap);
			
			if(result > 0 && "Y".equals(paramMap.getString("pollinit"))){
				ConnectionFactory.getInstance().resetConnectionPool(paramMap.getString("vconid"));
				json.put(ResultConstants.CODE,ResultConstants.CODE_VAL.SUCCESS);
			}
		} catch (Exception e) {
			json.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
			json.put(ResultConstants.MESSAGE,e.getMessage());
		}
		
		return json;
	}
	
	public boolean deleteVtconnectionInfo(DataCommonVO paramMap) {
		return adminDAO.deleteVtconnectionInfo(paramMap) > 0;
	}

	public List selectAllDbType() {
		return adminDAO.selectAllDbType();
	}
}