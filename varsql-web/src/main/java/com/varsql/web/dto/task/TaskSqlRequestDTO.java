package com.varsql.web.dto.task;
import com.varsql.web.model.entity.task.TaskSqlEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskSqlRequestDTO extends TaskRequestDTO{
	private String vconnid; 
	private String sql; 
	private String parameter; 

	public TaskSqlEntity toEntity() {
		return TaskSqlEntity.builder()
			.taskName(getTaskName())
			.taskId(getTaskId())
			.useYn(!"N".equals(getUseYn()))
			.description(getDescription())
			
			.vconnid(vconnid)
			.sql(sql)
			.parameter(parameter)
			.build();
	}
}