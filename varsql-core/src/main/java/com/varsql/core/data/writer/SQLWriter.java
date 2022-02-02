package com.varsql.core.data.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.db.DBType;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.util.SQLUtils;
import com.vartech.common.io.writer.AbstractWriter;

/**
 *
 * @FileName  : SQLWriter.java
 * @프로그램 설명 : sql writer
 * @Date      : 2020. 12. 2.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class SQLWriter extends AbstractWriter{
	private BufferedWriter writer;
	private String tableName;
	private DBType dbytpe;
	private List<GridColumnInfo> columnInfos;

	public SQLWriter(OutputStream output, DBType dbytpe ,String tableName) throws IOException {
		this(output, dbytpe, tableName, VarsqlConstants.CHAR_SET);
	}

	public SQLWriter(OutputStream output, DBType dbytpe, String tableName, String charset) throws IOException {
		this(output, dbytpe, tableName , charset, null);
	}
	public SQLWriter(OutputStream output, DBType dbytpe, String tableName, String charset, List<GridColumnInfo> columnInfos) throws IOException {
		this.dbytpe = dbytpe;
		this.tableName = tableName;
		this.columnInfos = columnInfos;
		this.writer = new BufferedWriter(new OutputStreamWriter(output,VarsqlConstants.CHAR_SET));
	}

	private boolean firstFlag = true;
	private String insertQueryPrefix;

	@SuppressWarnings({ "rawtypes" })
	@Override
	public <E> void addRow(Object addRow) throws IOException {
		Map rowValue = (Map)addRow;

		if(firstFlag) {

			StringBuilder columnSb = new StringBuilder();
			if(columnInfos == null) {
				columnInfos = new LinkedList<GridColumnInfo>();
				Object[] keyArr = rowValue.keySet().toArray();
				int keyLen = keyArr.length;
				for (int i= 0; i <keyLen; i++) {
					GridColumnInfo gci = new GridColumnInfo();
					String key = keyArr[i].toString();
					Object colVal = rowValue.get(keyArr[i]);

					gci.setKey(key);
					gci.setLabel(key);

					if(colVal instanceof Number) {
						gci.setNumber(true);
					}else {
						gci.setNumber(false);
					}
					columnInfos.add(gci);
				}
			}

			for (int i = 0; i < columnInfos.size(); i++) {
				GridColumnInfo columnInfo = columnInfos.get(i);
				columnSb.append(i==0? "" : ", ").append(columnInfo.getLabel());
			}

			insertQueryPrefix = String.format("insert into %s(%s) values (", tableName ,columnSb.toString());
			firstFlag = false;
		}
		writer.write(insertQueryPrefix);

		StringBuilder valueSb = new StringBuilder();

		for (int i = 0; i < columnInfos.size(); i++) {
			GridColumnInfo columnInfo = columnInfos.get(i);
			Object colVal = rowValue.get(columnInfo.getKey());

			valueSb.append(i==0? "" : ", ");

			if(colVal==null) {
				valueSb.append("null");
			}else {
				if(columnInfo.isNumber()) {
					valueSb.append("".equals(colVal)?null:colVal);
				}else {
					if(columnInfo.isLob()) {
						valueSb.append("null");
					}else {
						valueSb.append("'").append(SQLUtils.escapeValue(colVal)).append("'");;
					}
				}
			}
		}

		writer.write(valueSb.toString());
		writer.write(");");
		writer.newLine();

		addRowIdx();
	}

	public List<GridColumnInfo> getColumnInfos() {
		return columnInfos;
	}

	public void setColumnInfos(List<GridColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	}

	@Override
	public void write() throws IOException {
		writer.flush();
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

}
