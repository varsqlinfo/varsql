package com.varsql.db.ext.sqlserver;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.varsql.core.sql.AbstractStatementSetter;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.type.SQLCommandType;

public class SqlserverStatementSetter extends AbstractStatementSetter {
	@Override
	public void setMaxRow(Statement stmt, int maxRow, SqlSource tmpSqlSource) throws SQLException {
		if(tmpSqlSource.getCommand().isSelectCommand()) {
			Matcher  m = Pattern.compile(".*into\\s.*").matcher(tmpSqlSource.getQuery().toLowerCase());
			
			if(!m.find()) {
				stmt.setMaxRows(maxRow);
			}
		}else if(SQLCommandType.OTHER.equals( tmpSqlSource.getCommand())) {
			stmt.setMaxRows(maxRow);
		}
	}
}
