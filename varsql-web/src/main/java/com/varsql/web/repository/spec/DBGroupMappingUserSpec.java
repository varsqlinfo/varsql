package com.varsql.web.repository.spec;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.db.DBGroupMappingUserEntity;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DBGroupMappingUserSpec.java
* @desc		: 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 5. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class DBGroupMappingUserSpec extends DefaultSpec{

	// 그룹에 속한  사용자  정보 보기.
    public static Specification<DBGroupMappingUserEntity> dbGroupUserList(String groupId) {
    	return Specification.where(joinViewid()).and(groupId(groupId));
    }
    
    
    
	private static Specification<DBGroupMappingUserEntity> groupId(String groupId) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.equal(root.get(DBGroupMappingUserEntity.GROUP_ID),  groupId);
			return predicate;
		};
	}

    private static Specification<DBGroupMappingUserEntity> joinViewid() {
    	return (root, query, cb) -> {
    		root.fetch(DBGroupMappingUserEntity.JOIN_USERINFO);
    		return cb.equal(cb.literal(1), 1);
    	};
    }
}