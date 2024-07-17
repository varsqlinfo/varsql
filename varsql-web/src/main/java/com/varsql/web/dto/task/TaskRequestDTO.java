package com.varsql.web.dto.task;
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
public class TaskRequestDTO {
	private String taskId; 
	private String taskName; 
	private String taskType; 
	private String useYn; 
	private String description; 
}