package com.varsql.web.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.app.component.ConnectionInfoDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBConnectionEntityRepository extends DefaultJpaRepository, JpaRepository<DBConnectionEntity, String>, JpaSpecificationExecutor<DBConnectionEntity>  {
	public DBConnectionEntity findByVconnid(String vconnid);

	@Query(value = "select new com.varsql.web.app.component.ConnectionInfoDTO(a, b, c) "
			+ "from DBConnectionEntity a join DBTypeDriverProviderEntity b on a.vdriver = b.driverProviderId "
			+ " join DBTypeDriverEntity c on b.driverId = c.driverId where a.vconnid = :vconnid ")
	ConnectionInfoDTO findByConnInfo(@Param(DBConnectionEntity.VCONNID) String connid);
}