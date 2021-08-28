package com.varsql.web.dto.user;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: NoteResponseDTO.java
* @DESC		: 쪽지 정보.
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
public class NoteResponseDTO{
	private String noteId;

	private String noteTitle;

	private String noteCont;

	private String parentNoteId;

	private String regUserInfo;

	private LocalDateTime regDt;

	private List<String> recvUsers;

}