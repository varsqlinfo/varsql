package com.varsql.web.app.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;
import com.varsql.web.util.VarsqlUtil;

@Repository
public class SQLDAO extends BaseDAO{
	public int insertSqlUserLog(DataCommonVO paramMap){
		return getSqlSession().insert("sqlServiceMapper.insertSqlUserLog", paramMap );
	}

	public int saveQueryInfo(DataCommonVO paramMap) {
		return getSqlSession().insert("sqlServiceMapper.saveQueryInfo", paramMap );
	}

	public int updateQueryInfo(DataCommonVO paramMap) {
		return getSqlSession().update("sqlServiceMapper.updateQueryInfo", paramMap );
	}

	public Map selectLastSqlInfo(DataCommonVO paramMap) {
		return getSqlSession().selectOne("sqlServiceMapper.selectLastSqlInfo", paramMap );
	}

	public int selectSqlListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("sqlServiceMapper.selectSqlListTotalCnt", paramMap );
	}
	
	public List selectSqlList(DataCommonVO paramMap) {
		return getSqlSession().selectList("sqlServiceMapper.selectSqlList", paramMap );
	}
	
	public List selectSearchUserList(DataCommonVO paramMap) {
		return getSqlSession().selectList("sqlServiceMapper.selectSearchUserList", paramMap );
	}

	public int deleteSqlSaveInfo(DataCommonVO paramMap) {
		return getSqlSession().delete("sqlServiceMapper.deleteSqlSaveInfo", paramMap );
	}

	public int insertSendSqlInfo(DataCommonVO paramMap) {
		SqlSession batch = getSqlSession(true);
		String [] recvArr = paramMap.getString("recv_id").split(";;");
		
		batch.commit(false);
		
		for (int i = 0; i < recvArr.length; i++) {
			paramMap.put("memo_id", VarsqlUtil.generateUUID());
			paramMap.put("recv_id", recvArr[i]);
			
			System.out.println(paramMap);
			batch.insert("sqlServiceMapper.insertSendSqlInfo", paramMap );
		}
		batch.flushStatements();
		
		batch.commit(true);
		return 0; 
	}
}
