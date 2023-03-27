package com.varsql.web.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.user.UserLogHistEntity;

@Repository
public interface UserLogHistRepository extends JpaRepository<UserLogHistEntity, Long> {
}