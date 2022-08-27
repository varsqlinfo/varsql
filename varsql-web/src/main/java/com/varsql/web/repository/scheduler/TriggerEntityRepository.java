package com.varsql.web.repository.scheduler;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.scheduler.TriggersEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface TriggerEntityRepository extends DefaultJpaRepository, JpaRepository<TriggersEntity, String>, JpaSpecificationExecutor<TriggersEntity> {
	
	Page<TriggersEntity> findByJobName(String jobName, Pageable convertSearchInfoToPage);
	
}
