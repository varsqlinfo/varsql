package com.varsql.web.dto.execution;

import java.io.Serializable;

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
public class ExecutionHistoryResponseDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long histSeq; 
	private String instanceId; 
	private String targetType; 
	private String targetId; 
	private String runType; 
	private String startTime; 
	private String endTime; 
	private long runTime; 
	private long resultCount; 
	private long failCount; 
	private String message; 
	private String status;
	private String log;
	
}