package com.varsql.core.task;

import com.varsql.core.task.transfer.TaskResult;

public interface Task {
	public void submit()throws Exception;
	
	public TaskResult result();
}
