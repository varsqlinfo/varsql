package com.varsql.web.repository.spec;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.core.auth.AuthorityType;
import com.varsql.web.model.entity.user.UserEntity;

/**
 * -----------------------------------------------------------------------------
* @fileName		: UserSpec.java
* @desc		: user model specification
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class UserSpec extends DefaultSpec{

	public static Specification<UserEntity> getUserRole(AuthorityType userAuth ) {
    	return (root, query, criteriaBuilder) -> {
    		Predicate predicate = criteriaBuilder.equal(root.get(UserEntity.USER_ROLE), userAuth.name());
    		return predicate;
    	};
    }
	
	public static Specification<UserEntity> userRoleNotIn(boolean isAdmin) {
		return (root, query, criteriaBuilder) -> {
			if(isAdmin) {
				return criteriaBuilder.not(root.get(UserEntity.USER_ROLE).in(AuthorityType.ADMIN.name()));
			}else {
				return criteriaBuilder.not(root.get(UserEntity.USER_ROLE).in(AuthorityType.ADMIN.name(), AuthorityType.MANAGER.name()));
			}
		};
	}

    public static Specification<UserEntity> getUnameOrUid(String keyword) {
        return (root, query, criteriaBuilder) -> {
        	List<Predicate> predicate = new ArrayList<>();

        	predicate.add(criteriaBuilder.like(root.get(UserEntity.UNAME),contains(keyword)));
        	predicate.add(criteriaBuilder.like(root.get(UserEntity.UID),contains(keyword)));

            return criteriaBuilder.or(predicate.toArray(new Predicate[0]));
        };
    }

    public static Specification<UserEntity> getUid(String uid) {
    	return (root, query, criteriaBuilder) -> {
    		Predicate predicate = criteriaBuilder.equal(root.get(UserEntity.UID), uid);
    		return predicate;
    	};
    }

    public static Specification<UserEntity> likeUnameOrUid(AuthorityType userAuth, String name) {
        return Specification.where(getUserRole(userAuth)).and(getUnameOrUid(name));
    }
    
    public static Specification<UserEntity> likeUnameOrUid(boolean isAdmin, String name) {
    	return Specification.where(userRoleNotIn(isAdmin)).and(getUnameOrUid(name));
    }

    public static Specification<UserEntity> detailInfo(String uid) {
    	return Specification.where(getUid(uid));
    }
}