package com.varsql.web.dto.task;
import javax.validation.constraints.NotEmpty;

import com.varsql.web.model.entity.task.TaskTransferEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * task 
 * @author ytkim
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class TaskTransferRequestDTO extends TaskRequestDTO{
	@NotEmpty
	private String transferType; 

	private int commitCount; 
	
	@NotEmpty
	private String sourceVconnid;
	
	private String sourceConfig; 

	@NotEmpty
	private String targetVconnid;
	
	private String targetConfig; 
	
	public TaskTransferEntity toEntity() {
		return TaskTransferEntity.builder()
			.taskName(getTaskName())
			.taskId(getTaskId())
			.useYn(!"N".equals(getUseYn()))
			.description(getDescription())
			
			.transferType(transferType)
			.commitCount(commitCount)
			.sourceVconnid(sourceVconnid)
			.sourceConfig(sourceConfig)
			.targetVconnid(targetVconnid)
			.targetConfig(targetConfig)
			.build();
	}
}