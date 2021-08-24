package com.varsql.core.data.importdata.handler;

import java.sql.SQLException;
import java.util.Map;

public interface ImportDataHandler{
	public void handler(Map hashMap) throws SQLException;
}
