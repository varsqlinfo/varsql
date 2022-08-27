package com.varsql.web.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.varsql.web.dto.user.UserPermissionInfoDTO;
import com.varsql.web.model.entity.db.QDBBlockingUserEntity;
import com.varsql.web.model.entity.user.QUserEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.security.repository.UserRepository;

@Repository
public interface UserInfoRepository extends UserRepository ,JpaSpecificationExecutor<UserEntity> , UserInfoRepositoryCustom {

	public class UserInfoRepositoryCustomImpl extends QuerydslRepositorySupport implements UserInfoRepositoryCustom{

		public UserInfoRepositoryCustomImpl() {
			super(UserEntity.class);
		}

		@Override
		public List<UserPermissionInfoDTO> findPermissionInfo(String viewid) {
			
			final QUserEntity userEntity = QUserEntity.userEntity;
			final QDBBlockingUserEntity dbBlockingUserEntity = QDBBlockingUserEntity.dBBlockingUserEntity;
			
			return from(userEntity).leftJoin(dbBlockingUserEntity).on(userEntity.viewid.eq(dbBlockingUserEntity.viewid))
			.select(Projections.constructor(UserPermissionInfoDTO.class, userEntity.viewid, userEntity.acceptYn, userEntity.blockYn, dbBlockingUserEntity.vconnid))
			.where(userEntity.viewid.eq(viewid)).fetch();
		}
	}
}


interface UserInfoRepositoryCustom{
	public List<UserPermissionInfoDTO> findPermissionInfo(@Param("viewid") String viewid);
}