package com.varsql.web.app.user.join;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.auth.Authority;
import com.varsql.web.app.user.beans.UserForm;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.util.VarsqlUtil;
import com.vartech.common.app.beans.ResponseResult;

/**
 * 
 * @FileName  : AdminServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class JoinServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(JoinServiceImpl.class);
	
	@Autowired
	JoinDAO joinDAO ;
	

	public Map selectUserDetail(DataCommonVO paramMap) {
		Map json = new HashMap();
		json.put("result", joinDAO.selectUserDetail(paramMap));
		return json;
	}

	public boolean insertUserInfo(UserForm userForm) {
		String viewId = joinDAO.selectUserMaxVal();
		
		try{
			viewId=String.format("%07d", Integer.parseInt(viewId)+1);
		}catch(Exception e){
			viewId=String.format("%07d", 1);
		}
		
		userForm.setViewid(viewId);
		userForm.setRole(Authority.GUEST.name());
		userForm.setAcceptYn("N");
		userForm.setCreId("join");
		
		return joinDAO.insertUserInfo(userForm) > 0;
	}

	public boolean updateUserInfo(DataCommonVO paramMap) {
		return joinDAO.updateUserInfo(paramMap) > 0;
	}
	
	/**
	 * 
	 * @Method Name  : selectIdCheck
	 * @Method 설명 : id check
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 28. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectIdCheck(String uid) {
		
		ResponseResult resultObject = new ResponseResult();
		resultObject.setItemOne(joinDAO.selectIdCheck(uid));
		
		return resultObject;
	}

}