package com.varsql.web.repository.spec;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.app.GlossaryEntity;
import com.varsql.web.model.entity.app.QnAEntity;
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
public class GlossarySpec extends DefaultSpec{


    public static Specification<GlossaryEntity> searchField(SearchParameter param) {
    	String keyword = param.getKeyword();
    	
        return (root, query, cb) -> {
        	return cb.or(
	    		cb.like(root.get(GlossaryEntity.WORD),contains(keyword))
	    		,cb.like(root.get(GlossaryEntity.WORD_EN),contains(keyword))
	    	);
        };
        
    }

}