package com.varsql.core.task.transfer;

import java.util.Map;

import com.varsql.core.common.job.AbstractJobHandler;
import com.varsql.core.common.job.JobStatus;
import com.varsql.core.task.transfer.reader.AbstractSourceReader;
import com.varsql.core.task.transfer.writer.AbstractTargetWriter;

public class TaskTransferExecutor extends AbstractTaskTransfer {
	
	public TaskTransferExecutor(TaskTransferBuilder taskTransferBuilder) {
		super(taskTransferBuilder);
	}

	@Override
	public void submit() throws Exception {
		
		SourceVO sourceVO = taskTransferBuilder.getSourceVo();
		TargetVO targetVO = taskTransferBuilder.getTargetVo();
		
		final TaskResult taskResult = taskTransferBuilder.getTaskResult()==null ? TaskResult.builder().build() : taskTransferBuilder.getTaskResult();
		taskTransferBuilder.setTaskResult(taskResult);
		
		if(sourceVO.getProgressInfo() !=null) {
			sourceVO.getProgressInfo().setCustomInfo(taskResult);
		};
		
		AbstractTargetWriter targetWriter = targetVO.getWriteType().getTargetWriter(targetVO);		
		
		final AbstractJobHandler handler = new AbstractJobHandler() {
			
			@Override
			public JobStatus handler(Object obj) throws Exception {
				targetWriter.update((Map)obj);
				
				taskResult.setReadIdx(getIndex());
				taskResult.setReadFail(getFail());
				taskResult.setReadTotal(getTotal());
				
				taskResult.setWriteTotal(targetWriter.getTotalCount());
				taskResult.setWriteSuccess(targetWriter.getSuccessCount());
				taskResult.setWriteFail(targetWriter.getFailCount());
				taskResult.setWriteUpdate(targetWriter.getUpdateCount());
				taskResult.setWriteInsert(targetWriter.getInsertCount());
				
				return JobStatus.SUCCESS;
			}
		};
		
		try(
				AbstractSourceReader sourceReader = sourceVO.getSourceType().getSourceReader(sourceVO, handler);
		){
			sourceReader.read();

			taskResult.setReadFail(handler.getFail());
			taskResult.setReadTotal(handler.getTotal());
			taskResult.setWriteTotal(targetWriter.getTotalCount());
			taskResult.setWriteSuccess(targetWriter.getSuccessCount());
			taskResult.setWriteFail(targetWriter.getFailCount());
			taskResult.setWriteUpdate(targetWriter.getUpdateCount());
			taskResult.setWriteInsert(targetWriter.getInsertCount());
			
			targetWriter.close();
			sourceReader.close();
			
		}finally {
			targetWriter.close();
		}
	}

	@Override
	public TaskResult result() {
		return taskTransferBuilder.getTaskResult();
	}
}
