package com.varsql.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.app.QnAEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface QnAEntityRepository extends DefaultJpaRepository, JpaRepository<QnAEntity, String>, JpaSpecificationExecutor<QnAEntity>  {
	
	public QnAEntity findByQnaid(String qnaid);
}
