package com.varsql.web.repository.sql;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.sql.SqlStatisticsReponseDTO;
import com.varsql.web.model.entity.sql.QSqlStatisticsEntity;
import com.varsql.web.model.entity.sql.SqlStatisticsEntity;
import com.varsql.web.model.entity.user.QUserEntity;
import com.varsql.web.repository.DefaultJpaRepository;
import com.varsql.web.util.ConvertUtils;
import com.varsql.web.util.ValidateUtils;

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

			return from(sqlStat)
				.select(Projections.constructor(SqlStatisticsReponseDTO.class, sqlStat.startTime.max(), sqlStat.sMm, sqlStat.sDd, sqlStat.vconnid.count().as("YCol")))
				.where(sqlStat.vconnid.eq(vconid).and(sqlStat.startTime.between(ConvertUtils.stringToLocalDateTime(s_date), ConvertUtils.stringToLocalDateTime(e_date))))
				.groupBy(sqlStat.sMm,sqlStat.sDd)
				.orderBy(sqlStat.sMm.asc(),sqlStat.sDd.asc())
				.fetch().stream().map(item ->{
					item.setViewDt(ConvertUtils.dateToStringDate(item.getStartTime()));
					item.setXCol(String.format("%02d%02d", item.getSMm(),item.getSDd()));
					return item;
			}).collect(Collectors.toList());
		}

		@Override
		public List<SqlStatisticsReponseDTO> findSqlDayStat(String vconid, String s_date, String e_date) {
			final QSqlStatisticsEntity sqlStat = QSqlStatisticsEntity.sqlStatisticsEntity;

			return from(sqlStat)
				.select(Projections.constructor(SqlStatisticsReponseDTO.class,sqlStat.commandType.as("xCol") ,sqlStat.vconnid.count().as("yCol")))
				.where(sqlStat.vconnid.eq(vconid).and(sqlStat.startTime.between(ConvertUtils.stringToLocalDateTime(s_date), ConvertUtils.stringToLocalDateTime(e_date))))
				.groupBy(sqlStat.commandType)
				.fetch();
		}

		@Override
		public List<SqlStatisticsReponseDTO> findDayUserRank(String vconid, String s_date, String e_date, String commandType) {
			final QSqlStatisticsEntity sqlStat = QSqlStatisticsEntity.sqlStatisticsEntity;

			QUserEntity user = QUserEntity.userEntity;

			BooleanExpression commandTypeChk = null;
			if(ValidateUtils.isNotBlank(commandType) && !"all".contentEquals(commandType)) {
				commandTypeChk = sqlStat.commandType.eq(ConvertUtils.toUpperCase(commandType));
			}

			return from(user)
				.select(Projections.constructor(SqlStatisticsReponseDTO.class,user.uname.as("viewid") ,sqlStat.vconnid.count().as("cnt")))
				.innerJoin(sqlStat)
				.on(user.viewid.eq(sqlStat.viewid))
				.where(sqlStat.vconnid.eq(vconid).and(commandTypeChk).and(sqlStat.startTime.between(ConvertUtils.stringToLocalDateTime(s_date), ConvertUtils.stringToLocalDateTime(e_date))))
				.groupBy(sqlStat.viewid)
				.orderBy(sqlStat.vconnid.count().asc())
				.limit(5)
				.fetch();

		}
	}
}

interface SqlStatisticsEntityCustom {
	List<SqlStatisticsReponseDTO> findSqlDateStat(String vconid, String s_date, String e_date);

	List<SqlStatisticsReponseDTO> findSqlDayStat(String vconid, String s_date, String e_date);

	List<SqlStatisticsReponseDTO> findDayUserRank(String vconid, String s_date, String e_date , String commandType);
}