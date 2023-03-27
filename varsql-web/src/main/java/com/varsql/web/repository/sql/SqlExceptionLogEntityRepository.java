package com.varsql.web.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.app.ExceptionLogEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface SqlExceptionLogEntityRepository extends DefaultJpaRepository, JpaRepository<ExceptionLogEntity, String>, JpaSpecificationExecutor<ExceptionLogEntity>  {
}