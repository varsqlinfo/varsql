package com.varsql.web.model.entity.task;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.varsql.web.constants.TaskType;
import com.varsql.web.model.entity.db.DBConnectionViewEntity;

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
@Table(name = TaskTransferEntity._TB_NAME)
@DiscriminatorValue("TRANSFER")
public class TaskTransferEntity extends TaskEntity{ 

	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTTASK_TRANSFER";

	@Column(name ="COMMIT_COUNT")
	private int commitCount; 
	
	@Column(name ="SOURCE_VCONNID")
	private String sourceVconnid; 

	@Column(name ="SOURCE_CONFIG")
	private String sourceConfig; 

	@Column(name ="TARGET_VCONNID")
	private String targetVconnid; 

	@Column(name ="TARGET_CONFIG")
	private String targetConfig; 
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOURCE_VCONNID", nullable = false, insertable = false, updatable = false)
	private DBConnectionViewEntity sourceDBConnection;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TARGET_VCONNID", nullable = false, insertable = false, updatable = false)
	private DBConnectionViewEntity targetDBConnection;

	@Builder
	public TaskTransferEntity (String taskId, String taskName, String taskType, boolean useYn, String description,
			String transferType, String sourceVconnid, String sourceConfig, int commitCount, String targetVconnid, String targetConfig) {
		
		super(taskId, taskName, taskType, useYn, description);
		
		this.sourceVconnid = sourceVconnid;
		this.sourceConfig = sourceConfig;
		this.commitCount = commitCount;
		this.targetVconnid = targetVconnid;
		this.targetConfig = targetConfig;
	}
	
	public final static String TRANSFER_TYPE="transferType";
	public final static String SOURCE_VCONNID="sourceVconnid";
	public final static String SOURCE_CONFIG="sourceConfig";
	public final static String COMMIT_COUNT="commitCount";
	public final static String TARGET_VCONNID="targetVconnid";
	public final static String TARGET_CONFIG="targetConfig";
}