package com.varsql.web.repository.spec;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.app.NoteEntity;
import com.varsql.web.model.entity.app.NoteMappingUserEntity;

public class NoteSpec extends DefaultSpec {

	 // 권한 있는 db connection 정보 보기.
    public static Specification<NoteEntity> userNoteList(String viewid) {
    	return Specification.where(recvUser(viewid));
    }
	
	// manager 권한을 가진 db
    private static Specification<NoteEntity> recvUser(String viewid) {
    	return (root, query, cb) -> {
    		Join<NoteEntity, NoteMappingUserEntity> join = root.join(NoteEntity.JOIN_RECV_LIST);
    		
    		return cb.and(
				cb.equal(join.get(NoteMappingUserEntity.RECV_ID), viewid)
	    		,cb.isNotNull(join.get(NoteMappingUserEntity.VIEW_DT))
	    	);
    	};
    }
}