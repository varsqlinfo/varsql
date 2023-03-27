package com.varsql.web.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DDLExportItemVO {

	private String name;
	
	private String contentid;
	
	private boolean allObjectExport;
	
	private List<String> objectNameList;
	
	@Builder
	public DDLExportItemVO(String name, String contentid, boolean allObjectExport, List<String> objectNameList) {
		this.name = name;
		this.contentid = contentid;
		this.allObjectExport = allObjectExport;
		this.objectNameList = objectNameList;
	}
}
