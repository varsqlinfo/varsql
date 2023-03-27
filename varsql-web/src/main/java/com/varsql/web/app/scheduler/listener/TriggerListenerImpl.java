package com.varsql.web.app.scheduler.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
* 
* @fileName	: TriggersListener.java
* @author	: ytkim
 */
@Component
public class TriggerListenerImpl implements TriggerListener {
	private final Logger logger = LoggerFactory.getLogger(TriggerListenerImpl.class);
	
    @Override
    public String getName() {
        return "varsqlTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
//        JobKey jobKey = trigger.getJobKey();
//        logger.info("triggerFired at {} :: jobKey : {}", trigger.getStartTime(), jobKey);
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
//        JobKey jobKey = trigger.getJobKey();
//        logger.info("triggerMisfired at {} :: jobKey : {}", trigger.getStartTime(), jobKey);
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
								Trigger.CompletedExecutionInstruction triggerInstructionCode) {
//        JobKey jobKey = trigger.getJobKey();
//        logger.info("triggerComplete at jobKey {} :: jobName ,{} startTime : {} , endTime :{}", jobKey, jobKey.getName(), context.getFireTime(), context.getJobRunTime());
    }
}
