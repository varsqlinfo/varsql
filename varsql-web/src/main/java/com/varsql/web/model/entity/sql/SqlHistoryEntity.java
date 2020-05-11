package com.varsql.web.model.entity.sql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.varsql.web.model.entity.user.RegInfoEntity;
import com.varsql.web.model.id.generator.AppUUIDGenerator;

import lombok.Builder;

@Entity
@Table(name = SqlHistoryEntity._TB_NAME)
public class SqlHistoryEntity{
	public final static String _TB_NAME="VTSQL_HISTORY";
	
	@Id
	@GenericGenerator(name = "historyidGenerator"
		, strategy = "com.varsql.web.model.id.generator.AppUUIDGenerator"
		, parameters = @Parameter(
            name = AppUUIDGenerator.PREFIX_PARAMETER,
            value = ""
		)
	)
    @GeneratedValue(generator = "historyidGenerator")
	@Column(name ="HISTORYID")
	private String historyid;
	
	
	@Column(name ="VCONNID")
	private String vconnid;

	@Column(name ="VIEWID")
	private String viewid;

	@Column(name ="START_TIME")
	private String startTime;

	@Column(name ="END_TIME")
	private String endTime;

	@Column(name ="DELAY_TIME")
	private int delayTime;

	@Column(name ="LOG_SQL")
	private String logSql;

	@Column(name ="USR_IP")
	private String usrIp;

	@Column(name ="ERROR_LOG")
	private String errorLog;
	
	@OneToOne
	@JoinColumn(name = "VIEWID", referencedColumnName = RegInfoEntity.VIEWID ,nullable = false, insertable =false , updatable =false)
	private RegInfoEntity regInfo;


	@Builder
	public SqlHistoryEntity(String vconnid, String viewid, String historyid, String startTime, String endTime, int delayTime, String logSql, String usrIp, String errorLog) {
		this.vconnid = vconnid;
		this.viewid = viewid;
		this.historyid = historyid;
		this.startTime = startTime;
		this.endTime = endTime;
		this.delayTime = delayTime;
		this.logSql = logSql;
		this.usrIp = usrIp;
		this.errorLog = errorLog;

	}
	public final static String VCONNID="vconnid";

	public final static String VIEWID="viewid";

	public final static String HISTORYID="historyid";

	public final static String START_TIME="startTime";

	public final static String END_TIME="endTime";

	public final static String DELAY_TIME="delayTime";

	public final static String LOG_SQL="logSql";

	public final static String USR_IP="usrIp";

	public final static String ERROR_LOG="errorLog";

}