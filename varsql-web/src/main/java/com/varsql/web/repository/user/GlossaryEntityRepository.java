package com.varsql.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.user.GlossaryEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface GlossaryEntityRepository extends DefaultJpaRepository, JpaRepository<GlossaryEntity, Long>, JpaSpecificationExecutor<GlossaryEntity>  {
	public void deleteByWordIdx(Long wordIdx);
}
