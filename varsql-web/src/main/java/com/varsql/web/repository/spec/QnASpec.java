package com.varsql.web.repository.spec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.model.entity.user.QnAEntity;
import com.vartech.common.app.beans.SearchParameter;

/**
 * -----------------------------------------------------------------------------
 * 
 * @fileName : QnASpec.java
 * @desc : q & a specication
 * @author : ytkim
 *         -----------------------------------------------------------------------------
 *         DATE AUTHOR DESCRIPTION
 *         -----------------------------------------------------------------------------
 *         2020. 4. 27. ytkim 최초작성
 * 
 *         -----------------------------------------------------------------------------
 */
public class QnASpec extends DefaultSpec {

	public static Specification<QnAEntity> answerTypeSearch(SearchParameter param) {

		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			predicates.add(delYn( root ,cb));

			switch (param.getCategory()) {
			case "Y":
				predicates.add(root.get(QnAEntity.ANSWER_ID).isNotNull());
				break;
			case "N":
				predicates.add(root.get(QnAEntity.ANSWER_ID).isNull());
				break;
			default:
				break;
			}

			predicates.add(titleOrQuestion( root ,cb , param));

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public static Specification<QnAEntity> userQnaSearch(SearchParameter param) {
		
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			predicates.add(delYn( root ,cb));
			
			predicates.add(cb.equal(root.get(QnAEntity.REG_ID), SecurityUtil.userViewId()));
			
			predicates.add(titleOrQuestion( root ,cb , param));
			
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}	
	/**
	 * @method  : titleOrQuestion
	 * @desc : title or question search
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param root
	 * @param cb
	 * @param param
	 * @return
	 */
	private static Predicate titleOrQuestion(Root<?> root, CriteriaBuilder cb, SearchParameter param) {
		String keyword = param.getKeyword();
		return cb.or(
			cb.like(root.get(QnAEntity.TITLE), contains(keyword)),
			cb.like(root.get(QnAEntity.QUESTION), contains(keyword))
		);
	}
	
	/**
	 * @method  : delYn
	 * @desc : delyn != 'N'
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param root
	 * @param cb
	 * @return
	 */
	private static Predicate delYn(Root<?> root, CriteriaBuilder cb) {
		return cb.notEqual(root.get(QnAEntity.DEL_YN), "Y");
	}

}