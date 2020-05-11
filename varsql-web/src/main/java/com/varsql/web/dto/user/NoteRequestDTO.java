package com.varsql.web.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.varsql.web.model.entity.app.NoteEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: MemoInfo.java
* @DESC		: 메모 정보. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 5. 2. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */

@Getter
@Setter
@NoArgsConstructor
public class NoteRequestDTO{
	private String noteId;
	
	@NotEmpty
	@Size(max=1000)
	private String noteTitle;
	
	private String noteCont;
	
	private String parentNoteId;
	
	private String reNoteCont;

	private boolean delYn;
	
	@NotEmpty
	private String recvId;
	
	
	public NoteEntity toEntity() {
		return NoteEntity.builder()
				.noteId(noteId)
				.noteTitle(noteTitle)
				.noteCont(noteCont)
				.parentNoteId(parentNoteId)
				.delYn(delYn)
				.build();
	}

}