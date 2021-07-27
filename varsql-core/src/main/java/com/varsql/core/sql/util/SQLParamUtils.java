package com.varsql.core.sql.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.mapping.ParameterMapping;

public final class SQLParamUtils {
	private SQLParamUtils() {}

	public static String functionValue (ParameterMapping param , Map reqParam) {
		return "";
	}
	
	public static void setSqlParameter(PreparedStatement pstmt, SqlSource tmpSqlSource) throws SQLException {
		List<ParameterMapping> paramList= tmpSqlSource.getParamList();

		if(paramList != null){
			Map orginParamMap = tmpSqlSource.getOrginSqlParam();

			for(int i =1 ;i <= paramList.size() ;i++){
				ParameterMapping param = paramList.get(i-1);
				Object objVal;
				if(param.isFunction()) {
					objVal = SQLParamUtils.functionValue(param, orginParamMap);
				}else {
					objVal = orginParamMap.get(param.getProperty());
				}

				if(param.getJdbcType()==null) {
					pstmt.setObject(i, objVal);
				}else {
					param.getJdbcType().setParameter(pstmt, i, objVal);
				}
			}
		}
	}
}
