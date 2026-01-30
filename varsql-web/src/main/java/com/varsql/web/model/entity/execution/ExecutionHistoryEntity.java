package com.varsql.web.model.entity.execution;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;

import com.varsql.core.common.constants.ExecuteType;
import com.varsql.web.constants.VarsqlJpaConstants;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* execution history 
* @author	: ytkim
*/
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = ExecutionHistoryEntity._TB_NAME)
public class ExecutionHistoryEntity {

	public final static String _TB_NAME = "VTEXECUTION_HISTORY";

	@Id
	@TableGenerator(
		name = "executionHistoryIdGenerator"
		,table= VarsqlJpaConstants.TABLE_SEQUENCE_NAME
		,pkColumnValue = _TB_NAME
		,allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "executionHistoryIdGenerator")
	@Column(name = "HIST_SEQ", nullable = false)
	private long histSeq;
	
	@Column(name = "INSTANCE_ID")
	private String instanceId;

	@Column(name = "TARGET_TYPE")
	@Enumerated(EnumType.STRING)
	private ExecuteType targetType;
	
	@Column(name = "TARGET_ID")
	private String targetId;
	
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
	public ExecutionHistoryEntity(String instanceId, ExecuteType targetType, String targetId, String runType, Timestamp startTime,
			Timestamp endTime, long runTime, long resultCount, long failCount, String message, String status,  String customInfo) {
		this.instanceId = instanceId;
		this.targetType = targetType;
		this.targetId = targetId;
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
	public final static String TARGET_TYPE = "targetType";
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