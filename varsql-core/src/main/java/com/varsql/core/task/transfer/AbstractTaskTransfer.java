package com.varsql.core.task.transfer;

import com.varsql.core.task.Task;

public abstract class AbstractTaskTransfer implements Task {
	
	protected TaskTransferBuilder taskTransferBuilder; 
	
	public AbstractTaskTransfer(TaskTransferBuilder taskTransferBuilder) {
		this.taskTransferBuilder = taskTransferBuilder;
	}
}
