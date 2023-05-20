package com.varsql.core.db.datatype;

import com.varsql.core.db.datatype.handler.MetaDataHandler;
import com.varsql.core.db.datatype.handler.ResultSetHandler;
import com.varsql.core.db.datatype.handler.StatementHandler;

public interface DataType {
	
	final int VARCHAR_DEFAULT_SIZE = 512;
	
	public int getTypeCode();
	
	public String getTypeName();
	
	public int getDefaultSize();
	
	public boolean isExcludeImportColumn();
	
	public DBColumnMetaInfo getJDBCDataTypeMetaInfo();
	
	public StatementHandler getStatementHandler();
	
	public ResultSetHandler getResultSetHandler();
	
	public MetaDataHandler getMetaDataHandler();
	
}
