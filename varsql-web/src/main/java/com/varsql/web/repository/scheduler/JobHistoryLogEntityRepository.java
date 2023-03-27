package com.varsql.web.repository.scheduler;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.scheduler.JobHistoryEntity;
import com.varsql.web.model.entity.scheduler.JobHistoryLogEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface JobHistoryLogEntityRepository extends DefaultJpaRepository, JpaRepository<JobHistoryLogEntity, Long>, JpaSpecificationExecutor<JobHistoryLogEntity> {
	
	JobHistoryEntity findByHistSeq(long histSeq);
	
}
