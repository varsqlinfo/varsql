package com.varsql.web.dto.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.varsql.web.model.entity.user.GlossaryEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * -----------------------------------------------------------------------------
* @fileName		: GlossaryRequestDTO.java
* @desc		: 용어 dto 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 27. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Getter
@Setter
@NoArgsConstructor
public class GlossaryRequestDTO{
	
	private Long wordIdx;
	
	@NotEmpty
	@Size(max=500)
	private String word;
	
	@Size(max=500)
	private String wordEn;
	
	@Size(max=500)
	private String wordAbbr;
	
	@Size(max=2000)
	private String wordDesc;

	public GlossaryEntity toEntity() {
		return GlossaryEntity.builder()
				.wordIdx(wordIdx)
				.word(word)
				.wordEn(wordEn)
				.wordAbbr(wordAbbr)
				.wordDesc(wordDesc).build();
	}
}