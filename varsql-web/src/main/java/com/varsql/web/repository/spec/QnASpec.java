package com.varsql.web.repository.spec;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.user.QnAEntity;
import com.vartech.common.app.beans.SearchParameter;

/**
 * -----------------------------------------------------------------------------
* @fileName		: QnASpec.java
* @desc		: q & a specication 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 27. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class QnASpec extends DefaultSpec{


    public static Specification<QnAEntity> searchField(SearchParameter param) {
    	String keyword = param.getKeyword();
    	
        return (root, query, cb) -> {
        	List<Predicate> predicates = new ArrayList<>();
        	
        	predicates.add(cb.notEqual(root.get(QnAEntity.DEL_YN), "N"));
        	
        	switch (param.getCategory()) {
	            case "Y":
	            	predicates.add(root.get(QnAEntity.ANSWER_ID).isNotNull());
	            	break; 
	            case "N":
	            	predicates.add(root.get(QnAEntity.ANSWER_ID).isNull());
	            	break;
	            default :
	            	break; 
	        }
        	
        	predicates.add(cb.or(
	    		cb.like(root.get(QnAEntity.TITLE),contains(keyword))
	    		,cb.like(root.get(QnAEntity.QUESTION),contains(keyword))
	    	));
        	
        	return cb.and(predicates.toArray(new Predicate[0]));
        };
        
    }

}