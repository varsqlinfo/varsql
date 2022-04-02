package com.varsql.db.ext.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.meta.handler.AbstractDBMetaHandler;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.vartech.common.utils.StringUtils;

public class OracleDBMetaHandler extends AbstractDBMetaHandler{
	@Override
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeFactory dataTypeFactory,Set keyColumn , Map colComment) throws SQLException{
		ColumnInfo column = new ColumnInfo();

		String cName=  rs.getString(MetaColumnConstants.COLUMN_NAME);
		int degitsLen= rs.getInt(MetaColumnConstants.DECIMAL_DIGITS);
		int columnSize= rs.getInt(MetaColumnConstants.COLUMN_SIZE);
		String dataType = rs.getString(MetaColumnConstants.DATA_TYPE);
		DataType dataTypeInfo = dataTypeFactory.getDataType(dataType);

		column.setName(cName);
		column.setDataType(dataType);
		column.setLength(columnSize);
		column.setDefaultVal(StringUtils.nullToString(rs.getString(MetaColumnConstants.COLUMN_DEF)));
		column.setNullable(StringUtils.nullToString(rs.getString(MetaColumnConstants.IS_NULLABLE)));
		column.setAutoincrement("");
		column.setTypeName(dataTypeInfo.getTypeName());
		column.setTypeAndLength(dataTypeInfo.getJDBCDataTypeMetaInfo().getTypeAndLength(dataTypeInfo, null, columnSize, degitsLen));

		if(colComment != null){
			column.setComment(StringUtils.nullToString(colComment.get(cName)!=null? (String)colComment.get(cName) : rs.getString(MetaColumnConstants.REMARKS)));
		}

		column.setConstraints(keyColumn.contains(cName)?"PK":"");

		return column;

	}
}