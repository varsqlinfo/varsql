package com.varsql.web.dto.task;
import java.io.Serializable;
import java.util.Map;

import com.varsql.core.common.beans.ProgressInfo;
import com.varsql.core.common.constants.ExecuteType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TaskExecutionVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String taskId; 
	private String taskName; 
	private String requid; 
	private ExecuteType runType; 
	private ProgressInfo progressInfo; 
	private Map params;
	
	@Builder
	public TaskExecutionVO(String taskId, String taskName, String requid, ExecuteType runType, Map params, ProgressInfo progressInfo){
		this.taskId = taskId;
		this.requid = requid;
		this.runType = runType;
		this.progressInfo = progressInfo;
		this.params = params;
	}
}