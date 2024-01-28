package com.varsql.core.sql;

import java.sql.SQLException;
import java.sql.Statement;

import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.type.SQLCommandType;

public abstract class AbstractStatementSetter implements StatementSetter{

	@Override
	public void setMaxRow(Statement stmt, int maxRow, SqlSource tmpSqlSource) throws SQLException {
		if(tmpSqlSource.getCommand().isSelectCommand()) {
			stmt.setMaxRows(maxRow);
		}else if(SQLCommandType.OTHER.equals( tmpSqlSource.getCommand())) {
			stmt.setMaxRows(maxRow);
		}
		
	}

}
