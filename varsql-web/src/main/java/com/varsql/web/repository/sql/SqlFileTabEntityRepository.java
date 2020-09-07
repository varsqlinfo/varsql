package com.varsql.web.repository.sql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.sql.SqlFileTabResponseDTO;
import com.varsql.web.model.entity.sql.QSqlFileEntity;
import com.varsql.web.model.entity.sql.QSqlFileTabEntity;
import com.varsql.web.model.entity.sql.SqlFileTabEntity;
import com.varsql.web.repository.DefaultJpaRepository;
import com.vartech.common.sort.TreeDataSort;

@Repository
public interface SqlFileTabEntityRepository extends DefaultJpaRepository, JpaRepository<SqlFileTabEntity, String>, JpaSpecificationExecutor<SqlFileTabEntity> ,SqlFileTabEntityCustom  {

	@Modifying
	@Query(value = "update SqlFileTabEntity as ste set ste.viewYn= 'N' where ste.vconnid = :vconnid and ste.viewid = :viewid and ste.viewYn= 'Y'")
	void updateSqlFileTabDisable(@Param("vconnid") String vconnid,@Param("viewid") String viewid);

	@Modifying
	@Query(value = "update SqlFileTabEntity as ste set ste.viewYn= 'Y' where ste.vconnid = :vconnid and ste.viewid = :viewid and ste.sqlId = :sqlId")
	void updateSqlFileTabEnable(@Param("vconnid") String vconnid, @Param("viewid") String viewid, @Param("sqlId") String sqlId);

	@Modifying
	@Query(value = "delete from SqlFileTabEntity ste where ste.vconnid = :vconnid and ste.viewid = :viewid and ste.sqlId = :sqlId")
	void deleteTabInfo(@Param("vconnid") String vconnid, @Param("viewid") String viewid, @Param("sqlId") String sqlId);

	@Modifying
	@Query(value = "delete from SqlFileTabEntity ste where ste.vconnid = :vconnid and ste.viewid = :viewid")
	void deleteAllSqlFileTabInfo(@Param("vconnid") String vconnid, @Param("viewid") String viewid);
	
	@Modifying
	@Query(value = "update SqlFileTabEntity as ste set ste.prevSqlId= :prevId where ste.vconnid = :vconnid and ste.viewid = :viewid and ste.prevSqlId = :sqlId")
	void updateSqlFilePrevID(@Param("vconnid") String vconnid, @Param("viewid") String viewid, @Param("sqlId") String sqlId, @Param("prevId") String prevId);

	@Transactional(readOnly = true , value=ResourceConfigConstants.APP_TRANSMANAGER)
	public class SqlFileTabEntityCustomImpl extends QuerydslRepositorySupport implements SqlFileTabEntityCustom {

		public SqlFileTabEntityCustomImpl() {
			super(SqlFileTabEntity.class);
		}

		@Override
		public List<SqlFileTabResponseDTO> findSqlFileTab(String vconid, String viewid) {
			final QSqlFileTabEntity sqlFileTabEntity = QSqlFileTabEntity.sqlFileTabEntity;
			final QSqlFileEntity sqlFileEntity = QSqlFileEntity.sqlFileEntity;

			//String sqlId, String prevSqlId, String viewYn, String sqlTitle, String sqlCont, String sqlParam
			TreeDataSort tds = new TreeDataSort("sqlId", "prevSqlId");

			from(sqlFileTabEntity).innerJoin(sqlFileEntity).on(sqlFileTabEntity.sqlId.eq(sqlFileEntity.sqlId))
				.select(Projections.constructor(SqlFileTabResponseDTO.class, sqlFileTabEntity.sqlId, sqlFileTabEntity.prevSqlId, sqlFileTabEntity.viewYn, sqlFileEntity.sqlTitle, sqlFileEntity.sqlCont, sqlFileEntity.sqlParam))
				.where(sqlFileTabEntity.vconnid.eq(vconid).and(sqlFileTabEntity.viewid.eq(viewid)))
				.orderBy(sqlFileTabEntity.prevSqlId.asc())
				.fetch().forEach(item -> {
					tds.sortTreeData(item);
				});

			return (List<SqlFileTabResponseDTO>)tds.getSortList();
		}
	}
}


interface SqlFileTabEntityCustom {
	List<SqlFileTabResponseDTO> findSqlFileTab(String vconid, String viewid);

}