package com.varsql.web.repository.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.user.UserEntity;

@Repository
public interface UserMgmtRepository extends UserRepository {
	List<UserEntity> findByViewidIn(List<String> viewids);
	
	
	
}
