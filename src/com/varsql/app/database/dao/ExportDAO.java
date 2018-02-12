package com.varsql.app.database.dao;

import org.springframework.stereotype.Repository;

import com.varsql.app.dao.BaseDAO;
import com.varsql.app.database.beans.PreferencesInfo;

@Repository
public class ExportDAO extends BaseDAO{
	public int insertUserTableExportInfo(PreferencesInfo preferencesInfo){
		return getSqlSession().insert("userPreferencesMapper.insertUserTableExportInfo", preferencesInfo );
	}
}
