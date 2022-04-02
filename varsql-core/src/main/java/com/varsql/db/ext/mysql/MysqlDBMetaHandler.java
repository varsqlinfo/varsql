package com.varsql.db.ext.mysql;

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

/**
 *
 * @FileName  : MysqlDBMetaHandler.java
 * @프로그램 설명 :  result meta 얻기.
 * @Date      : 2019. 10. 27.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class MysqlDBMetaHandler extends AbstractDBMetaHandler{
	/**
	 *
	 * @Method Name  : getColumnInfo
	 * @Method 설명 : 컬럼 정보 얻기.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 20.
	 * @변경이력  :
	 * @param rs
	 * @param dataTypeFactory
	 * @param keyColumn
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeFactory dataTypeFactory,Set keyColumn , Map colComment) throws SQLException{
		ColumnInfo column = new ColumnInfo();

		String cName=  rs.getString(MetaColumnConstants.COLUMN_NAME);
		String dataType = rs.getString(MetaColumnConstants.DATA_TYPE);

		int degitsLen= rs.getInt(MetaColumnConstants.DECIMAL_DIGITS);
		int columnSize= rs.getInt(MetaColumnConstants.COLUMN_SIZE);
		DataType dataTypeInfo = dataTypeFactory.getDataType(dataType);

		column.setName(cName);
		column.setDataType(dataType);
		column.setLength(rs.getBigDecimal(MetaColumnConstants.COLUMN_SIZE));
		column.setDefaultVal(StringUtils.nullToString(rs.getString(MetaColumnConstants.COLUMN_DEF)));
		column.setNullable(StringUtils.nullToString(rs.getString(MetaColumnConstants.IS_NULLABLE)));
		column.setAutoincrement("");
		column.setTypeName(dataTypeInfo.getTypeName());
		
		column.setTypeAndLength(dataTypeInfo.getJDBCDataTypeMetaInfo().getTypeAndLength(dataTypeInfo, null, columnSize, degitsLen));
		
		if(colComment != null){
			column.setComment(StringUtils.nullToString(colComment.get(cName)!=null? (String)colComment.get(cName) : rs.getString(MetaColumnConstants.REMARKS)));
		}

		if(keyColumn !=null){
			column.setConstraints(keyColumn.contains(cName)?"PK":"");
		}
		return column;
	}

}