package com.varsql.web.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.dto.user.UserPermissionInfoDTO;
import com.varsql.web.model.entity.db.DBBlockingUserEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.security.repository.UserRepository;

@Repository
public interface UserInfoRepository extends UserRepository ,JpaSpecificationExecutor<UserEntity>  {

	@Query("select new com.varsql.web.dto.user.UserPermissionInfoDTO(m."+UserEntity.VIEWID+", m."+UserEntity.ACCEPT_YN+", m."+UserEntity.BLOCK_YN+", t."+DBBlockingUserEntity.VCONNID+") from UserEntity m left join m.dbBlockingUserEntity t where m.viewid= :viewid")
	public List<UserPermissionInfoDTO> findPermissionInfo(@Param("viewid") String viewid);



}
