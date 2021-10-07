package com.varsql.web.app.component;

import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBTypeDriverEntity;
import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionInfoDTO {
	private DBConnectionEntity connection;
	private DBTypeDriverProviderEntity provider;
	private DBTypeDriverEntity driver;
	
	public ConnectionInfoDTO(DBConnectionEntity connection, DBTypeDriverProviderEntity provider, DBTypeDriverEntity driver) {
		this.connection = connection;
		this.provider = provider;
		this.driver = driver;
	}
}
