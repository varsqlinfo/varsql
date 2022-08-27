package com.varsql.web.app.scheduler.bean;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.app.scheduler.JOBServiceUtils;
import com.varsql.web.common.service.CommonLogService;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobScheduleVO;
import com.varsql.web.model.entity.scheduler.ScheduleHistoryEntity;
import com.varsql.web.util.ConvertUtils;
import com.vartech.common.utils.VartechUtils;

/**
 * 
*  
* @fileName	: VarsqlJobBean.java
* @author	: ytkim
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public abstract class VarsqlJobBean extends QuartzJobBean implements JobService{
 
	private final Logger logger = LoggerFactory.getLogger(VarsqlJobBean.class);
	
	@Autowired
	private CommonLogService commonLogService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    	String message = "";
    	String status = BatchStatus.COMPLETED.name();
    	
    	JobDataMap jobDataMap = context.getMergedJobDataMap();
    	
    	JobScheduleVO jsv = VartechUtils.jsonStringToObject(jobDataMap.getString("jobScheduleVO"), JobScheduleVO.class);
    	
    	if(jsv == null) {
    		throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER, "JobScheduleVO not found : " + jsv);
    	}
    	
    	if(logger.isDebugEnabled()) {
    		logger.debug("jobScheduleVO : {}", jsv);
    	}
    	
    	JobResultVO jobResultVo;
        try {
        	jobResultVo= doExecute(context, jsv);
        	message = jobResultVo.getMessage()==null? "" : jobResultVo.getMessage();
        }catch (Throwable e) {
        	status = BatchStatus.FAILED.name();
        	message = e.getMessage();
        	logger.error("executeInternal : {} ", message, e);
        	jobResultVo = JobResultVO.builder().message(message).build();
        }
        
        if(message != null) {
        	message += message.length() > 1500 ? message.substring(0 , 1500) : message;
        }
        
        System.out.println("context.getTrigger().getJobKey().getGroup() : "+ context.getTrigger().getKey().getGroup());
		
		ScheduleHistoryEntity schedulerHistoryEntity = ScheduleHistoryEntity.builder()
				.instanceId(context.getFireInstanceId())
				.jobUid(jsv.getJobUid())
				.startTime(ConvertUtils.longToTimestamp(context.getFireTime().getTime()))
				.endTime(ConvertUtils.longToTimestamp(System.currentTimeMillis()))
				.runTime(context.getJobRunTime())
				.runType(context.getTrigger().getKey().getGroup().startsWith(JOBServiceUtils.RUN_SIMPLE_TIGGER_GROUP_PREFIX)?"run":"batch")
				.message(message)
				.status(status)
				.resultCount(jobResultVo.getResultCount())
				.failCount(jobResultVo.getFailCount())
				.build();
		
		commonLogService.saveScheduleHistory(schedulerHistoryEntity);
        
    }
    
}