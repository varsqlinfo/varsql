package com.varsql.web.repository.spec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.board.BoardEntity;
import com.vartech.common.app.beans.SearchParameter;

public class BoardSpec extends DefaultSpec {

	public static Specification<BoardEntity> boardSearch(String boardCode, SearchParameter param) {
		
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			predicates.add(cb.equal(root.get(BoardEntity.BOARD_CODE), boardCode));
			
			titleOrContents(predicates, root, cb, param);
						
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}	
	/**
	 * @method  : titleOrContents
	 * @desc : title or contents search
	 * @author   : ytkim
	 * @param predicates 
	 * @date   : 2020. 4. 27. 
	 * @param root
	 * @param cb
	 * @param param
	 * @return
	 */
	private static void titleOrContents(List<Predicate> predicates, Root<?> root, CriteriaBuilder cb, SearchParameter param) {
		String keyword = param.getKeyword();
		
		if(keyword != null && !"".equals(keyword)) {
			predicates.add(cb.or(
				cb.like(root.get(BoardEntity.TITLE), contains(keyword)),
				cb.like(root.get(BoardEntity.CONTENTS), contains(keyword))
			));
		}
	}
	
	

}