package com.varsql.web.dto.db;

import com.varsql.web.model.entity.app.FileInfoEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileResponseDTO {

	private String fileId; 

	private String fileName;
	
	private String fileContId;
	
	private String fileFieldName; 
	
	private long fileSize;
	
	private String displaySize;
	
	public static FileResponseDTO toDto(FileInfoEntity e) {
		FileResponseDTO dto = new FileResponseDTO();
		
		dto.setFileId(e.getFileId());
		dto.setFileName(e.getFileName());
		dto.setFileContId(e.getFileContId());
		dto.setFileFieldName(e.getFileFieldName());
		dto.setFileSize(e.getFileSize());
		dto.setDisplaySize(e.getDisplaySize());
		
		return dto; 
	}

}