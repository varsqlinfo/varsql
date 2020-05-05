package com.varsql.web.model.entity.sql;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.varsql.web.model.id.SqlStatisticsID;

import lombok.Builder;

@Entity
@IdClass(SqlStatisticsID.class)
@Table(name = SqlStatisticsEntity._TB_NAME)
public class SqlStatisticsEntity{
	public final static String _TB_NAME="VTSQL_STATISTICS";
	
	@Id
	private String vconnid;
	
	@Id
	private String viewid;
	
	@Id
	private LocalDateTime startTime;

	@Column(name ="S_MM")
	private Integer sMm;

	@Column(name ="S_DD")
	private Integer sDd;

	@Column(name ="S_HH")
	private Integer sHh;

	@Column(name ="END_TIME")
	private LocalDateTime endTime;

	@Column(name ="DELAY_TIME")
	private Integer delayTime;

	@Column(name ="RESULT_COUNT")
	private double resultCount;

	@Column(name ="COMMAND_TYPE")
	private String commandType;

	@Builder
	public SqlStatisticsEntity(String vconnid, String viewid, LocalDateTime startTime, int sMm, int sDd, int sHh, LocalDateTime endTime, int delayTime, double resultCount, String commandType) {
		this.vconnid = vconnid;
		this.viewid = viewid;
		this.startTime = startTime;
		this.sMm = sMm;
		this.sDd = sDd;
		this.sHh = sHh;
		this.endTime = endTime;
		this.delayTime = delayTime;
		this.resultCount = resultCount;
		this.commandType = commandType;
	}
	
	public final static String VCONNID="vconnid";

	public final static String VIEWID="viewid";

	public final static String START_TIME="startTime";

	public final static String S_MM="sMm";

	public final static String S_DD="sDd";

	public final static String S_HH="sHh";

	public final static String END_TIME="endTime";

	public final static String DELAY_TIME="delayTime";

	public final static String RESULT_COUNT="resultCount";

	public final static String COMMAND_TYPE="commandType";

}