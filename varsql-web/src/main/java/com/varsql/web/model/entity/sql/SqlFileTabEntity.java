package com.varsql.web.model.entity.sql;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.converter.BooleanToYnConverter;
import com.varsql.web.model.id.SqlFileTabID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(SqlFileTabID.class)
@Table(name = SqlFileTabEntity._TB_NAME)
public class SqlFileTabEntity extends AbstractRegAuditorModel{
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
	@Convert(converter = BooleanToYnConverter.class)
	private boolean viewYn;

	@Builder
	public SqlFileTabEntity(String vconnid, String viewid, String prevSqlId, String sqlId, boolean viewYn) {
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