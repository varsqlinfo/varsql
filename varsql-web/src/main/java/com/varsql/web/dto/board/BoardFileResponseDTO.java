package com.varsql.web.dto.board;

import com.varsql.web.model.entity.board.BoardFileEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardFileResponseDTO {

	private long fileId; 

	private String fileName;
	
	private long fileContId;
	
	private String fileFieldName; 
	
	private long fileSize;
	
	private String displaySize;
	
	public static BoardFileResponseDTO toDto(BoardFileEntity bfe) {
		BoardFileResponseDTO dto = new BoardFileResponseDTO();
		
		dto.setFileId(bfe.getFileId());
		dto.setFileName(bfe.getFileName());
		dto.setFileContId(bfe.getContId());
		dto.setFileFieldName(bfe.getFileFieldName());
		dto.setFileSize(bfe.getFileSize());
		dto.setDisplaySize(bfe.getDisplaySize());
		
		return dto; 
	}

}