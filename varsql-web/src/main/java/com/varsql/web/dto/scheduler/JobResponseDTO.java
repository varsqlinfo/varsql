package com.varsql.web.dto.scheduler;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String jobUid; 

	private String jobName; 
	
	// 커넥션 id
	private String vconnid;
	
	// vconnid 삭제 여부
	private boolean isDeleteVconnid;
	
	// 커넥션명
	private String vname;
	
	private String jobData;
	
	private String jobDescription;
	
	private String cronExpression; 
	
	private String regId; 
	
	private LocalDateTime regDt; 
}