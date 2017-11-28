package com.varsql.web.app.user;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.constants.ResultConstants;
import com.vartech.common.app.beans.ParamMap;

@Service
public class UserMainServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(UserMainServiceImpl.class);
	@Autowired
	UserMainDAO userMainDAO;

	/**
	 * 사용자 검색.
	 * @param paramMap
	 * @return
	 */
	public Map selectSearchUserList(ParamMap paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT_ITEMS, userMainDAO.selectSearchUserList(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"selectSearchUserList", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}
	
	/**
	 * sql 보내기
	 * @param paramMap
	 * @return
	 */
	public Map insertSendSqlInfo(ParamMap paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT, userMainDAO.insertSendSqlInfo(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
			
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"insertSendSqlInfo", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}

	public Map selectMessageInfo(ParamMap paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT_ITEMS, userMainDAO.selectMessageInfo(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
			
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"selectMessageInfo", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}

	public Map updateMemoViewDate(ParamMap paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT, userMainDAO.updateMemoViewDate(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"updateMemoViewDate", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval;
	}
}