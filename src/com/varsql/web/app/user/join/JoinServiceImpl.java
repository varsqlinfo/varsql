package com.varsql.web.app.user.join;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.auth.Authority;
import com.varsql.web.common.vo.DataCommonVO;
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
public class JoinServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(JoinServiceImpl.class);
	
	@Autowired
	JoinDAO joinDAO ;
	

	public Map selectUserDetail(DataCommonVO paramMap) {
		Map json = new HashMap();
		json.put("result", joinDAO.selectUserDetail(paramMap));
		return json;
	}

	public boolean insertUserInfo(DataCommonVO paramMap) {
		String vconid = joinDAO.selectUserMaxVal();
		
		try{
			vconid=String.format("%07d", Integer.parseInt(vconid)+1);
		}catch(Exception e){
			vconid=String.format("%07d", 1);
		}
		paramMap.put("viewid", vconid);
		paramMap.put("role", Authority.GUEST.name());
		paramMap.put("description", "");
		paramMap.put("accept_yn", "N");
		paramMap.put("cre_id", "join");
		
		return joinDAO.insertUserInfo(paramMap) > 0;
	}

	public boolean updateUserInfo(DataCommonVO paramMap) {
		return joinDAO.updateUserInfo(paramMap) > 0;
	}

	public Map selectIdCheck(DataCommonVO paramMap) {
		Map json = new HashMap();
		json.put("result", joinDAO.selectIdCheck(paramMap));
		return json;
	}

}