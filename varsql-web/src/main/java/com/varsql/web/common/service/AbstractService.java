package com.varsql.web.common.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.web.model.converter.DomainMapper;

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
public class AbstractService{
	@Autowired
	protected DomainMapper domainMapper;

}