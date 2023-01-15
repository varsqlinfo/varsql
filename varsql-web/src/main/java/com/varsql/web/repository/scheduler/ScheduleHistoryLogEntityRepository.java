package com.varsql.web.repository.scheduler;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.scheduler.ScheduleHistoryEntity;
import com.varsql.web.model.entity.scheduler.ScheduleHistoryLogEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface ScheduleHistoryLogEntityRepository extends DefaultJpaRepository, JpaRepository<ScheduleHistoryLogEntity, Long>, JpaSpecificationExecutor<ScheduleHistoryLogEntity> {
	
	ScheduleHistoryEntity findByHistSeq(long histSeq);
	
}
