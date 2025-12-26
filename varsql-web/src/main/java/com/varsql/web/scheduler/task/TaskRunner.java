package com.varsql.web.scheduler.task;

import com.varsql.core.task.transfer.TaskResult;
import com.varsql.web.dto.task.TaskExecutionVO;

public interface TaskRunner {
	
	public TaskResult run(TaskExecutionVO vo);
}
