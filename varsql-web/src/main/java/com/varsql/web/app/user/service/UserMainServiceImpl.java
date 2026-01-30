package com.varsql.web.app.user.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.varsql.web.common.service.AbstractService;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.model.mapper.user.UserMapper;
import com.varsql.web.repository.spec.UserSpec;
import com.varsql.web.repository.user.UserInfoRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMainServiceImpl extends AbstractService{
	private final Logger logger = LoggerFactory.getLogger(UserMainServiceImpl.class);

	private final UserInfoRepository userInfoRepository;

	/**
	 *
	 * @Method Name  : selectSearchUserList
	 * @Method 설명 : 사용자 검색.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectSearchUserList(SearchParameter searchParameter) {
		List<UserEntity> result = userInfoRepository.findAll(
			UserSpec.findUser(searchParameter.getKeyword())
		);

		return VarsqlUtils.getResponseResult(result, UserMapper.INSTANCE);
	}

}