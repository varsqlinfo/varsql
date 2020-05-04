package com.varsql.web.repository.sql;

import java.util.Collections;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.web.model.entity.sql.QSqlStatisticsEntity;
import com.varsql.web.model.entity.sql.SqlStatisticsEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
//public interface SqlStatisticsEntityRepository extends DefaultJpaRepository, JpaRepository<SqlStatisticsEntity, String>, JpaSpecificationExecutor<SqlStatisticsEntity>{
public interface SqlStatisticsEntityRepository extends DefaultJpaRepository, JpaRepository<SqlStatisticsEntity, String>, JpaSpecificationExecutor<SqlStatisticsEntity>, SqlStatisticsEntityCustom {

	@Transactional(readOnly = true)
	public class SqlStatisticsEntityCustomImpl extends QuerydslRepositorySupport implements SqlStatisticsEntityCustom {
		
		public SqlStatisticsEntityCustomImpl() {
			super(SqlStatisticsEntity.class);
		}

		@Override
		// 최근 가입한 limit 갯수 만큼 유저 리스트를 가져온다
		public List<SqlStatisticsEntity> findSqlDateStat(String vconid, String s_date, String e_date) {
			//QSqlStatisticsEntity
			return Collections.emptyList();
		}
	}
}

interface SqlStatisticsEntityCustom {
	List<SqlStatisticsEntity> findSqlDateStat(String vconid, String s_date, String e_date);
}