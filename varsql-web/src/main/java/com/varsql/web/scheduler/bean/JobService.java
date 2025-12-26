package com.varsql.web.scheduler.bean;

import org.quartz.JobExecutionContext;

import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobVO;

public interface JobService{
	public JobResultVO doExecute(JobExecutionContext context, JobVO jsv) throws Exception;
}
