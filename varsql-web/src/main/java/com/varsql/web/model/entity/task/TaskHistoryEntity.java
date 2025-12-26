package com.varsql.web.model.entity.task;
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

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = TaskHistoryEntity._TB_NAME)
public class TaskHistoryEntity{

	public final static String _TB_NAME="VTTASK_HISTORY";

	@Id
	@TableGenerator(
		name = "taskHistoryIdGenerator"
		,table= VarsqlJpaConstants.TABLE_SEQUENCE_NAME
		,pkColumnValue = _TB_NAME
		,allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "taskHistoryIdGenerator")
	@Column(name = "HIST_SEQ", nullable = false)
	private long histSeq; 

	@Column(name ="INSTANCE_ID")
	private String instanceId; 

	@Column(name ="TASK_ID")
	private String taskId; 

	@Column(name ="RUN_TYPE")
	private String runType; 

	@Column(name ="START_TIME")
	private Timestamp startTime; 

	@Column(name ="END_TIME")
	private Timestamp endTime; 

	@Column(name ="RUN_TIME")
	private long runTime; 

	@Column(name ="RESULT_COUNT")
	private long resultCount; 

	@Column(name ="FAIL_COUNT")
	private long failCount; 

	@Column(name ="MESSAGE")
	private String message; 

	@Column(name ="STATUS")
	private String status; 

	@Column(name ="CUSTOM_INFO")
	private String customInfo; 


	@Builder
	public TaskHistoryEntity (long histSeq, String instanceId, String taskId, String runType, Timestamp startTime, Timestamp endTime, long runTime, long resultCount, long failCount, String message, String status, String customInfo) {
		this.histSeq = histSeq;
		this.instanceId = instanceId;
		this.taskId = taskId;
		this.runType = runType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.runTime = runTime;
		this.resultCount = resultCount;
		this.failCount = failCount;
		this.message = message;
		this.status = status;
		this.customInfo = customInfo;
	}
	public final static String HIST_SEQ="histSeq";
	public final static String INSTANCE_ID="instanceId";
	public final static String TASK_ID="taskId";
	public final static String RUN_TYPE="runType";
	public final static String START_TIME="startTime";
	public final static String END_TIME="endTime";
	public final static String RUN_TIME="runTime";
	public final static String RESULT_COUNT="resultCount";
	public final static String FAIL_COUNT="failCount";
	public final static String MESSAGE="message";
	public final static String STATUS="status";
	public final static String CUSTOM_INFO="customInfo";
}