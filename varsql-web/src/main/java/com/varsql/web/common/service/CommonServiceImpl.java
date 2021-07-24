package com.varsql.web.common.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.core.common.util.CommUtil;
import com.varsql.web.model.entity.app.ExceptionLogEntity;
import com.varsql.web.repository.sql.SqlExceptionLogEntityRepository;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: CommonServiceImpl.java
* @DESC		: 공통 서비스
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class CommonServiceImpl{
	private final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Autowired
	private SqlExceptionLogEntityRepository sqlExceptionLogEntityRepository;

	/**
	 *
	 * @Method Name  : insertExceptionLog
	 * @Method 설명 : error insert
	 * @작성자   : ytkim
	 * @작성일   : 2019. 4. 16.
	 * @변경이력  :
	 * @param exceptionType
	 * @param e
	 */
	public void insertExceptionLog(String exceptionType, Throwable e) {
		try{
			String exceptionTitle = e.getMessage();

			sqlExceptionLogEntityRepository.save(ExceptionLogEntity.builder()
					.excpType(exceptionType)
					.excpCont(CommUtil.getExceptionStr(e).substring(0 , 2000))
					.excpTitle(exceptionTitle.length() > 1500 ?exceptionTitle.substring(0,1500) :  exceptionTitle)
					.serverId(CommUtil.getHostname()).build());
		}catch(Throwable e1) {
			logger.error("insertExceptionLog Cause : "+ e1.getMessage() ,e1);
		}
	}

}