package com.varsql.web.app.user;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;
import com.varsql.web.util.VarsqlUtil;
import com.vartech.common.app.beans.ParamMap;


@Repository
public class UserMainDAO extends BaseDAO{
	
	public int selectdbListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("userMapper.selectdbListTotalCnt", paramMap);
	}
	
	public List<Object> selectdbList(DataCommonVO paramMap) {
		return getSqlSession().selectList("userMapper.selectdbList", paramMap);
	}

	public int updateQnaAnswerContent(DataCommonVO paramMap){
		return getSqlSession().update("userMapper.updateQnaAnswerContent", paramMap);
	}
	
	public int selectDbUserMappingListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("userMapper.selectDbUserMappingListTotalCnt", paramMap);
	}
	
	public List<Object>  selectDbUserMappingList(DataCommonVO paramMap){
		return getSqlSession().selectList("userMapper.selectDbUserMappingList", paramMap);
	}
	
	public List selectSearchUserList(ParamMap paramMap) {
		return getSqlSession().selectList("userMapper.selectSearchUserList", paramMap );
	}
	
	public int insertSendSqlInfo(ParamMap paramMap) {
		SqlSession batch = getSqlSession(true);
		String [] recvArr = paramMap.getString("recv_id").split(";;");
		
		try{
			for (int i = 0; i < recvArr.length; i++) {
				paramMap.put("memo_id", VarsqlUtil.generateUUID());
				paramMap.put("recv_id", recvArr[i]);
				batch.insert("userMapper.insertSendSqlInfo", paramMap );
			}
			batch.flushStatements();
			batch.commit(true);
		}finally{
			batch.close();
		}
		
		return 0; 
	}

	public List selectMessageInfo(ParamMap paramMap) {
		return getSqlSession().selectList("userMapper.selectMessageInfo", paramMap );
	}
	
	public int updateMemoViewDate(ParamMap paramMap) {
		return getSqlSession().update("userMapper.updateMemoViewDate", paramMap);
	}
}
