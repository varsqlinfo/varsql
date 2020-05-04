package com.varsql.web.repository.sql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.StringExpression;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.sql.SqlStatisticsReponseDTO;
import com.varsql.web.model.entity.sql.QSqlStatisticsEntity;
import com.varsql.web.model.entity.sql.SqlStatisticsEntity;
import com.varsql.web.repository.DefaultJpaRepository;
import com.varsql.web.util.ConvertUtils;
import com.varsql.web.util.QueryDslUtils;

@Repository
public interface SqlStatisticsEntityRepository extends DefaultJpaRepository, JpaRepository<SqlStatisticsEntity, String>, JpaSpecificationExecutor<SqlStatisticsEntity>, SqlStatisticsEntityCustom {

	@Transactional(readOnly = true , value=ResourceConfigConstants.APP_TRANSMANAGER)
	public class SqlStatisticsEntityCustomImpl extends QuerydslRepositorySupport implements SqlStatisticsEntityCustom {
		
		public SqlStatisticsEntityCustomImpl() {
			super(SqlStatisticsEntity.class);
		}
		
		// 통계 처리. 
		@Override
		public List<SqlStatisticsReponseDTO> findSqlDateStat(String vconid, String s_date, String e_date) {
			final QSqlStatisticsEntity sqlStat = QSqlStatisticsEntity.sqlStatisticsEntity;
			
			StringExpression viewDt = QueryDslUtils.toChar(sqlStat.startTime.max()); 
			
			return from(sqlStat)
					.select(Projections.constructor(SqlStatisticsReponseDTO.class,viewDt.as("viewdt") , sqlStat.vconnid.count().as("cnt")))
					.where(sqlStat.vconnid.eq(vconid).and(sqlStat.startTime.between(ConvertUtils.stringToLocalDateTime(s_date), ConvertUtils.stringToLocalDateTime(e_date))))
					.groupBy(sqlStat.sMm,sqlStat.sDd)
					.orderBy(sqlStat.sMm.asc(),sqlStat.sDd.asc())
					.fetch();
		}
	}
}

interface SqlStatisticsEntityCustom {
	List<SqlStatisticsReponseDTO> findSqlDateStat(String vconid, String s_date, String e_date);
}