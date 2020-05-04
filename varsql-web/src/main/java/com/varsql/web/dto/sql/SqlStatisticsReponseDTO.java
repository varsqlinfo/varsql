package com.varsql.web.dto.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SqlStatisticsReponseDTO {
	private String viewdt;
	private long cnt;
	
	public SqlStatisticsReponseDTO(String viewdt , long cnt) {
		this.viewdt = viewdt; 
		this.cnt = cnt; 
	}
}

