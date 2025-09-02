package com.varsql.db.ext.sqlserver;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.binary.Hex;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DataExceptionReturnType;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeConfigInfo;
import com.varsql.core.db.datatype.DataTypeHandler;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;
import com.varsql.core.db.datatype.handler.ResultSetHandler;
import com.varsql.core.db.meta.MetaBeanConfig;
import com.varsql.db.ext.oracle.OracleDBMeta;

/**
 * sqlserver data type
 * 
 * @author ytkim
 *
 */
@MetaBeanConfig(dbVenderType = DBVenderType.SQLSERVER, metaBean = OracleDBMeta.class, ddlBean = SqlserverDDLScript.class, dataTypeBean = SqlserverDataTypeFactory.class, statementSetterBean = SqlserverStatementSetter.class, commandTypeBean = SqlserverCommandTypeFactory.class)
public class SqlserverDataTypeFactory extends AbstractDataTypeFactory{
	
	public SqlserverDataTypeFactory() {
		
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TEXT").typeCode(DefaultDataType.CLOB.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TEXT).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("NTEXT").typeCode(DefaultDataType.NCLOB.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TEXT).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("DATETIME2").typeCode(DefaultDataType.DATETIME.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.DATE).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("DATETIMEOFFSET").typeCode(DefaultDataType.TIMESTAMP_WITH_TIMEZONE.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIMESTAMP).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIMESTAMP").typeCode(DefaultDataType.BINARY.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER)
			.dataTypeHandler(DataTypeHandler.builder().resultSetHandler(new ResultSetHandler() {
				@Override
				public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
					byte[] val = rs.getBytes(columnIndex);
					
					if(val == null) return null;
					
					try {
						return Hex.encodeHex(val);
					}catch(Exception e) {
						return rs.getObject(columnIndex).toString();
					}
				}
			}).build())
			.excludeImportColumn(true).build()));
		
		
	}
}
