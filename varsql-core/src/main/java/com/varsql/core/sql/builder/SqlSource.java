package com.varsql.core.sql.builder;

import java.util.List;
import java.util.Map;

import com.varsql.core.sql.mapping.ParameterMapping;
import com.varsql.core.sql.type.SQLCommand;
import com.varsql.core.sql.type.SQLCommandType;

public class SqlSource {
	private SQLCommandType command;
	private VarsqlStatementType statementType;
	private String query;
	private String calluserid;
	private SqlSourceResultVO result;
	private List<ParameterMapping> paramList;
	private Map orginSqlParam;

	public SQLCommandType getCommand() {
		return this.command;
	}

	public void setCommand(SQLCommandType commandType) {

		this.command = commandType;
		this.statementType = VarsqlStatementType.PREPARED;

		if(SQLCommandType.CALL.equals(commandType.getCommand())){
			this.statementType = VarsqlStatementType.CALLABLE;
		}
	}

	public VarsqlStatementType getStatementType() {
		return statementType;
	}

	public void setStatementType(VarsqlStatementType statementType) {
		this.statementType = statementType;
	}

	public String getCalluserid() {
		return calluserid;
	}

	public void setCalluserid(String calluserid) {
		this.calluserid = calluserid;
	}

	public SqlSourceResultVO getResult() {
		return result;
	}

	public void setResult(SqlSourceResultVO result) {
		this.result = result;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<ParameterMapping> getParamList() {
		return paramList;
	}

	public void setParamList(List<ParameterMapping> paramList) {
		this.paramList = paramList;
	}

	public Map getOrginSqlParam() {
		return orginSqlParam;
	}

	public void setOrginSqlParam(Map orginSqlParam) {
		this.orginSqlParam = orginSqlParam;
	}

	@Override
	public String toString() {
		return new StringBuffer()
			.append("calluserid : [").append(calluserid)
			.append("] result : [").append( result)
			.append("] commandType : [").append(command)
			.append("] statementType : [").append(statementType)
			.append("] query : [").append(query)
			.append("] param : [").append(paramList)
			.append("]")
			.toString();
	}
}

