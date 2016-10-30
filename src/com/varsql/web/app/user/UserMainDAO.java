package com.varsql.web.app.user;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;
import com.varsql.web.util.VarsqlUtil;


@Repository
public class UserMainDAO extends BaseDAO{
	
	public int selectdbListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectdbListTotalCnt", paramMap);
	}
	
	public List<Object> selectdbList(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectdbList", paramMap);
	}

	public int updateQnaAnswerContent(DataCommonVO paramMap){
		return getSqlSession().update("updateQnaAnswerContent", paramMap);
	}
	
	public int selectDbUserMappingListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectDbUserMappingListTotalCnt", paramMap);
	}
	
	public List<Object>  selectDbUserMappingList(DataCommonVO paramMap){
		return getSqlSession().selectList("selectDbUserMappingList", paramMap);
	}
	
	public List selectSearchUserList(DataCommonVO paramMap) {
		return getSqlSession().selectList("sqlServiceMapper.selectSearchUserList", paramMap );
	}
	
	public int insertSendSqlInfo(DataCommonVO paramMap) {
		SqlSession batch = getSqlSession(true);
		String [] recvArr = paramMap.getString("recv_id").split(";;");
		
		try{
			for (int i = 0; i < recvArr.length; i++) {
				paramMap.put("memo_id", VarsqlUtil.generateUUID());
				paramMap.put("recv_id", recvArr[i]);
				batch.insert("sqlServiceMapper.insertSendSqlInfo", paramMap );
			}
			batch.flushStatements();
			batch.commit(true);
		}finally{
			batch.close();
		}
		
		return 0; 
	}
}
