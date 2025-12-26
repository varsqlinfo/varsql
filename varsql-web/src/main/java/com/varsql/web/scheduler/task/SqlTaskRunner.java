package com.varsql.web.scheduler.task;

import org.springframework.batch.core.BatchStatus;
import org.springframework.stereotype.Component;

import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.exception.NotFoundException;
import com.varsql.core.sql.util.SQLUtils;
import com.varsql.core.task.Task;
import com.varsql.core.task.sql.SQLTask;
import com.varsql.core.task.transfer.TaskResult;
import com.varsql.web.common.service.CommonLogService;
import com.varsql.web.dto.task.TaskExecutionVO;
import com.varsql.web.exception.DatabaseNotFoundException;
import com.varsql.web.model.entity.task.TaskHistoryEntity;
import com.varsql.web.model.entity.task.TaskSqlEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.task.TaskSqlRepository;
import com.varsql.web.util.ConvertUtils;
import com.vartech.common.utils.CommUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * sql task runner
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SqlTaskRunner implements TaskRunner{
	
	final private TaskSqlRepository taskSqlRepository;
	
	final private DBConnectionEntityRepository dbConnectionEntityRepository;
	
	final private CommonLogService commonLogService;
	
	public TaskResult run(TaskExecutionVO vo) {
		log.info("task execution vo {}", vo);
		
		String taskId = vo.getTaskId();
		
		long startTime = System.currentTimeMillis();
		
		BatchStatus status = BatchStatus.COMPLETED;
		
		TaskResult result;
		String message = "";
		try {
			
			TaskSqlEntity item = taskSqlRepository.findByTaskId(taskId);
			
			if(item == null) {
				throw new NotFoundException(String.format("[%s] task not found ",  taskId));
			}
			
			DatabaseInfo dbinfo = dbConnectionEntityRepository.findDatabaseInfo(item.getVconnid());
			
			if(dbinfo==null) {
				throw new DatabaseNotFoundException(String.format("[%s] db not found ", item.getVconnid()));
			}
			
			SqlStatementInfo ssi = new SqlStatementInfo();
			
			ssi.setRequid$$(vo.getRequid());
			ssi.setDatabaseInfo(dbinfo);
			ssi.setSql(item.getSql());
			ssi.setSqlParamMap(SQLUtils.stringParamListToMap(item.getParameter()));
			
			Task sqlTask = new SQLTask(ssi);
			
			try {
				sqlTask.submit();
				
				result= sqlTask.result();
				message = result.getErrorMessage();
			} catch (Exception e) {
				status = BatchStatus.FAILED;
				log.error(e.getMessage(), e);
				message = e.getMessage();
				
				result = TaskResult.builder()
						.errorMessage(e.getMessage())
						.build();
			}
		
		}finally {
			long endTime = System.currentTimeMillis();
			commonLogService.saveTaskHistory(
				TaskHistoryEntity.builder()
				.instanceId(CommUtils.getHostname())
				.taskId(taskId)
				.runType(vo.getRunType().name())
				.status(status.name())
				.startTime(ConvertUtils.longToTimestamp(startTime))
				.endTime(ConvertUtils.longToTimestamp(endTime))
				.message(message)
				.build()
			);
		}

		return result;
	}
}
