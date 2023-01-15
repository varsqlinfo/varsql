package com.varsql.web.dto.scheduler;

import java.io.Serializable;

import com.varsql.web.model.entity.scheduler.ScheduleHistoryEntity;
import com.varsql.web.model.entity.scheduler.ScheduleHistoryLogEntity;
import com.varsql.web.model.mapper.scheduler.ScheduleHistoryMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleHistoryResponseDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long histSeq; 
	private String instanceId; 
	private String jobUid; 
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