package com.varsql.web.dto.scheduler;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobScheduleDetailDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String jobUid; 

	private String jobName; 
	
	private String vconnid;
	
	private String vname;
	
	private String jobData;
	
	private String jobDescription;
	
	private String cronExpression; 
	
	private String regId; 
	
	private LocalDateTime regDt; 
	
	private List<TriggerResponseDTO> triggerList= new ArrayList<>(); 

}