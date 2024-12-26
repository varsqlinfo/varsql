package com.varsql.web.dto.db;

import com.varsql.web.model.entity.db.DBTypeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DBTypeDTO {

	private String typeid;

	private String name;

	private String langkey;

	private String urlprefix;
	
	public static DBTypeDTO toDto(DBTypeEntity e) {
		DBTypeDTO dto = new DBTypeDTO();
		
		dto.setTypeid(e.getTypeid());
		dto.setName(e.getName());
		dto.setLangkey(e.getLangkey());
		dto.setUrlprefix(e.getUrlprefix());
		
		return dto; 
	}

}