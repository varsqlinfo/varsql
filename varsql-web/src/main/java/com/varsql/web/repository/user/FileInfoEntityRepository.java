package com.varsql.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface FileInfoEntityRepository extends DefaultJpaRepository, JpaRepository<FileInfoEntity, String> {

}