package com.varsql.core.task.transfer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskTransferBuilder{
	
	private SourceVO sourceVo;
	
	private TargetVO targetVo;
	
	private TaskResult taskResult;
}
