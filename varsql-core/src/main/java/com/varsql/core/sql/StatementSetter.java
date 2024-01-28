package com.varsql.core.sql;

import java.sql.SQLException;
import java.sql.Statement;

import com.varsql.core.sql.builder.SqlSource;

public interface StatementSetter {
	/**
	 *
	 * @Method Name  : setMaxRow
	 * @Method 설명 : row 갯수 셋팅
	 * @작성일   : 2015. 4. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param stmt
	 * @param maxRow
	 * @param tmpSqlSource 
	 * @throws SQLException
	 */
	public void setMaxRow(Statement stmt, int maxRow, SqlSource tmpSqlSource) throws SQLException;
}
