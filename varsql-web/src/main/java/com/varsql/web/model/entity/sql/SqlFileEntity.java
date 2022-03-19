package com.varsql.web.model.entity.sql;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.varsql.web.model.base.AbstractAuditorModel;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.id.generator.AppUUIDGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = SqlFileEntity._TB_NAME)
public class SqlFileEntity extends AbstractAuditorModel{
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

	@Column(name ="SQL_TITLE")
	private String sqlTitle;

	@Basic(fetch=FetchType.LAZY)
	@Column(name ="SQL_CONT" ,columnDefinition="CLOB")
	@Lob
	private String sqlCont;

	@Column(name ="SQL_PARAM")
	private String sqlParam;

	@Column(name ="EDITOR_CURSOR")
	private String editorCursor;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VCONNID" ,nullable = false, insertable =false , updatable =false)
	private DBConnectionEntity connInfo;

	@Builder
	public SqlFileEntity(String sqlId, String vconnid, String viewid, String sqlTitle, String sqlCont, String sqlParam, String editorCursor) {
		this.sqlId = sqlId;
		this.vconnid = vconnid;
		this.viewid = viewid;
		this.sqlTitle = sqlTitle;
		this.sqlCont = sqlCont;
		this.sqlParam = sqlParam;
		this.editorCursor = editorCursor;
	}
	public final static String SQL_ID="sqlId";

	public final static String VCONNID="vconnid";

	public final static String VIEWID="viewid";

	public final static String SQL_TITLE="sqlTitle";

	public final static String SQL_CONT="sqlCont";

	public final static String SQL_PARAM="sqlParam";

	public final static String EDITOR_CURSOR="editorCursor";
}