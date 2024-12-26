package com.varsql.core.task.transfer;

import com.vartech.common.app.beans.ResponseResult;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskResult{
	
	private long readTotal;
	
	private long readIdx;
	
	private long readFail;
	
	private long writeTotal;
	
	private long writeFail;
	
	private long writeSuccess;
	
	private long writeUpdate;
	
	private long writeInsert;
	
	private String errorMessage; 
	
	private ResponseResult customResult; 
}
