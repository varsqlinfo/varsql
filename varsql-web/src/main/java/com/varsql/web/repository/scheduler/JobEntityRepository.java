package com.varsql.web.repository.scheduler;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.varsql.web.dto.scheduler.JobDetailDTO;
import com.varsql.web.dto.scheduler.TriggerResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.QDBConnectionEntity;
import com.varsql.web.model.entity.scheduler.JobEntity;
import com.varsql.web.model.entity.scheduler.QJobEntity;
import com.varsql.web.model.entity.scheduler.QTriggersEntity;
import com.varsql.web.model.entity.scheduler.TriggersEntity;
import com.varsql.web.repository.DefaultJpaRepository;

import lombok.Getter;

@Repository
public interface JobEntityRepository extends DefaultJpaRepository, JpaRepository<JobEntity, String>, JpaSpecificationExecutor<JobEntity>, JobScheduleEntityCustom{
	
	Page<JobEntity> findByJobGroupAndJobNameContaining(String jobGroup, String jobName, Pageable pageInfo);
	
	JobEntity findByJobUid(String jobUid);

	public class JobScheduleEntityCustomImpl extends QuerydslRepositorySupport implements JobScheduleEntityCustom {

		public JobScheduleEntityCustomImpl() {
			super(JobEntity.class);
		}

		@Override
		public JobDetailDTO findJobDetailInfo(String jobUid) {
			final QJobEntity jobScheduleEntity = QJobEntity.jobEntity;
			final QDBConnectionEntity dBConnectionEntity = QDBConnectionEntity.dBConnectionEntity;
			final QTriggersEntity triggersEntity = QTriggersEntity.triggersEntity;
			
			List<JobScheduleResultVO> list = from(jobScheduleEntity).innerJoin(triggersEntity).on(jobScheduleEntity.jobUid.eq(triggersEntity.jobName))
				.innerJoin(dBConnectionEntity).on(jobScheduleEntity.jobDBConnection.vconnid.eq(dBConnectionEntity.vconnid))
				.select(Projections.constructor(JobScheduleResultVO.class,jobScheduleEntity, dBConnectionEntity, triggersEntity))
				.where(jobScheduleEntity.jobUid.eq(jobUid))
				.orderBy(triggersEntity.startTime.desc())
				.fetch();
			
			boolean firstFlag = true; 
			JobDetailDTO dto = new JobDetailDTO();
			for (JobScheduleResultVO row : list) {
				if(firstFlag) {
					JobEntity jse = row.getJobEntity();
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
		private JobEntity jobEntity;
		private DBConnectionEntity dbConnectionEntity;
		private TriggersEntity triggersEntity;
		public JobScheduleResultVO(JobEntity jobEntity, DBConnectionEntity dbConnectionEntity, TriggersEntity triggersEntity) {
			this.jobEntity = jobEntity; 
			this.dbConnectionEntity = dbConnectionEntity; 
			this.triggersEntity = triggersEntity;
		}
	}
	
}

interface JobScheduleEntityCustom {
	JobDetailDTO findJobDetailInfo(String jobUid);
}

