package com.varsql.web.app.manager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.user.GlossaryRequestDTO;
import com.varsql.web.model.entity.user.GlossaryEntity;
import com.varsql.web.repository.spec.GlossarySpec;
import com.varsql.web.repository.user.GlossaryEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;


/**
 * -----------------------------------------------------------------------------
* @fileName		: GlossaryServiceImpl.java
* @desc		: 용어집 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class GlossaryServiceImpl{
	
	@Autowired
	private GlossaryEntityRepository glossaryEntityRepository;
	/**
	 * 
	 * @Method Name  : selectUserList
	 * @Method 설명 : 사용자 목록 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 12. 1. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectGlossaryList(SearchParameter searchParameter) {
		
		Page<GlossaryEntity> result = glossaryEntityRepository.findAll(
			GlossarySpec.searchField(searchParameter)
			, VarsqlUtils.convertSearchInfoToPage(searchParameter)
		);

		return VarsqlUtils.getResponseResult(result, searchParameter);
	}

	/**
	 * 
	 * @Method Name  : saveGlossaryInfo
	 * @Method 설명 : 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult saveGlossaryInfo(GlossaryRequestDTO glossaryInfo) {
		
		GlossaryEntity entity = glossaryInfo.toModel();
		
		entity = glossaryEntityRepository.save(entity);
		
		return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	/**
	 * 
	 * @Method Name  : deleteGlossaryInfo
	 * @Method 설명 : 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param parameter
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult deleteGlossaryInfo(String wordIdx) {
		glossaryEntityRepository.deleteByWordIdx(Long.valueOf(wordIdx));
		return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	
}