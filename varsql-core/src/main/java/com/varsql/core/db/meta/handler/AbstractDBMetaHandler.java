package com.varsql.core.db.meta.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.sql.ConstraintType;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : DBMetaHandlerImpl.java
 * @프로그램 설명 :  result meta 얻기.
 * @Date      : 2016. 11. 20.
 * @작성자      : ytkim
 * @변경이력 :
 */
public abstract class AbstractDBMetaHandler implements DBMetaHandler{
	/**
	 *
	 * @Method Name  : getColumnInfo
	 * @Method 설명 : 컬럼 정보 얻기.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 20.
	 * @변경이력  :
	 * @param rs
	 * @param dataTypeInfo
	 * @param keyColumn
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeFactory dataTypeFactory) throws SQLException{
		return getColumnInfo(rs, dataTypeFactory, null);
	}
	@Override
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeFactory dataTypeFactory, Set keyColumn) throws SQLException{
		return getColumnInfo(rs, dataTypeFactory,keyColumn , null);
	}
	@Override
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeFactory dataTypeFactory, Set keyColumn, Map colComment) throws SQLException{

		String cName=  rs.getString(MetaColumnConstants.COLUMN_NAME);
		String typeName = rs.getString(MetaColumnConstants.TYPE_NAME);

		int degitsLen = rs.getInt(MetaColumnConstants.DECIMAL_DIGITS);
		int columnSize = rs.getInt(MetaColumnConstants.COLUMN_SIZE);
		int dataPrecision = rs.getInt(MetaColumnConstants.DATA_PRECISION);
		
		DataType dataTypeInfo = dataTypeFactory.getDataType(typeName);

		ColumnInfo column = new ColumnInfo(); 
		column.setName(cName);
		column.setTypeCode(dataTypeInfo.getTypeCode());
		column.setTypeName(typeName);
		column.setLength(rs.getInt(MetaColumnConstants.COLUMN_SIZE));
		column.setDefaultVal(StringUtils.nullToString(rs.getString(MetaColumnConstants.COLUMN_DEF)));
		column.setNullable(StringUtils.nullToString(rs.getString(MetaColumnConstants.IS_NULLABLE)));
		column.setAutoincrement("");
		column.setTypeName(dataTypeInfo.getTypeName());
		column.setTypeAndLength(dataTypeInfo.getJDBCDataTypeMetaInfo().getTypeAndLength(typeName, dataTypeInfo, null, columnSize, dataPrecision, degitsLen));

		if(colComment != null){
			column.setComment(StringUtils.nullToString(colComment.get(cName)!=null? (String)colComment.get(cName) : rs.getString(MetaColumnConstants.REMARKS)));
		}

		if(keyColumn !=null){
			column.setConstraints(keyColumn.contains(cName)?ConstraintType.PRIMARY.getType():"");
		}
		return column;
	}
}