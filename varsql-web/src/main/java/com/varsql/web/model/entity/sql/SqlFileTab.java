package com.varsql.web.model.entity.sql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.varsql.web.model.base.AbstractRegAuditorModel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
@Entity
@Table(name = SqlFileTab._TB_NAME)
public class SqlFileTab extends AbstractRegAuditorModel{
	public final static String _TB_NAME="VTSQL_SQLFILE_TAB";
	
	@Id
	private String vconnid;
	
	@Id
	private String viewid;
	
	@Id
	private String sqlId;
	
	@Column(name ="PREV_SQL_ID")
	private String prevSqlId;
	
	@Column(name ="VIEW_YN")
	private char viewYn;

	@Builder
	public SqlFileTab(String vconnid, String viewid, String prevSqlId, String sqlId, char viewYn) {
		this.vconnid = vconnid;
		this.viewid = viewid;
		this.prevSqlId = prevSqlId;
		this.sqlId = sqlId;
		this.viewYn = viewYn;

	}
	public final static String VCONNID="vconnid";

	public final static String VIEWID="viewid";

	public final static String PREV_SQL_ID="prevSqlId";

	public final static String SQL_ID="sqlId";

	public final static String VIEW_YN="viewYn";

}