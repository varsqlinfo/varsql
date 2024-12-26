package com.varsql.web.dto.db;

import com.varsql.web.model.entity.db.DBTypeDriverEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DBTypeDriverDTO {

	private String driverId;

	private String dbtype;

	private String dbdriver;

	private String validationQuery;

	private String urlFormat;

	private int defaultPort;

	private String schemaType;

	private String driverDesc;
	
	public static DBTypeDriverDTO toDto(DBTypeDriverEntity e) {
		DBTypeDriverDTO dto = new DBTypeDriverDTO();
		
		dto.setDriverId(e.getDriverId());
		dto.setDbtype(e.getDbtype());
		dto.setDbdriver(e.getDbdriver());
		dto.setValidationQuery(e.getValidationQuery());
		dto.setUrlFormat(e.getUrlFormat());
		dto.setDefaultPort(e.getDefaultPort());
		dto.setSchemaType(e.getSchemaType());
		dto.setDriverDesc(e.getDriverDesc());
		
		return dto; 
	}

}