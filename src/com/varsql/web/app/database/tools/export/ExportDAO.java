package com.varsql.web.app.database.tools.export;

import org.springframework.stereotype.Repository;

import com.varsql.web.app.database.beans.PreferencesInfo;
import com.varsql.web.dao.BaseDAO;

@Repository
public class ExportDAO extends BaseDAO{
	public int insertUserTableExportInfo(PreferencesInfo preferencesInfo){
		return getSqlSession().insert("userPreferencesMapper.insertUserTableExportInfo", preferencesInfo );
	}
}
