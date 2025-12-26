package com.varsql.web.scheduler.job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.task.transfer.TaskResult;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobVO;
import com.varsql.web.dto.task.TaskExecutionVO;
import com.varsql.web.scheduler.JobType;
import com.varsql.web.scheduler.bean.JobBean;
import com.varsql.web.scheduler.task.SqlTaskRunner;
import com.vartech.common.utils.VartechUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskExecuteJob extends JobBean {
	private final Logger logger = LoggerFactory.getLogger(TaskExecuteJob.class);
	
	final private SqlTaskRunner sqlTaskService; 

	@Override
	public JobResultVO doExecute(JobExecutionContext context, JobVO jsv) throws Exception {
		logger.debug("## task execute job start : {}", jsv);
		
		TaskExecutionVO taskExecutionVO = VartechUtils.jsonStringToObject(jsv.getJobData(), TaskExecutionVO.class);
		
		TaskResult result;
		try {
			result = sqlTaskService.run(taskExecutionVO);
		} catch (Throwable e ) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER ,"task execute error : ["+ taskExecutionVO.getTaskId() + "] message : "+ e.getMessage(), e);
		}
		
		return JobResultVO.builder()
				.jobType(JobType.SQL)
				.log(result.getErrorMessage())
				.build(); 
	}
}