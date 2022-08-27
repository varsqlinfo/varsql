package com.varsql.web.app.scheduler;

import java.time.Instant;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.dto.scheduler.JobScheduleVO;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

public class JOBServiceUtils {
	
	final public static String JOB_UID = "jobUid";
	
	final public static String RUN_SIMPLE_TIGGER_GROUP_PREFIX = "simpleRun"; 
	
	/**
	 * job save or update
	 *
	 * @method : saveOrUpdate
	 * @param scheduler
	 * @param jobServiceClass
	 * @param vo
	 * @throws SchedulerException
	 */
	public static void saveOrUpdate(Scheduler scheduler, Class<? extends Job> jobServiceClass, JobScheduleVO vo) throws SchedulerException{
		
		if(StringUtils.isBlank(vo.getCronExpression())) {
			throw new VarsqlRuntimeException(VarsqlAppCode.ERROR ,"cron");
		}
		
		if(scheduler.checkExists(new JobKey(vo.getJobUid(), vo.getJobGroup()))) {
			updateJobInfo(scheduler, jobServiceClass, vo);
		}else {
			createJobInfo(scheduler, jobServiceClass, vo);
		}
	}
	
	/**
	 * create job info 
	 *
	 * @method : createJobInfo
	 * @param scheduler
	 * @param jobServiceClass
	 * @param vo
	 * @param jobKey
	 * @throws SchedulerException
	 */
	private static void createJobInfo(Scheduler scheduler, Class<? extends Job> jobServiceClass, JobScheduleVO vo) throws SchedulerException {
		String jobName = vo.getJobUid();
		String jobGroup = vo.getJobGroup();
		
		JobDetail jobDetail = JobBuilder.newJob(jobServiceClass).withIdentity(jobName, jobGroup)
                .build();
		
		CronTrigger cronTrigger  = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(vo.getCronExpression()))
                .withDescription(StringUtils.isBlank(vo.getJobDescription())? vo.getJobName() : vo.getJobDescription())
                .build();
        
		cronTrigger.getJobDataMap().put("jobScheduleVO", VartechUtils.objectToJsonString(vo));
		
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * update job info 
	 *
	 * @method : updateJobInfo
	 * @param scheduler
	 * @param jobServiceClass
	 * @param vo
	 * @param jobKey
	 * @throws SchedulerException
	 */
	private static void updateJobInfo(Scheduler scheduler, Class<? extends Job> jobServiceClass, JobScheduleVO vo) throws SchedulerException {
		
		TriggerKey triggerKey = TriggerKey.triggerKey(vo.getJobUid(), vo.getJobGroup());

		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(vo.getCronExpression());
		cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder)
				.withDescription(
						StringUtils.isBlank(vo.getJobDescription()) ? vo.getJobName() : vo.getJobDescription())
				.build();
		
		cronTrigger.getJobDataMap().put("jobScheduleVO", VartechUtils.objectToJsonString(vo));

		scheduler.rescheduleJob(triggerKey, cronTrigger);
		
	}

	/**
	 * job 삭제.
	 *
	 * @method : deleteJob
	 * @param scheduler
	 * @param vo
	 * @throws SchedulerException
	 */
	public static void deleteJob(Scheduler scheduler, JobScheduleVO vo) throws SchedulerException {
		JobKey jobKey = JobKey.jobKey(vo.getJobUid(), vo.getJobGroup());
		scheduler.deleteJob(jobKey);
	}
	
	/**
	 * job 실행
	 *
	 * @method : runJob
	 * @param scheduler
	 * @param vo
	 * @throws SchedulerException
	 */
	public static void runJob(Scheduler scheduler, JobScheduleVO vo) throws SchedulerException{
		
        JobKey jobKey = JobKey.jobKey(vo.getJobUid(), vo.getJobGroup());
        
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        
        Trigger trigger =TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(vo.getJobUid(), RUN_SIMPLE_TIGGER_GROUP_PREFIX+"-"+ VartechUtils.generateUUID())
                .withDescription("simple run")
                .startAt(Date.from(Instant.now().plusSeconds(5)))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
        
        
        trigger.getJobDataMap().put("jobScheduleVO", VartechUtils.objectToJsonString(vo));
        
        /*
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(, vo.getJobGroup())
                .startAt(Date.from(Instant.now().plusSeconds(5)))
                .build();
                
        */
        scheduler.scheduleJob(trigger);
    }
	
	
	/**
	 * 일시정지
	 *
	 * @method : pauseJob
	 * @param scheduler
	 * @param vo
	 * @throws SchedulerException
	 */
	public static void pauseJob(Scheduler scheduler, JobScheduleVO vo) throws SchedulerException {
		JobKey jobKey = JobKey.jobKey(vo.getJobUid(), vo.getJobGroup());
		scheduler.pauseJob(jobKey);
	}
	
	/**
	 * job 재시작
	 *
	 * @method : resumeJob
	 * @param scheduler
	 * @param vo
	 * @throws SchedulerException
	 */
	public static void resumeJob(Scheduler scheduler, JobScheduleVO vo) throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(vo.getJobUid(), vo.getJobGroup());
        scheduler.resumeJob(jobKey);
    }
}
