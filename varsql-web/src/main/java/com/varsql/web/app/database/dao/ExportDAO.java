package com.varsql.web.app.database.dao;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.dao.BaseDAO;
import com.varsql.web.dto.user.PreferencesRequestDTO;

@Repository
public class ExportDAO extends BaseDAO{
	public int insertUserTableExportInfo(PreferencesRequestDTO preferencesInfo){
		return getSqlSession().insert("userPreferencesMapper.insertUserTableExportInfo", preferencesInfo );
	}
}
