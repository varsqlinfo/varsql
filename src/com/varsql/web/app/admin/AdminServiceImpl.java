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
import com.varsql.common.util.SecurityUtil;
import com.varsql.db.ConnectionFactory;
import com.varsql.sql.util.SQLUtil;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.constants.ResultConstants;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

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
	
	/**
	 * 
	 * @Method Name  : selectDblist
	 * @Method 설명 : db 목록 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectDblist(SearchParameter searchParameter) {
		
		ResponseResult resultObject = new ResponseResult();
		
		int totalcnt = adminDAO.selectDBTotalCnt(searchParameter);
		
		if(totalcnt > 0){
			resultObject.setItemList(adminDAO.selectDbList(searchParameter));
		}else{
			resultObject.setItemList(null);
		}
		resultObject.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return resultObject;
	}
		
	/**
	 * 
	 * @Method Name  : selectDetailObject
	 * @Method 설명 : db 정보 상세.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectDetailObject(DataCommonVO paramMap) {
		ResponseResult resultObject = new ResponseResult();
		
		resultObject.setItemOne(adminDAO.selectDetailObject(paramMap));
		return resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : connectionCheck
	 * @Method 설명 : 커넥션 체크. 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22. 
	 * @변경이력  :
	 * @param dcv
	 * @return
	 */
	public ResponseResult connectionCheck(ParamMap dcv) {
		
		ResponseResult resultObject = new ResponseResult();
		logger.debug("connection check object :  {}",dcv);
		
		String driver = dcv.getString("vdriver");
		String url = dcv.getString("vurl");
		String username = dcv.getString("vid");
		String pwd = dcv.getString("vpw");
		String dbtype = dcv.getString("vtype");
		
		String connection_opt = dcv.getString("vconnopt");
		String validation_query = dcv.getString("vquery","");
		
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
			validation_query = validation_query != null && !"".equals(validation_query.trim()) ?validation_query :com.varsql.db.validation.Validation.getInstance().validationQuery(dbtype);
			
			pstmt=connChk.prepareStatement(validation_query);
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
		
		resultObject.setMessageCode(result);
		resultObject.setMessage(failMessage);
		
		return resultObject;
		
	}
	
	/**
	 * 
	 * @Method Name  : insertVtconnectionInfo
	 * @Method 설명 : 정보 저장
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult saveVtconnectionInfo(DataCommonVO paramMap) {
		
		ResponseResult resultObject = new ResponseResult();
		
		SecurityUtil.setUserInfo(paramMap);
		
		if("".equals(paramMap.getString("vconid"))){
			String vconid = adminDAO.selectVtconnectionMaxVal();
			try{
				vconid=String.format("%05d", Integer.parseInt(vconid)+1);
			}catch(Exception e){
				vconid=String.format("%05d", 1);
			}
			paramMap.put("vconid", vconid);
				
			resultObject.setItemOne(adminDAO.insertVtconnectionInfo(paramMap));
		}else{
			int result =adminDAO.updateVtconnectionInfo(paramMap);
			
			if(result > 0 && "Y".equals(paramMap.getString("pollinit"))){
				try {
					ConnectionFactory.getInstance().resetConnectionPool(paramMap.getString("vconid"));
					resultObject.setResultCode(ResultConstants.CODE_VAL.SUCCESS.intVal());
				} catch (Exception e) {
					resultObject.setResultCode(ResultConstants.CODE_VAL.ERROR.intVal());
					resultObject.setMessage(e.getMessage());
				}
			}
		}
		return resultObject;
	}
	
	
	public boolean deleteVtconnectionInfo(DataCommonVO paramMap) {
		SecurityUtil.setUserInfo(paramMap);
		return adminDAO.deleteVtconnectionInfo(paramMap) > 0;
	}

	public List selectAllDbType() {
		return adminDAO.selectAllDbType();
	}
	
	/**
	 * 
	 * @Method Name  : selectDbDriverList
	 * @Method 설명 : db driver list
	 * @작성자   : ytkim
	 * @작성일   : 2017. 5. 25. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectDbDriverList(DataCommonVO paramMap) {
		ResponseResult resultObject = new ResponseResult();
		resultObject.setItemList(adminDAO.selectDbDriverList(paramMap));
		
		return resultObject;
	}
}