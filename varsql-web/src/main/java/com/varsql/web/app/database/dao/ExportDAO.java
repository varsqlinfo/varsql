package com.varsql.web.app.database.dao;

import org.springframework.stereotype.Repository;

import com.varsql.web.app.database.beans.PreferencesInfo;
import com.varsql.web.common.dao.BaseDAO;

@Repository
public class ExportDAO extends BaseDAO{
	public int insertUserTableExportInfo(PreferencesInfo preferencesInfo){
		return getSqlSession().insert("userPreferencesMapper.insertUserTableExportInfo", preferencesInfo );
	}
}
