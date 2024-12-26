package com.varsql.web.dto.task;
import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TaskVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String taskId; 
	private String taskName; 
	private String taskType; 
	private String useYn; 
	private String description; 
	
	@Builder
	public TaskVO(String taskId, String taskName, String taskType, String useYn, 
			String description){
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskType = taskType;
		this.useYn = useYn;
		this.description = description;
	}
}