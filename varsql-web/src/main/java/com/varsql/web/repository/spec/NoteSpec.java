package com.varsql.web.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.EntityFieldConstants;
import com.varsql.web.model.entity.app.NoteEntity;
import com.varsql.web.model.entity.app.NoteMappingUserEntity;

public class NoteSpec extends DefaultSpec {

	 // 권한 있는 db connection 정보 보기.
    public static Specification<NoteEntity> userNoteList(String viewid) {
    	return Specification.where(recvUser(viewid)).and(delYn());
    }
    
    public static Specification<NoteEntity> sendMsg(String viewid ,String keyword) {
    	return Specification.where(sendMsgUser(viewid, keyword)).and(delYn());
    }
    
    public static Specification<NoteEntity> recvMsg(String viewid ,String keyword) {
    	return Specification.where(recvUser(viewid)).and(delYn());
    }
    
    
    public static Specification<NoteEntity> findUserReply(String parentNoteId) {
    	return Specification.where(delYn()).and(replyList(parentNoteId));
    }
    
	private static Specification<NoteEntity> replyList(String parentNoteId) {
		return (root, query, cb) -> {
    		return cb.equal(root.get(NoteEntity.PARENT_NOTE_ID) , parentNoteId);
    	};
	}

	private static Specification<NoteEntity> sendMsgUser(String viewid, String keyword) {
		return (root, query, cb) -> {
    		Join<NoteEntity, NoteMappingUserEntity> join = root.join(NoteEntity.JOIN_RECV_LIST , JoinType.LEFT);
    		
    		return cb.and(
    			cb.equal(root.get(EntityFieldConstants.REG_ID) , viewid)
    			,cb.like(root.get(NoteEntity.NOTE_TITLE), contains(keyword))
	    	);
    	};
	}
	
	// manager 권한을 가진 db
    private static Specification<NoteEntity> recvUser(String viewid) {
    	return (root, query, cb) -> {
    		Join<NoteEntity, NoteMappingUserEntity> join = root.join(NoteEntity.JOIN_RECV_LIST);
    		
    		return cb.and(
				cb.equal(join.get(NoteMappingUserEntity.RECV_ID), viewid)
	    		, cb.isNotNull(join.get(NoteMappingUserEntity.VIEW_DT))
	    	);
    	};
    }
    
    private static Specification<NoteEntity> delYn() {
    	return (root, query, cb) -> {
    		return cb.isFalse(root.get(NoteEntity.DEL_YN));
    	};
	}
}