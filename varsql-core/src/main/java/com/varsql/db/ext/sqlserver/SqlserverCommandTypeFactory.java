package com.varsql.db.ext.sqlserver;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.sql.type.AbstractCommandTypeFactory;
import com.varsql.core.sql.type.SQLCommand;
import com.varsql.core.sql.type.SQLCommandType;
import com.vartech.common.utils.StringUtils;

/**
 * 
 * @FileName  : SqlserverCommandTypeFactory.java
 * @프로그램 설명 : sqlserver command type
 * @Date      : 2022. 3. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class SqlserverCommandTypeFactory extends AbstractCommandTypeFactory{
	
	public SqlserverCommandTypeFactory() {
		
		addCommandType(new SQLCommand() {
			@Override
			public SQLCommandType getCommand() {
				return SQLCommandType.MERGE;
			}
			
			@Override
			public String checkSql(String sql) {
				sql = StringUtils.trim(sql);
				return sql.endsWith(VarsqlConstants.SEMICOLON) ? sql : sql+VarsqlConstants.SEMICOLON; 
			}

			
		});
		
	}
}
