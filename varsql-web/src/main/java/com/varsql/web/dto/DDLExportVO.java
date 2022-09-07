package com.varsql.web.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * export ddl info
* 
* @fileName	: DDLExportVO.java
* @author	: ytkim
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DDLExportVO {

	private String schema;
	
	private String charset;
	
	private List<DDLExportItemVO> exportItems;
	
	
}
