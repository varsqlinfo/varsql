package com.varsql.web.app.database.tools.export;

import org.springframework.stereotype.Repository;

import com.varsql.web.app.database.bean.PreferencesInfo;
import com.varsql.web.dao.BaseDAO;
import com.vartech.common.app.beans.ParamMap;

@Repository
public class ExportDAO extends BaseDAO{
	public int insertUserTableExportInfo(ParamMap paramMap){
		return getSqlSession().insert("userPreferencesMapper.insertSqlUserLog", paramMap );
	}
}
