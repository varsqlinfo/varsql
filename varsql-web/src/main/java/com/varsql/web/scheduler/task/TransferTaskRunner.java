package com.varsql.web.scheduler.task;

import org.springframework.batch.core.BatchStatus;
import org.springframework.stereotype.Component;

import com.varsql.core.common.constants.ExecuteType;
import com.varsql.core.connection.ConnectionInfoManager;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.NotFoundException;
import com.varsql.core.task.Task;
import com.varsql.core.task.transfer.SourceVO;
import com.varsql.core.task.transfer.TargetVO;
import com.varsql.core.task.transfer.TaskResult;
import com.varsql.core.task.transfer.TaskTransferBuilder;
import com.varsql.core.task.transfer.TaskTransferExecutor;
import com.varsql.web.common.service.CommonLogService;
import com.varsql.web.dto.task.TaskExecutionVO;
import com.varsql.web.dto.websocket.MessageDTO;
import com.varsql.web.model.entity.execution.ExecutionHistoryEntity;
import com.varsql.web.model.entity.task.TaskTransferEntity;
import com.varsql.web.repository.task.TaskTransferRepository;
import com.varsql.web.util.ConvertUtils;
import com.vartech.common.utils.CommUtils;
import com.vartech.common.utils.VartechUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferTaskRunner implements TaskRunner{
	
	private final TaskTransferRepository taskTransferRepository;
	
	private final CommonLogService commonLogService;
	
	public TaskResult run(TaskExecutionVO vo) throws Exception {
		log.info("task execution vo {}", vo);
		String taskId = vo.getTaskId();
		
		long startTime = System.currentTimeMillis();
		
		BatchStatus status = BatchStatus.COMPLETED;
		
		TaskResult result=null;
		String message = "";
		MessageDTO messageDTO = null;
		try {
			
			TaskTransferEntity item = taskTransferRepository.findByTaskId(taskId);
			
			if(item == null) {
				throw new NotFoundException("task id not found : "+ taskId);
			}
			
			messageDTO = MessageDTO.builder()
					.title(item.getTaskName())
					.recvIds(new String[] {item.getRegId()})
					.build();
			
			
			SourceVO sourceVo = VartechUtils.jsonStringToObject(item.getSourceConfig(), SourceVO.class, true); 
			TargetVO targetVo = VartechUtils.jsonStringToObject(item.getTargetConfig(), TargetVO.class, true);
			
			sourceVo.setRequid(taskId);
			sourceVo.setTypeInfo(DatabaseInfo.toDatabaseInfo(ConnectionInfoManager.getInstance().getConnectionInfo(item.getSourceVconnid())));
			sourceVo.setProgressInfo(vo.getProgressInfo());
			
			targetVo.setRequid(taskId);
			targetVo.setTypeInfo(DatabaseInfo.toDatabaseInfo(ConnectionInfoManager.getInstance().getConnectionInfo(item.getTargetVconnid())));
			
			TaskResult taskResult = TaskResult.builder().build();
			
			Task taskTransferExecutor = new TaskTransferExecutor(TaskTransferBuilder.builder()
					.sourceVo(sourceVo)
					.targetVo(targetVo)
					.taskResult(taskResult)
				.build());
					
			taskTransferExecutor.submit();
			
			result= taskTransferExecutor.result();
			message = result.getErrorMessage();
		} catch (Exception e) {
			status = BatchStatus.FAILED;
			log.error(e.getMessage(), e);
			message = e.getMessage();
			
			result = TaskResult.builder()
					.errorMessage(e.getMessage())
					.build();
			throw e;
		}finally {
			long endTime = System.currentTimeMillis();
			commonLogService.saveExecutionHistory(
				messageDTO,
				ExecutionHistoryEntity.builder()
				.instanceId(CommUtils.getHostname())
				.targetType(ExecuteType.TASK)
				.targetId(taskId)
				.runType(vo.getRunType().name())
				.status(status.name())
				.customInfo(VartechUtils.objectToJsonString(result))
				.startTime(ConvertUtils.longToTimestamp(startTime))
				.endTime(ConvertUtils.longToTimestamp(endTime))
				.message(message)
				.build()
			);
		}
		
		return result;

	}
}
