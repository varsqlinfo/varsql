package com.varsql.web.repository.spec;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.EntityValueConstants;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBGroupMappingDbEntity;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DBGroupMappingDbSpec.java
* @desc		: 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 5. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class DBGroupMappingDbSpec extends DefaultSpec{

	// 그룹에 속한  db 정보 보기.
    public static Specification<DBGroupMappingDbEntity> dbGroupConnList(String groupId) {
    	return Specification.where(joinVconnid()).and(groupId(groupId));
    }
    
	private static Specification<DBGroupMappingDbEntity> groupId(String groupId) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.equal(root.get(DBGroupMappingDbEntity.GROUP_ID),  groupId);
			return predicate;
		};
	}

    private static Specification<DBGroupMappingDbEntity> joinVconnid() {
    	return (root, query, cb) -> {
    		root.fetch(DBGroupMappingDbEntity.JOIN_CONNINFO);
    		//Join<DBGroupMappingDbEntity, DBConnectionEntity> join = root.join(DBGroupMappingDbEntity.JOIN_CONNINFO);
    		return cb.equal(root.get(DBGroupMappingDbEntity.JOIN_CONNINFO).get(DBConnectionEntity.USE_YN), EntityValueConstants.YN.Y.name());
    	};
    }
}