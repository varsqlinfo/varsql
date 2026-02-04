package com.varsql.web.dto.task;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
	
	@Size(max=36)
	private String taskId; 
	
	@NotEmpty
	@Size(max=255)
	private String taskName; 
	private String taskType; 
	private String useYn; 
	private String description; 
}