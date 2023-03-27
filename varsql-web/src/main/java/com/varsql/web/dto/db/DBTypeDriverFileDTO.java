package com.varsql.web.dto.db;

import com.varsql.web.model.entity.db.DBTypeDriverFileEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DBTypeDriverFileDTO {

	private String fileId; 

	private String fileName;
	
	private String fileContId;
	
	private String fileFieldName; 
	
	private long fileSize;
	
	private String displaySize;
	
	public static DBTypeDriverFileDTO toDto(DBTypeDriverFileEntity e) {
		DBTypeDriverFileDTO dto = new DBTypeDriverFileDTO();
		
		dto.setFileId(e.getFileId());
		dto.setFileName(e.getFileName());
		dto.setFileContId(e.getFileContId());
		dto.setFileSize(e.getFileSize());
		dto.setDisplaySize(e.getDisplaySize());
		
		return dto; 
	}

}