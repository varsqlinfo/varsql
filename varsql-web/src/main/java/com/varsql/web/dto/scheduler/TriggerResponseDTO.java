package com.varsql.web.dto.scheduler;

import com.varsql.web.model.entity.scheduler.TriggersEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TriggerResponseDTO {
	
	private String triggerName; 
	
	private String triggerGroup; 
	
	private String description; 
	
	private long nextFireTime; 
	
	private long prevFireTime; 
	
	private int priority; 
	
	private String triggerState; 
	
	private String triggerType; 
	
	private long startTime; 
	
	private long endTime; 
	
	private String calendarName; 
	
	private int misfireInstr; 
	
	@Builder
	public TriggerResponseDTO(String triggerName, String triggerGroup, String description, long nextFireTime, long prevFireTime, int priority, 
			String triggerState, String triggerType, long startTime, long endTime, String calendarName,int misfireInstr) {
		
		this.triggerName =triggerName;
		this.triggerGroup =triggerGroup;
		this.description =description;
		this.nextFireTime =nextFireTime;
		this.prevFireTime =prevFireTime;
		this.priority =priority;
		this.triggerState =triggerState;
		this.triggerType =triggerType;
		this.startTime =startTime;
		this.endTime =endTime;
		this.calendarName =calendarName;
		this.misfireInstr=misfireInstr;
	}

	public static TriggerResponseDTO toDto(TriggersEntity te) {
		return TriggerResponseDTO.builder()
				.triggerName(te.getTriggerName())
				.triggerGroup(te.getTriggerGroup())
				.description(te.getDescription())
				.nextFireTime(te.getNextFireTime())
				.prevFireTime(te.getPrevFireTime())
				.priority(te.getPriority())
				.triggerState(te.getTriggerState())
				.triggerType(te.getTriggerType())
				.startTime(te.getStartTime())
				.endTime(te.getEndTime())
				.calendarName(te.getCalendarName())
				.misfireInstr(te.getMisfireInstr())
				.build();
	}
	
	
}
