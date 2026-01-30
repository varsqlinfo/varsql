package com.varsql.web.scheduler.job;
import java.util.LinkedList;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.ExecuteType;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.task.transfer.TaskResult;
import com.varsql.web.constants.TaskType;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobVO;
import com.varsql.web.dto.scheduler.TaskJobVO;
import com.varsql.web.dto.task.TaskExecutionVO;
import com.varsql.web.model.entity.task.TaskEntity;
import com.varsql.web.repository.task.TaskRepository;
import com.varsql.web.scheduler.JobType;
import com.varsql.web.scheduler.bean.JobBean;
import com.varsql.web.scheduler.task.SqlTaskRunner;
import com.varsql.web.scheduler.task.TaskRunner;
import com.varsql.web.scheduler.task.TransferTaskRunner;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

@Component
public class TaskExecuteJob extends JobBean {
	private final Logger logger = LoggerFactory.getLogger(TaskExecuteJob.class);
	
	@Autowired
	private SqlTaskRunner sqlTaskService;
	
	@Autowired
	private TransferTaskRunner transferTaskRunner; 
	
	@Autowired
	private TaskRepository taskRepository; 

	@Override
	public JobResultVO doExecute(JobExecutionContext context, JobVO jsv) throws Exception {
		logger.debug("## task execute job start : {}", jsv);
		
		TaskJobVO taskJobVO = VartechUtils.jsonStringToObject(jsv.getJobData(), TaskJobVO.class, true);
		
		LinkedList<TaskExecutionVO> taskExecutionList= taskJobVO.getMappedTaskList();
		
		TaskResult result= null;
		
		StringBuffer errorMessage = new StringBuffer();
		int totalCount = taskExecutionList.size();
		
		int executeCount = 0; 
		
		for(TaskExecutionVO taskExecutionVO : taskExecutionList) {
			
			taskExecutionVO.setRunType(ExecuteType.BATCH);
			
			executeCount++;
			TaskEntity taskEntity=taskRepository.findByTaskId(taskExecutionVO.getTaskId());
			
			if(taskEntity != null) {
				TaskRunner taskRunner = null;
				if(TaskType.TRANSFER.equals(TaskType.fromString(taskEntity.getTaskType()))){
					taskRunner = transferTaskRunner;
				}else if(TaskType.SQL.equals(TaskType.fromString(taskEntity.getTaskType()))) {
					taskRunner = sqlTaskService;
				}
				
				try {
					result =  taskRunner.run(taskExecutionVO);
					if(!StringUtils.isBlank(result.getErrorMessage())) {
						errorMessage.append(result.getErrorMessage());
					}
				} catch (Throwable e ) {
					errorMessage.append(e.getMessage());
					
					if(!taskJobVO.isContinueOnError()) {
						throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER ,"task execute error : ["+ taskExecutionVO.getTaskId() + "] message : "+ e.getMessage(), e);
					}else {
						logger.error("task execute error : ["+ taskExecutionVO.getTaskId() + "] message : "+ e.getMessage(), e);
					}
				}
			}else {
				if(!taskJobVO.isContinueOnError()) {
					throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER ,"task execute info null, task id: "+ taskExecutionVO.getTaskId());
				}else {
					logger.error("task execute info null, task id: {}", taskExecutionVO.getTaskId());
				}
			}
		}
		
		return JobResultVO.builder()
				.jobType(JobType.TASK)
				.message(errorMessage.toString())
				.build()
				.addCustomInfo("task info ", String.format("task execute/total count : %d/%d", executeCount, totalCount)); 
	}
}