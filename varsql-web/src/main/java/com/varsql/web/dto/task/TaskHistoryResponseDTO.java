package com.varsql.web.dto.task;
import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * task history dto
 */
@Getter
@Setter
@NoArgsConstructor
public class TaskHistoryResponseDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long histSeq; 
	private String instanceId; 
	private String taskId; 
	private String runType; 
	private String startTime; 
	private String endTime; 
	private long runTime; 
	private long resultCount; 
	private long failCount; 
	private String message; 
	private String status; 
	private String customInfo; 

}