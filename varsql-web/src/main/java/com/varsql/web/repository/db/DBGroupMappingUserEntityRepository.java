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
import com.varsql.web.dto.user.UserResponseDTO;
import com.varsql.web.model.entity.db.DBGroupMappingUserEntity;
import com.varsql.web.model.entity.db.QDBGroupMappingUserEntity;
import com.varsql.web.model.entity.user.QUserEntity;
import com.varsql.web.repository.DefaultJpaRepository;
import com.vartech.common.app.beans.SearchParameter;

@Repository
public interface DBGroupMappingUserEntityRepository extends DefaultJpaRepository, JpaRepository<DBGroupMappingUserEntity, Long> , JpaSpecificationExecutor<DBGroupMappingUserEntity> , DBGroupMappingUserEntityRepositoryCustom {
	
	void deleteByGroupIdAndViewid(String groupId, String viewid);
	
	public class DBGroupMappingUserEntityRepositoryCustomImpl extends QuerydslRepositorySupport implements DBGroupMappingUserEntityRepositoryCustom, DefaultJpaRepository {

		public DBGroupMappingUserEntityRepositoryCustomImpl() {
			super(DBGroupMappingUserEntity.class);
		}

		@Override
		public Page<UserResponseDTO> findByDbGroupUserList(String projectId, SearchParameter searchParameter, Pageable pageInfo) {
			QDBGroupMappingUserEntity qdbgme = QDBGroupMappingUserEntity.dBGroupMappingUserEntity;
			QUserEntity que = QUserEntity.userEntity;
			
			String keyworkd = searchParameter.getKeyword();
			List<UserResponseDTO> result = getUserList(projectId, pageInfo, keyworkd, qdbgme, que);
			
			JPQLQuery<Long> countQuery = getUserCount(projectId, pageInfo, keyworkd, qdbgme, que);

		    return PageableExecutionUtils.getPage(result, pageInfo, () -> countQuery.fetchOne());
			
		}
		/**
		 * 카운트 쿼리
		 * @param projectId
		 * @param pageInfo
		 * @param keyworkd
		 * @param qme
		 * @param que
		 * @return
		 */
		private JPQLQuery<Long> getUserCount(String projectId, Pageable pageInfo, String keyworkd, QDBGroupMappingUserEntity qme, QUserEntity que) {
			JPQLQuery<Long> countQuery = from(qme).innerJoin(que).on(qme.viewid.eq(que.viewid))
				.select(que.count())
				.where(qme.groupId.eq(projectId).andAnyOf(que.uname.like(contains(keyworkd)), que.uid.like(contains(keyworkd))));
				
			return countQuery;
		}
		
		/**
		 * 결과 쿼리
		 * @param projectId
		 * @param pageInfo
		 * @param keyworkd
		 * @param qme
		 * @param que
		 * @return
		 */
		private List<UserResponseDTO> getUserList(String projectId, Pageable pageInfo, String keyworkd, QDBGroupMappingUserEntity qme, QUserEntity que) {
        	ArrayList<UserResponseDTO> reval = new ArrayList<>();
        	
        	from(qme).innerJoin(que).on(qme.viewid.eq(que.viewid))
			.select(que.uname, que.viewid, que.uid)
			.where(qme.groupId.eq(projectId).andAnyOf(que.uname.like(contains(keyworkd)), que.uid.like(contains(keyworkd))))
			.offset(pageInfo.getOffset())
			.limit(pageInfo.getPageSize())
			.orderBy(que.uname.desc())
			.fetch().forEach(tuple->{
				reval.add(UserResponseDTO.builder()
			    	.uname(tuple.get(que.uname))
			    	.viewid(tuple.get(que.viewid))
			    	.uid(tuple.get(que.uid))
			    .build());
			});
			
			return reval; 
		}
	}
}

interface DBGroupMappingUserEntityRepositoryCustom{
	Page<UserResponseDTO> findByDbGroupUserList(String groupId, SearchParameter searchParameter,
			Pageable convertSearchInfoToPage);
}