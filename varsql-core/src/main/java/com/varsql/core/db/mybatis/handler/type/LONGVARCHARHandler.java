package com.varsql.core.db.mybatis.handler.type;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedJdbcTypes(JdbcType.LONGNVARCHAR)
public class LONGVARCHARHandler implements TypeHandler{
 
    @Override
    // 파라메터 셋팅할때
    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
           throws SQLException {
        String s = (String) parameter;
        try(StringReader reader = new StringReader(s)){
        	ps.setCharacterStream(i, reader, s.length());
        }catch(SQLException e) {
        	throw e; 
        }
        
    }
 
    @Override
    // Statement 로 SQL 호출해서 ResultSet 으로 컬럼값을 읽어올때
    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }
 
    @Override
    // CallableStatement 로 SQL 호출해서 컬럼값 읽어올때
    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }

	@Override
	public Object getResult(ResultSet arg0, int arg1) throws SQLException {
		return arg0.getObject(arg1);
	}
}
