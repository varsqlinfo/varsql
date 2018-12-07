package com.varsql.app.admin.service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.admin.beans.Vtconnection;
import com.varsql.app.admin.beans.VtconnectionOption;
import com.varsql.app.admin.dao.AdminDAO;
import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.ResultConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.sql.util.SQLUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;
import com.vartech.common.utils.StringUtil;
import com.vartech.common.utils.VartechUtils;

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
	 * @param vtConnection
	 * @return
	 */
	public ResponseResult connectionCheck(Vtconnection vtConnection) {
		
		ResponseResult resultObject = new ResponseResult();
		
		logger.debug("connection check object :  {}" , VartechUtils.reflectionToString(vtConnection));
		
		String driver = vtConnection.getVdriver();
		String url = vtConnection.getVurl();
		String username = vtConnection.getVid();
		String pwd = vtConnection.getVpw();
		String dbtype = vtConnection.getVtype();
		
		if(!"".equals(vtConnection.getVconnid()) && (pwd ==null || "".equals(pwd))){
			pwd = adminDAO.selectDbPassword(vtConnection);
		}
		
		Properties p =new Properties();
		
		p.setProperty("user", username);
		p.setProperty("password", pwd);
		
		String result = "";
		String failMessage = "";
		
		PreparedStatement pstmt = null;
		Connection connChk = null;
		try {
			Class.forName(driver);
			connChk = DriverManager.getConnection(url, p);
			String validation_query = ValidationProperty.getInstance().validationQuery(dbtype);
			
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
	 * @param vtConnection
	 * @return
	 */
	public ResponseResult saveVtconnectionInfo(Vtconnection vtConnection) {
		
		ResponseResult resultObject = new ResponseResult();
		
		vtConnection.setUserId(SecurityUtil.loginId());
		
		if(vtConnection.getVconnid()==null || "".equals(StringUtil.allTrim(vtConnection.getVconnid()))){
			String vconnid = adminDAO.selectVtconnectionMaxVal();
			try{
				vconnid=String.format("%05d", Integer.parseInt(vconnid)+1);
			}catch(Exception e){
				vconnid=String.format("%05d", 1);
			}
			vtConnection.setVconnid(vconnid);
				
			resultObject.setItemOne(adminDAO.insertVtconnectionInfo(vtConnection));
		}else{
			int result =adminDAO.updateVtconnectionInfo(vtConnection);
			
			if(result > 0 && "Y".equals(vtConnection.getPollinit())){
				try {
					ConnectionFactory.getInstance().resetConnectionPool(vtConnection.getVconnid());
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
	
	/**
	 * 
	 * @Method Name  : saveVtconnectionOptionInfo
	 * @Method 설명 : connection option 정보 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 2. 19. 
	 * @변경이력  :
	 * @param vtconnectionOption
	 * @return
	 */
	public ResponseResult saveVtconnectionOptionInfo(VtconnectionOption vtconnectionOption) {
		ResponseResult resultObject = new ResponseResult();
		
		vtconnectionOption.setUserId(SecurityUtil.loginId());
		
		int result =adminDAO.updateVtconnectionOptionInfo(vtconnectionOption);
			
		if(result > 0 && "Y".equals(vtconnectionOption.getPollinit())){
			try {
				ConnectionFactory.getInstance().resetConnectionPool(vtconnectionOption.getVconnid());
				resultObject.setResultCode(ResultConstants.CODE_VAL.SUCCESS.intVal());
			} catch (Exception e) {
				resultObject.setResultCode(ResultConstants.CODE_VAL.ERROR.intVal());
				resultObject.setMessage(e.getMessage());
			}
		}
		return resultObject;
	}
}