package com.varsql.web.dto.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.user.QnAEntity;
import com.varsql.web.util.ValidateUtils;

import lombok.Getter;
import lombok.Setter;


/**
 * -----------------------------------------------------------------------------
* @fileName		: QnARequesetDTO.java
* @desc		: qna request dto 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 27. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Getter
@Setter
public class QnARequesetDTO {
	
	@Size(max=34)
	private String qnaid;
	
	@NotEmpty
	@Size(max=250)
	private String title;
	
	@NotEmpty
	@Size(max=2000)
	private String question;

	@Size(max=2000)
	private String answer;

	public QnAEntity toEntity() {
		return QnAEntity.builder()
				.qnaid(qnaid)
				.title(title)
				.question(question)
				.delYn("N").build();
	}
}
