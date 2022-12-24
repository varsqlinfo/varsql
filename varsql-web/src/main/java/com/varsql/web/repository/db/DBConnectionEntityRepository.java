package com.varsql.web.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.web.app.component.ConnectionInfoDTO;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.QDBConnectionEntity;
import com.varsql.web.model.entity.db.QDBTypeDriverEntity;
import com.varsql.web.model.entity.db.QDBTypeDriverProviderEntity;
import com.varsql.web.repository.DefaultJpaRepository;
import com.varsql.web.util.ConvertUtils;

@Repository
public interface DBConnectionEntityRepository extends DefaultJpaRepository, JpaRepository<DBConnectionEntity, String>, JpaSpecificationExecutor<DBConnectionEntity> , DBConnectionEntityCustom {
	public DBConnectionEntity findByVconnid(String vconnid);

	@Transactional(readOnly = true , value=ResourceConfigConstants.APP_TRANSMANAGER)
	public class DBConnectionEntityCustomImpl extends QuerydslRepositorySupport implements DBConnectionEntityCustom {

		public DBConnectionEntityCustomImpl() {
			super(DBConnectionEntity.class);
		}

		@Override
		public ConnectionInfoDTO findByConnInfo(String vconnid) {
			final QDBConnectionEntity dBConnectionEntity = QDBConnectionEntity.dBConnectionEntity;
			final QDBTypeDriverProviderEntity dBTypeDriverProviderEntity = QDBTypeDriverProviderEntity.dBTypeDriverProviderEntity;
			final QDBTypeDriverEntity dBTypeDriverEntity = QDBTypeDriverEntity.dBTypeDriverEntity;
			
			
			ConnectionInfoDTO resultDto = from(dBConnectionEntity).leftJoin(dBTypeDriverProviderEntity).on(dBConnectionEntity.dbTypeDriverProvider.driverProviderId.eq(dBTypeDriverProviderEntity.driverProviderId))
			.leftJoin(dBTypeDriverEntity).on(dBTypeDriverProviderEntity.driverId.eq(dBTypeDriverEntity.driverId))
			.select(Projections.constructor(ConnectionInfoDTO.class, dBConnectionEntity, dBTypeDriverProviderEntity, dBTypeDriverEntity))
			.where(dBConnectionEntity.vconnid.eq(vconnid)).fetchOne();
			
			if(resultDto == null) {
				throw new ConnectionFactoryException(VarsqlAppCode.EC_FACTORY_CONNECTION_INFO_EMPTY, "connection id : [" + vconnid + "] Connection info is null");
			}
			
			if(resultDto.getProvider() == null) {
				throw new ConnectionFactoryException(VarsqlAppCode.EC_FACTORY_CONNECTION_DRIVER_ERROR, "connection id : [" + vconnid + "] Driver info is null");
			}
			
			return resultDto;
			
		}

		@Override
		public DatabaseInfo findDatabaseInfo(String vconnid) {
			final QDBConnectionEntity dBConnectionEntity = QDBConnectionEntity.dBConnectionEntity;
			final QDBTypeDriverProviderEntity dBTypeDriverProviderEntity = QDBTypeDriverProviderEntity.dBTypeDriverProviderEntity;
			
			ConnectionInfoDTO dto = from(dBConnectionEntity).innerJoin(dBTypeDriverProviderEntity)
				.on(dBConnectionEntity.dbTypeDriverProvider.driverProviderId.eq(dBTypeDriverProviderEntity.driverProviderId))
				.select(Projections.constructor(ConnectionInfoDTO.class, dBConnectionEntity, dBTypeDriverProviderEntity))
				.where(dBConnectionEntity.vconnid.eq(vconnid)).fetchOne();
			
			return DatabaseInfo.builder()
					.vconnid(dto.getConnection().getVconnid())
					.type(dto.getProvider().getDbType())
					.name(dto.getConnection().getVname())
					.schema(dto.getConnection().getVdbschema())
					.basetableYn(dto.getConnection().getBasetableYn())
					.lazyLoad(dto.getConnection().getLazyloadYn())
					.version(ConvertUtils.longValueOf(dto.getConnection().getVdbversion()))
					.schemaViewYn(dto.getConnection().getSchemaViewYn())
					.maxSelectCount(ConvertUtils.intValue(dto.getConnection().getMaxSelectCount()))
					.databaseName(dto.getConnection().getVdatabasename())
					.build();
			
		}

	}
}

interface DBConnectionEntityCustom {
	ConnectionInfoDTO findByConnInfo(@Param(DBConnectionEntity.VCONNID) String vconnid);
	
	DatabaseInfo findDatabaseInfo(String vconnid);
}