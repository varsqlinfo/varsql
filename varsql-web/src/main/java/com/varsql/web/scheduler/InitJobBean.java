package com.varsql.web.scheduler;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.varsql.core.configuration.Configuration;
import com.varsql.web.app.manager.service.SchedulerMgmtServiceImpl;
import com.varsql.web.dto.scheduler.JobRequestDTO;
import com.varsql.web.dto.scheduler.JobVO;
import com.varsql.web.model.entity.db.DBConnectionViewEntity;
import com.varsql.web.model.entity.scheduler.JobEntity;
import com.varsql.web.repository.scheduler.JobEntityRepository;
import com.varsql.web.scheduler.job.BackupFileRemoveJob;

/**
 * 
*-----------------------------------------------------------------------------
* @fileName		: InitJobBean.java
* @desc		: 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2023. 2. 5. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class InitJobBean {
	
	private final Logger logger = LoggerFactory.getLogger(InitJobBean.class);
	
	@Autowired
	private JobEntityRepository jobEntityRepository;
	
	@Autowired
	private SchedulerMgmtServiceImpl schedulerMgmtServiceImpl;

    public void init() {
        initBackupFileDeleteJob();
    }

	private void initBackupFileDeleteJob() {
		
		JobRequestDTO dto = new JobRequestDTO();
		
		logger.info("backup file delete job info : {}", dto);
		
		
		dto.setJobUid("varsql_backup_file_delete_job");
		dto.setCronExpression(Configuration.getInstance().getBackupExpireCron());
		dto.setJobName("backupFileDeleteJob");
		
		String jobUid =  dto.getJobUid();
		
		schedulerMgmtServiceImpl.delete(jobUid);
		
		JobEntity entity;
		
		entity = jobEntityRepository.findByJobUid(jobUid);
		
		if(entity == null) {
			entity = dto.toEntity();
			entity.setJobUid(jobUid);
		}else {
			if(dto.getCronExpression().equals(entity.getCronExpression())) {
				return ; 
			}
		}
		
		entity.setJobName(dto.getJobName());
		entity.setCronExpression(dto.getCronExpression());
		entity.setJobData(dto.getJobData());
		entity.setJobDescription(dto.getJobDescription());
		entity.setJobGroup("backupFileDeleteJobGroup");
		entity.setJobDBConnection(DBConnectionViewEntity.builder().vconnid("empty").build());
		entity.setRegId("vasqlAdmin");
		entity.setUpdId("vasqlAdmin");
		
		jobEntityRepository.save(entity);
		
		try {
			schedulerMgmtServiceImpl.saveOrUpdate(BackupFileRemoveJob.class, JobVO.toVo(entity));
		} catch (SchedulerException e) {
			logger.error("backup file delete job error : {}", e.getMessage(), e);
		}
		
	}
}
