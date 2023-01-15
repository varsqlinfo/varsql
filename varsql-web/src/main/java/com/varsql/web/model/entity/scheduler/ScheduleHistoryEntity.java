package com.varsql.web.model.entity.scheduler;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;

import com.varsql.web.constants.VarsqlJpaConstants;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * schedule history
* 
* @fileName	: ScheduleHistoryEntity.java
* @author	: ytkim
 */
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = ScheduleHistoryEntity._TB_NAME)
public class ScheduleHistoryEntity {

	public final static String _TB_NAME = "VTQTZ_HISTORY";

	@Id
	@TableGenerator(
		name = "scheduleHistoryIdGenerator"
		,table= VarsqlJpaConstants.TABLE_SEQUENCE_NAME
		,pkColumnValue = _TB_NAME
		,allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "scheduleHistoryIdGenerator")
	@Column(name = "HIST_SEQ", nullable = false)
	private long histSeq;
	
	@Column(name = "INSTANCE_ID")
	private String instanceId;

	@Column(name = "JOB_UID")
	private String jobUid;
	
	@Column(name = "RUN_TYPE")	// batch, run
	private String runType;

	@Column(name = "START_TIME")
	private Timestamp startTime;

	@Column(name = "END_TIME")
	private Timestamp endTime;

	@Column(name = "RUN_TIME")
	private long runTime;

	@Column(name = "RESULT_COUNT")
	private long resultCount;
	
	@Column(name = "FAIL_COUNT")
	private long failCount;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "CUSTOM_INFO")
	private String customInfo;

	@Builder
	public ScheduleHistoryEntity(String instanceId, String jobUid, String runType, Timestamp startTime,
			Timestamp endTime, long runTime, long resultCount, long failCount, String message, String status,  String customInfo) {
		this.instanceId = instanceId;
		this.jobUid = jobUid;
		this.runType = runType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.runTime = runTime;
		this.failCount = failCount;
		this.resultCount = resultCount;
		this.message = message;
		this.status = status;
		this.customInfo = customInfo;
	}

	public final static String HIST_SEQ = "histSeq";
	public final static String INSTANCE_ID = "instanceId";
	public final static String JOB_UID = "jobUid";
	public final static String RUN_TYPE = "runType";
	public final static String START_TIME = "startTime";
	public final static String END_TIME = "endTime";
	public final static String RUN_TIME = "runTime";
	public final static String RESULT_COUNT = "resultCount";
	public final static String FAIL_COUNT = "failCount";
	public final static String MESSAGE = "message";
	public final static String STATUS = "status";
}