package com.varsql.web.dto.task;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * task request dto 
 * 
 * @author ytkim
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class TaskResponseDTO {
	private String taskId; 
	private String taskName; 
	private String taskType; 
	private boolean useYn; 
	private String description; 
	
	private LocalDateTime regDt;
	
	public TaskResponseDTO (String taskId, String taskName, String taskType, Boolean useYn, String description) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskType = taskType;
		this.useYn = useYn;
		this.description = description;
	}
}