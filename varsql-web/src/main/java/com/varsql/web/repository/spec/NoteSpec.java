package com.varsql.web.repository.spec;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.app.NoteEntity;
import com.varsql.web.model.entity.app.NoteMappingUserEntity;

public class NoteSpec extends DefaultSpec {

	 // 권한 있는 db connection 정보 보기.
    public static Specification<NoteEntity> userNoteList(String viewid) {
    	return Specification.where(recvUser(viewid)).and(viewDt()).and(viewDt());
    }
	
	 private static Specification<NoteEntity> viewDt() {
		 return (root, query, cb) -> {
			 Predicate predicate = cb.isNotNull(root.get(NoteMappingUserEntity.VIEW_DT));
	    		return predicate;
		};
	}


	// manager 권한을 가진 db
    private static Specification<NoteEntity> recvUser(String viewid) {
    	return (root, query, cb) -> {
    		Join<NoteEntity, NoteMappingUserEntity> join = root.join(NoteEntity.JOIN_RECV_LIST);
    		return cb.equal(join.get(NoteMappingUserEntity.RECV_ID), viewid);
    	};
    }
}