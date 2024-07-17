package com.varsql.web.model.entity.task;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.varsql.web.constants.TaskConstants;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


/**
 * data transfer task
 * 
 * @author ytkim
 *
 */
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = TaskTransperEntity._TB_NAME)
@DiscriminatorValue(TaskConstants.TRANSFER_TYPE_NAME)
public class TaskTransperEntity extends TaskEntity{ 

	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTTASK_TRANSPER";

	@Column(name ="TRANSPER_TYPE")
	private String transperType; 

	@Column(name ="KEY_CHECK_SQL")
	private String keyCheckSql; 

	@Column(name ="SOURCE_VCONNID")
	private String sourceVconnid; 

	@Column(name ="SOURCE_CONFIG")
	private String sourceConfig; 

	@Column(name ="FETCH_COUNT")
	private int fetchCount; 

	@Column(name ="TARGET_VCONNID")
	private String targetVconnid; 

	@Column(name ="TARGET_CONFIG")
	private String targetConfig; 

	@Builder
	public TaskTransperEntity (String taskId, String taskName, String taskType, boolean useYn, String description,
			String transperType, String keyCheckSql, String sourceVconnid, String sourceConfig, int fetchCount, String targetVconnid, String targetConfig) {
		
		super(taskId, taskName, taskType, useYn, description);
		
		this.transperType = transperType;
		this.keyCheckSql = keyCheckSql;
		this.sourceVconnid = sourceVconnid;
		this.sourceConfig = sourceConfig;
		this.fetchCount = fetchCount;
		this.targetVconnid = targetVconnid;
		this.targetConfig = targetConfig;
	}
	public final static String TRANSPER_TYPE="transperType";
	public final static String KEY_CHECK_SQL="keyCheckSql";
	public final static String SOURCE_VCONNID="sourceVconnid";
	public final static String SOURCE_CONFIG="sourceConfig";
	public final static String FETCH_COUNT="fetchCount";
	public final static String TARGET_VCONNID="targetVconnid";
	public final static String TARGET_CONFIG="targetConfig";
}