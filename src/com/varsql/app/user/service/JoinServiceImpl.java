package com.varsql.app.user.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.user.beans.JoinForm;
import com.varsql.app.user.dao.JoinDAO;
import com.varsql.core.auth.Authority;
import com.varsql.core.db.encryption.EncryptionFactory;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.encryption.EncryptDecryptException;

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
	
	/**
	 * 
	 * @Method Name  : insertUserInfo
	 * @Method 설명 : 사용자 정보 등록.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param userForm
	 * @return
	 * @throws EncryptDecryptException 
	 */
	public boolean insertUserInfo(JoinForm	joinForm) throws EncryptDecryptException {
		String viewId = joinDAO.selectUserMaxVal();
		
		try{
			viewId=String.format("%07d", Integer.parseInt(viewId)+1);
		}catch(Exception e){
			viewId=String.format("%07d", 1);
		}
		
		joinForm.setUpw(EncryptionFactory.getInstance().encrypt(joinForm.getUpw()));
		joinForm.setViewid(viewId);
		joinForm.setRole(Authority.GUEST.name());
		joinForm.setAcceptYn("N");
		joinForm.setCreId("join");
		
		return joinDAO.insertUserInfo(joinForm) > 0;
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