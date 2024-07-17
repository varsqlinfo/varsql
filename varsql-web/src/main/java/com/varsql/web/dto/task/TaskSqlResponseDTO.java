package com.varsql.web.dto.task;
import com.varsql.web.model.entity.task.TaskSqlEntity;

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
public class TaskSqlResponseDTO extends TaskResponseDTO{
	private String vconnid; 
	private String sql; 
	private String parameter; 

	public static TaskSqlResponseDTO toDto(TaskSqlEntity entity) {
		TaskSqlResponseDTO dto =new TaskSqlResponseDTO();
		
		dto.setTaskId(entity.getTaskId());
		dto.setTaskName(entity.getTaskName());
		dto.setTaskType(entity.getTaskType());
		dto.setDescription(entity.getDescription());
		dto.setRegDt(entity.getRegDt());
		dto.setUseYn(entity.isUseYn());
		dto.setVconnid(entity.getVconnid());
		dto.setSql(entity.getSql());
		dto.setParameter(entity.getParameter());
		
		return dto;
	}
}