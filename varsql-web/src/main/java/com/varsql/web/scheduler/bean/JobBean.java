package com.varsql.web.scheduler.bean;

import java.util.concurrent.TimeUnit;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.ExecuteType;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.common.service.CommonLogService;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobDetailDTO;
import com.varsql.web.dto.scheduler.JobVO;
import com.varsql.web.dto.user.RegInfoDTO;
import com.varsql.web.dto.websocket.MessageDTO;
import com.varsql.web.model.entity.execution.ExecutionHistoryEntity;
import com.varsql.web.model.entity.execution.ExecutionHistoryLogEntity;
import com.varsql.web.repository.scheduler.JobEntityRepository;
import com.varsql.web.scheduler.ExecuteContextHolder;
import com.varsql.web.scheduler.JOBServiceUtils;
import com.varsql.web.util.ConvertUtils;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

/**
 * 
*  
* @fileName	: JobBean.java
* @author	: ytkim
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public abstract class JobBean extends QuartzJobBean implements JobService{
 
	private final Logger logger = LoggerFactory.getLogger(JobBean.class);
	
	@Autowired
	private CommonLogService commonLogService;
	
	@Autowired
	private JobEntityRepository jobEntityRepository;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    	
    	 try {
    		 
			ExecuteType executeType = context.getTrigger().getKey().getGroup().startsWith(JOBServiceUtils.RUN_SIMPLE_TIGGER_GROUP_PREFIX) ? ExecuteType.NORMAL : ExecuteType.BATCH;
			ExecuteContextHolder.set(executeType, context.getJobDetail().getKey().getName());
    	        
	    	String message = "";
	    	BatchStatus status = BatchStatus.COMPLETED;
	    	
	    	JobDataMap jobDataMap = context.getMergedJobDataMap();
	    	
	    	JobVO jsv = VartechUtils.jsonStringToObject(jobDataMap.getString("jobCustomVO"), JobVO.class);
	    	
	    	if(jsv == null) {
	    		throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER, "jobCustomVO not found : " + jsv);
	    	}
	    	
	    	if(logger.isDebugEnabled()) {
	    		logger.debug("jobCustomVO : {}", jsv);
	    	}
	    	
	    	long startTime = System.currentTimeMillis(); 
	    	
	    	JobResultVO jobResultVo;
	    	
	        try {
	        	jobResultVo= doExecute(context, jsv);
	        	message = jobResultVo.getMessage()==null? "" : jobResultVo.getMessage();
	        }catch (Throwable e) {
	        	status = BatchStatus.FAILED;
	        	message = e.getMessage();
	        	logger.error("executeInternal : {} ", message, e);
	        	jobResultVo = JobResultVO.builder().build();
	        }
	        
	        if(message != null) {
	        	message = message.length() > 1500 ? message.substring(0 , 1500) : message;
	        }
	        
	        String customInfo =null;
	        if(jobResultVo.getCustomInfo() != null) {
	        	customInfo = VartechUtils.objectToJsonString(jobResultVo.getCustomInfo());
	        }
	        
	        long endTime = System.currentTimeMillis(); 
	        
			ExecutionHistoryEntity executionHistoryEntity = ExecutionHistoryEntity.builder()
				.instanceId(context.getFireInstanceId())
				.targetType(ExecuteType.BATCH)
				.targetId(jsv.getJobUid())
				.startTime(ConvertUtils.longToTimestamp(startTime))
				.endTime(ConvertUtils.longToTimestamp(endTime))
				.runTime(TimeUnit.MILLISECONDS.toSeconds(endTime - startTime))
				.runType(executeType.name())
				.message(message)
				.status(status.name())
				.resultCount(jobResultVo.getResultCount())
				.failCount(jobResultVo.getFailCount())
				.customInfo(customInfo)
				.build();
			
			JobDetailDTO detailDto = jobEntityRepository.findJobDetailInfo(jsv.getJobUid());
			
			MessageDTO messageDTO = null;
			if(detailDto != null) {
				messageDTO = MessageDTO.builder()
				.title(jsv.getJobName())
				.recvIds(new String[] {detailDto.getRegId()})
				.build();
			}
			
			if(!StringUtils.isBlank(jobResultVo.getLog())) {
				commonLogService.saveExecutionHistory(messageDTO, executionHistoryEntity, ExecutionHistoryLogEntity.builder()
						.histSeq(executionHistoryEntity.getHistSeq())
						.logType(jobResultVo.getJobType().name())
						.log(jobResultVo.getLog())
						.build());
			}else {
				commonLogService.saveExecutionHistory(messageDTO, executionHistoryEntity);
			}
	    }finally {
	    	ExecuteContextHolder.clear();
	    }
		
    }
    
}