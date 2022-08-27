package com.varsql.web.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobResultVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long resultCount;
	
	private long failCount; 
	
	private String message;
	
	@Builder
	public JobResultVO(long resultCount, long failCount,  String message) {
		this.resultCount = resultCount;
		this.failCount= failCount; 
		this.message= message;
	}
}
