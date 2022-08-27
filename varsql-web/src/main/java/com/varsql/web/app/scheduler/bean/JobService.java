package com.varsql.web.app.scheduler.bean;

import org.quartz.JobExecutionContext;

import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobScheduleVO;

public interface JobService{
	public JobResultVO doExecute(JobExecutionContext context, JobScheduleVO jsv) throws Exception;
}
