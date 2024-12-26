package com.varsql.core.sql.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.mapping.ParameterMapping;
import com.varsql.core.sql.mapping.ParameterMode;
import com.varsql.core.sql.type.SQLDataType;
import com.vartech.common.utils.VartechUtils;

public final class SQLParamUtils {
	private SQLParamUtils() {}

	public static String functionValue (ParameterMapping param , Map reqParam) {
		String functionName = param.getFunctionName();
		
		switch (functionName) {
		case "uuid":
			return VartechUtils.generateUUID();
		case "currentDate":
			return VarsqlDateUtils.currentDateFormat();
		case "currentDateTime":
			return VarsqlDateUtils.currentDateTimeFormat();
			
		default:
			break;
		}
		
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

				if(param.getDataType()==null) {
					pstmt.setObject(i, objVal);
				}else {
					param.getDataType().setParameter(pstmt, i, objVal);
				}
			}
		}
	}

	public static void setCallableParameter(CallableStatement stmt, SqlSource tmpSqlSource) throws SQLException {
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

				if(param.getMode()== ParameterMode.OUT || param.getMode() == ParameterMode.INOUT) {
					if(param.getDataType() == null) {
						SQLDataType.OBJECT.setOutParameter(stmt, i);
					}else {
						param.getDataType().setOutParameter(stmt, i);
					}
				}else {
					if(param.getDataType()==null) {
						stmt.setObject(i, objVal);
					}else {
						param.getDataType().setParameter(stmt, i, objVal);
					}
				}
			}
		}
	}
}
