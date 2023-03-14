package com.varsql.web.repository.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.JPQLQuery;
import com.varsql.web.dto.db.DBConnectionResponseDTO;
import com.varsql.web.model.entity.db.DBGroupMappingDbEntity;
import com.varsql.web.model.entity.db.DBGroupMappingUserEntity;
import com.varsql.web.model.entity.db.QDBConnectionEntity;
import com.varsql.web.model.entity.db.QDBGroupMappingDbEntity;
import com.varsql.web.repository.DefaultJpaRepository;
import com.vartech.common.app.beans.SearchParameter;

@Repository
public interface DBGroupMappingDbEntityRepository extends DefaultJpaRepository, JpaRepository<DBGroupMappingDbEntity, Long> , JpaSpecificationExecutor<DBGroupMappingDbEntity> , DBGroupMappingDbEntityRepositoryCustom {

	void deleteByVconnid(String vconnid);

	public class DBGroupMappingDbEntityRepositoryCustomImpl extends QuerydslRepositorySupport implements DBGroupMappingDbEntityRepositoryCustom, DefaultJpaRepository {

		public DBGroupMappingDbEntityRepositoryCustomImpl() {
			super(DBGroupMappingUserEntity.class);
		}

		@Override
		public Page<DBConnectionResponseDTO> findByDbGroupDbList(String groupId, SearchParameter searchParameter, Pageable pageInfo) {
			QDBGroupMappingDbEntity qdbgde = QDBGroupMappingDbEntity.dBGroupMappingDbEntity;
			QDBConnectionEntity qdbe = QDBConnectionEntity.dBConnectionEntity;
			
			String keyworkd = searchParameter.getKeyword();
			List<DBConnectionResponseDTO> result = getDBList(groupId, pageInfo, keyworkd, qdbgde, qdbe);
			
			JPQLQuery<Long> countQuery = getDBCount(groupId, pageInfo, keyworkd, qdbgde, qdbe);
			
		    return PageableExecutionUtils.getPage(result, pageInfo, () -> countQuery.fetchOne());
			
		}
		/**
		 * 카운트 쿼리
		 * @param groupId
		 * @param pageInfo
		 * @param keyworkd
		 * @param qme
		 * @param qdbe
		 * @return
		 */
		private JPQLQuery<Long> getDBCount(String groupId, Pageable pageInfo, String keyworkd, QDBGroupMappingDbEntity qme, QDBConnectionEntity qdbe) {
			JPQLQuery<Long> countQuery = from(qme).innerJoin(qdbe).on(qme.vconnid.eq(qdbe.vconnid))
				.select(qdbe.count())
				.where(qme.groupId.eq(groupId));
				
			return countQuery;
		}
		
		/**
		 * 결과 쿼리
		 * @param groupId
		 * @param pageInfo
		 * @param keyworkd
		 * @param qme
		 * @param qdbe
		 * @return
		 */
		private List<DBConnectionResponseDTO> getDBList(String groupId, Pageable pageInfo, String keyworkd, QDBGroupMappingDbEntity qme, QDBConnectionEntity qdbe) {
        	ArrayList<DBConnectionResponseDTO> reval = new ArrayList<>();
        	
        	from(qme).innerJoin(qdbe).on(qme.vconnid.eq(qdbe.vconnid))
			.select(qdbe.vname, qdbe.vconnid)
			.where(qme.groupId.eq(groupId))
			.orderBy(qdbe.vname.desc())
			.fetch().forEach(tuple->{
				reval.add(DBConnectionResponseDTO.builder()
			    	.vname(tuple.get(qdbe.vname))
			    	.vconnid(tuple.get(qdbe.vconnid))
			    .build());
			});
			
			return reval; 
		}
	}
}

interface DBGroupMappingDbEntityRepositoryCustom{
	Page<DBConnectionResponseDTO> findByDbGroupDbList(String groupId, SearchParameter searchParameter,
			Pageable convertSearchInfoToPage);
}