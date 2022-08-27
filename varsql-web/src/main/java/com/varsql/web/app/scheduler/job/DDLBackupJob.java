package com.varsql.web.app.scheduler.job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.varsql.web.app.scheduler.bean.VarsqlJobBean;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobScheduleVO;

@Component
public class DDLBackupJob extends VarsqlJobBean {
	
	private final Logger logger = LoggerFactory.getLogger(DDLBackupJob.class);

	@Override
	public JobResultVO doExecute(JobExecutionContext context, JobScheduleVO jsv) throws Exception{
		logger.debug("## ddl backup start ## ");
		context.setResult("DataBackupJob test ");
		logger.debug("## ddl backup  ## ");
		
		return JobResultVO.builder().build(); 
	}
}