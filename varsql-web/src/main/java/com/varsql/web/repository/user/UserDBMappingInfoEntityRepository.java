package com.varsql.web.repository.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.varsql.core.auth.AuthorityType;
import com.varsql.core.auth.User;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.varsql.web.model.entity.db.QDBBlockingUserEntity;
import com.varsql.web.model.entity.db.QDBConnectionEntity;
import com.varsql.web.model.entity.db.QDBGroupEntity;
import com.varsql.web.model.entity.db.QDBGroupMappingDbEntity;
import com.varsql.web.model.entity.db.QDBGroupMappingUserEntity;
import com.varsql.web.model.entity.db.QDBManagerEntity;
import com.varsql.web.model.entity.db.QDBTypeDriverProviderEntity;
import com.varsql.web.repository.DefaultJpaRepository;
import com.varsql.web.util.ConvertUtils;

import lombok.Getter;

@Repository
public interface UserDBMappingInfoEntityRepository extends DefaultJpaRepository, JpaRepository<DBConnectionEntity, String>, JpaSpecificationExecutor<DBConnectionEntity>, UserDBMappingInfoEntityCustom {

	public class UserDBMappingInfoEntityCustomImpl extends QuerydslRepositorySupport implements UserDBMappingInfoEntityCustom {

		public UserDBMappingInfoEntityCustomImpl() {
			super(DBConnectionEntity.class);
		}

		@Override
		public List<DatabaseInfo> userDBInfo(User userInfo) {
			AuthorityType tmpAuthority = userInfo.getTopAuthority();
			
			if(tmpAuthority.equals(AuthorityType.GUEST)) return Collections.emptyList();
			
			final QDBConnectionEntity connInfo= QDBConnectionEntity.dBConnectionEntity;
			final QDBTypeDriverProviderEntity provider= QDBTypeDriverProviderEntity.dBTypeDriverProviderEntity; 
			final QDBGroupEntity dbGroup= QDBGroupEntity.dBGroupEntity; 
			final QDBGroupMappingDbEntity dbGroupMappingDb= QDBGroupMappingDbEntity.dBGroupMappingDbEntity; 
			final QDBGroupMappingUserEntity dbGroupMappingUser= QDBGroupMappingUserEntity.dBGroupMappingUserEntity; 
			
			final QDBBlockingUserEntity dbBlockingUser = QDBBlockingUserEntity.dBBlockingUserEntity;
			final QDBManagerEntity dbManager = QDBManagerEntity.dBManagerEntity;
			
			
			BooleanBuilder builder = new BooleanBuilder();
			
			if (!tmpAuthority.equals(AuthorityType.ADMIN)) {
				builder.and(connInfo.vconnid.in(
						JPAExpressions.select(dbGroupMappingDb.vconnid).from(dbGroup).innerJoin(dbGroupMappingDb).on(dbGroup.groupId.eq(dbGroupMappingDb.groupId))
						.innerJoin(dbGroupMappingUser).on(dbGroupMappingDb.groupId.eq(dbGroupMappingUser.groupId))
						.innerJoin(connInfo).on(dbGroupMappingDb.vconnid.eq(connInfo.vconnid))
						.leftJoin(dbBlockingUser).on(connInfo.vconnid.eq(dbBlockingUser.vconnid).and(dbGroupMappingUser.viewid.eq(dbBlockingUser.viewid)))
						.where(
							dbGroupMappingUser.viewid.eq(userInfo.getViewid())
							.and(dbBlockingUser.viewid.isNull())
							.and(connInfo.useYn.eq("Y"))
						)
					).or(tmpAuthority.equals(AuthorityType.MANAGER) ? connInfo.vconnid.in( // 매니저 체크 
							JPAExpressions.select(dbManager.vconnid)
							.from(dbManager)
							.where(dbManager.viewid.eq(userInfo.getViewid()))
						) :  new BooleanBuilder())
				);
			}
			
			// 쿼리 
			JPQLQuery<DBCustomVO> query =from(connInfo).leftJoin(provider).on(connInfo.dbTypeDriverProvider.driverProviderId.eq(provider.driverProviderId))
					.select(Projections.constructor(DBCustomVO.class, connInfo, provider))
					.where(connInfo.useYn.eq("Y").and(connInfo.delYn.eq(false)).and(builder));
					
			List<DatabaseInfo> result =new ArrayList<>();
			query.fetch().forEach(item->{
				result.add(DatabaseInfo.builder()
					.vconnid(item.getConnection().getVconnid())
					.type(item.getProvider().getDbType())
					.name(item.getConnection().getVname())
					.schema(item.getConnection().getVdbschema())
					.basetableYn(item.getConnection().getBasetableYn())
					.lazyLoad(item.getConnection().getLazyloadYn())
					.version(ConvertUtils.longValueOf(item.getConnection().getVdbversion()))
					.schemaViewYn(item.getConnection().getSchemaViewYn())
					.maxSelectCount(ConvertUtils.intValue(item.getConnection().getMaxSelectCount()))
					.useColumnLabel(item.getConnection().getUseColumnLabel())
					.databaseName(item.getConnection().getVdatabasename())
					.build());
			});
			
			return result; 
		}
	}
	
	@Getter
	public class DBCustomVO{
		private DBConnectionEntity connection;
		private DBTypeDriverProviderEntity provider;
		
		public DBCustomVO(DBConnectionEntity connection, DBTypeDriverProviderEntity provider) {
			this.connection = connection; 
			this.provider = provider; 
		}
	}
}

interface UserDBMappingInfoEntityCustom {
	List<DatabaseInfo> userDBInfo(User user);
}