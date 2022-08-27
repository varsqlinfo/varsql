package com.varsql.web.app.scheduler.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.varsql.web.dto.JobResultVO;
import com.varsql.web.model.entity.scheduler.ScheduleHistoryEntity;
import com.varsql.web.repository.scheduler.ScheduleHistoryEntityRepository;
import com.varsql.web.util.ConvertUtils;

/**
 * 
* 
* @fileName	: JobsListener.java
* @author	: ytkim
 */
@Component
public class JobsListenerImpl implements JobListener {
	private final Logger logger = LoggerFactory.getLogger(JobsListenerImpl.class);
	
	@Override
	public String getName() {
		return "varsqlJobListener";
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		//JobDetail jobDetail = context.getJobDetail(); 
		//JobKey jobKey = context.getJobDetail().getKey();
		
		//logger.debug("jobWasExecuted at jobKey: {} ,jobName: {} ,FireTime: {} ,JobRunTime: {} ,instanceid: {}", jobKey, jobKey.getName(), context.getFireTime(), context.getJobRunTime(), context.getFireInstanceId());
		
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		//JobKey jobKey = context.getJobDetail().getKey();
		//logger.debug("jobWasExecuted at jobKey: {} ,jobName: {} ,FireTime: {} ,JobRunTime: {}", jobKey, jobKey.getName(), context.getFireTime(), context.getJobRunTime());
		
	}
}
