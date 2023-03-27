package com.varsql.web.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SqlExecuteJobVO {

	private String conuid;

	private String schema;
	
	private String sql;
	
	private Map parameter;
	
}
