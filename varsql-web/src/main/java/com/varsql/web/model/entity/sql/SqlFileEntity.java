package com.varsql.web.model.entity.sql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.varsql.web.model.base.AabstractAuditorModel;
import com.varsql.web.model.id.generator.AppUUIDGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
@Entity
@Table(name = SqlFileEntity._TB_NAME)
public class SqlFileEntity extends AabstractAuditorModel{
	public final static String _TB_NAME="VTSQL_SQLFILE";
	
	@Id
	@GenericGenerator(name = "sqlIdGenerator"
		, strategy = "com.varsql.web.model.id.generator.AppUUIDGenerator"
		, parameters = @Parameter(
            name = AppUUIDGenerator.PREFIX_PARAMETER,
            value = "SQ"
		)
	)
    @GeneratedValue(generator = "sqlIdGenerator")
	@Column(name ="SQL_ID")
	private String sqlId;

	@Column(name ="VCONNID")
	private String vconnid;

	@Column(name ="VIEWID")
	private String viewid;

	@Column(name ="GUERY_TITLE")
	private String gueryTitle;

	@Column(name ="QUERY_CONT")
	private String queryCont;

	@Column(name ="SQL_PARAM")
	private String sqlParam;

	@Builder
	public SqlFileEntity(String sqlId, String vconnid, String viewid, String gueryTitle, String queryCont, String sqlParam) {
		this.sqlId = sqlId;
		this.vconnid = vconnid;
		this.viewid = viewid;
		this.gueryTitle = gueryTitle;
		this.queryCont = queryCont;
		this.sqlParam = sqlParam;
	}
	public final static String SQL_ID="sqlId";

	public final static String VCONNID="vconnid";

	public final static String VIEWID="viewid";

	public final static String GUERY_TITLE="gueryTitle";

	public final static String QUERY_CONT="queryCont";

	public final static String SQL_PARAM="sqlParam";
}