package com.varsql.web.app.manager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.user.GlossaryRequestDTO;
import com.varsql.web.model.entity.app.GlossaryEntity;
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
	 * @method  : selectGlossaryList
	 * @desc : 용어집 보기.
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
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
	 * @method  : saveGlossaryInfo
	 * @desc : 저장
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param glossaryInfo
	 * @return
	 */
	public ResponseResult saveGlossaryInfo(GlossaryRequestDTO glossaryInfo) {
		
		GlossaryEntity entity = glossaryInfo.toEntity();
		
		entity = glossaryEntityRepository.save(entity);
		
		return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	/**
	 * @method  : deleteGlossaryInfo
	 * @desc : 삭제
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param wordIdx
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult deleteGlossaryInfo(String wordIdx) {
		glossaryEntityRepository.deleteByWordIdx(Long.valueOf(wordIdx));
		return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	
}