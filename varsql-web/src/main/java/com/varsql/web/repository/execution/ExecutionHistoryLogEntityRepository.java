package com.varsql.web.repository.execution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.execution.ExecutionHistoryEntity;
import com.varsql.web.model.entity.execution.ExecutionHistoryLogEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface ExecutionHistoryLogEntityRepository extends DefaultJpaRepository, JpaRepository<ExecutionHistoryLogEntity, Long>, JpaSpecificationExecutor<ExecutionHistoryLogEntity> {
	
	ExecutionHistoryEntity findByHistSeq(long histSeq);
	
}
