package com.varsql.core.db.datatype;

import com.varsql.core.db.datatype.handler.MetaDataHandler;
import com.varsql.core.db.datatype.handler.ResultSetHandler;
import com.varsql.core.db.datatype.handler.StatementHandler;

public interface DataType {
	
	public int getTypeCode();
	
	public String getTypeName();
	
	public boolean isExcludeImportColumn();
	
	public DBColumnMetaInfo getJDBCDataTypeMetaInfo();
	
	public StatementHandler getStatementHandler();
	
	public ResultSetHandler getResultSetHandler();
	
	public MetaDataHandler getMetaDataHandler();
	
}
