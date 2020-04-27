package com.varsql.web.repository.user;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.security.repository.UserRepository;

@Repository
public interface UserMgmtRepository extends UserRepository ,JpaSpecificationExecutor<UserEntity>  {
	UserEntity findByViewid(String viewId);
	
}
