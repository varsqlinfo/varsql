package com.varsql.web.repository.scheduler;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.scheduler.ScheduleHistoryEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface ScheduleHistoryEntityRepository extends DefaultJpaRepository, JpaRepository<ScheduleHistoryEntity, String>, JpaSpecificationExecutor<ScheduleHistoryEntity> {
	
	Page<ScheduleHistoryEntity> findByJobUid(String jobUid, Pageable pageInfo);
	
}
