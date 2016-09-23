package com.varsql.web.app.manager.qna;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class QnaDAO extends BaseDAO{
	
	public int selectQnaMgmtTotalCnt(DataCommonVO paramMap) {
		System.out.println("------------------");
		System.out.println("paramMap : "+ paramMap);
		System.out.println("------------------");
		return getSqlSession().selectOne("selectQnaMgmtTotalCnt", paramMap);
	}
	
	public List<Object> selectQnaMgmtList(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectQnaMgmtList", paramMap);
	}

	public int updateQnaAnswerContent(DataCommonVO paramMap){
		return getSqlSession().update("updateQnaAnswerContent", paramMap);
	}

}
