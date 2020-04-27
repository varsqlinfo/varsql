package com.varsql.web.app.user.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.db.encryption.EncryptionFactory;
import com.varsql.web.dto.user.UserReqeustDTO;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.user.UserMgmtRepository;
import com.varsql.web.util.VarsqlUtils;
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
	private UserMgmtRepository userMgmtRepository; 

	/**
	 * @method  : saveUser
	 * @desc : 사용자 정보 등록.
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param joinForm
	 * @return
	 * @throws EncryptDecryptException
	 */
	public boolean saveUser(UserReqeustDTO joinForm) throws EncryptDecryptException {
		joinForm.setUpw(EncryptionFactory.getInstance().encrypt(joinForm.getUpw()));
		
		UserEntity entity = joinForm.toEntity();
		
		entity.setUserRole(AuthorityType.GUEST.name());
		entity.setAcceptYn("N");
		
		entity = userMgmtRepository.save(entity);

		return entity== null? false :true;
	}

	/**
	 * @method  : idCheck
	 * @desc : id check
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param uid
	 * @return
	 */
	public ResponseResult idCheck(String uid) {
		return VarsqlUtils.getResponseResultItemOne(userMgmtRepository.countByUid(uid));
	}
	
	/**
	 * @method  : emailCheck
	 * @desc : email check
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param uemail
	 * @return
	 */
	public ResponseResult emailCheck(String uemail) {
		return VarsqlUtils.getResponseResultItemOne(userMgmtRepository.countByUemail(uemail));
	}
}