package com.varsql.web.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.varsql.web.app.scheduler.JobType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResultVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private JobType jobType;
	
	private long resultCount;
	
	private long failCount; 
	
	private String message;
	
	private Map customInfo;
	
	private String log;
	
	public JobResultVO addCustomInfo(Object key, Object val) {
		if(customInfo==null) {
			customInfo = new HashMap(); 
		}
		
		customInfo.put(key, val);
		
		return this;
	}
}
