package com.varsql.web.repository.scheduler;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.scheduler.JobScheduleDetailDTO;
import com.varsql.web.dto.scheduler.TriggerResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.QDBConnectionEntity;
import com.varsql.web.model.entity.scheduler.JobScheduleEntity;
import com.varsql.web.model.entity.scheduler.QJobScheduleEntity;
import com.varsql.web.model.entity.scheduler.QTriggersEntity;
import com.varsql.web.model.entity.scheduler.TriggersEntity;
import com.varsql.web.repository.DefaultJpaRepository;

import lombok.Getter;

@Repository
public interface JobScheduleEntityRepository extends DefaultJpaRepository, JpaRepository<JobScheduleEntity, String>, JpaSpecificationExecutor<JobScheduleEntity>, JobScheduleEntityCustom{
	
	Page<JobScheduleEntity> findByJobGroupAndJobNameContaining(String jobGroup, String jobName, Pageable pageInfo);
	
	JobScheduleEntity findByJobUid(String jobUid);

	public class JobScheduleEntityCustomImpl extends QuerydslRepositorySupport implements JobScheduleEntityCustom {

		public JobScheduleEntityCustomImpl() {
			super(JobScheduleEntity.class);
		}

		@Override
		public JobScheduleDetailDTO findJobDetailInfo(String jobUid) {
			final QJobScheduleEntity jobScheduleEntity = QJobScheduleEntity.jobScheduleEntity;
			final QDBConnectionEntity dBConnectionEntity = QDBConnectionEntity.dBConnectionEntity;
			final QTriggersEntity triggersEntity = QTriggersEntity.triggersEntity;
			
			List<JobScheduleResultVO> list = from(jobScheduleEntity).innerJoin(triggersEntity).on(jobScheduleEntity.jobUid.eq(triggersEntity.jobName))
				.innerJoin(dBConnectionEntity).on(jobScheduleEntity.scheduleDBConnection.vconnid.eq(dBConnectionEntity.vconnid))
				.select(Projections.constructor(JobScheduleResultVO.class,jobScheduleEntity, dBConnectionEntity, triggersEntity))
				.where(jobScheduleEntity.jobUid.eq(jobUid))
				.orderBy(triggersEntity.startTime.desc())
				.fetch();
			
			boolean firstFlag = true; 
			JobScheduleDetailDTO dto = new JobScheduleDetailDTO();
			for (JobScheduleResultVO row : list) {
				if(firstFlag) {
					JobScheduleEntity jse = row.getJobScheduleEntity();
					DBConnectionEntity de = row.getDbConnectionEntity();
					// job info
					dto.setJobUid(jse.getJobUid());
					dto.setJobName(jse.getJobName());
					dto.setJobData(jse.getJobData());
					dto.setJobDescription(jse.getJobDescription());
					dto.setCronExpression(jse.getCronExpression());
					
					// db connection info
					dto.setVconnid(de.getVconnid());
					dto.setVname(de.getVname());
					firstFlag = false; 
				}
				
				TriggersEntity te = row.getTriggersEntity();
				
				dto.getTriggerList().add(TriggerResponseDTO.toDto(te));;
			}
			
			
			return dto; 
			
		}
	}
	
	@Getter
	public class JobScheduleResultVO{
		private JobScheduleEntity jobScheduleEntity;
		private DBConnectionEntity dbConnectionEntity;
		private TriggersEntity triggersEntity;
		public JobScheduleResultVO(JobScheduleEntity jobScheduleEntity, DBConnectionEntity dbConnectionEntity, TriggersEntity triggersEntity) {
			this.jobScheduleEntity = jobScheduleEntity; 
			this.dbConnectionEntity = dbConnectionEntity; 
			this.triggersEntity = triggersEntity;
		}
	}
	
}

interface JobScheduleEntityCustom {
	JobScheduleDetailDTO findJobDetailInfo(String jobUid);
}

