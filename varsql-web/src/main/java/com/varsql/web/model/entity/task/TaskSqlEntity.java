package com.varsql.web.model.entity.task;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.varsql.web.constants.TaskType;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBConnectionViewEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * sql 실행 task
 * 
 * @author ytkim
 *
 */
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = TaskSqlEntity._TB_NAME)
@DiscriminatorValue("SQL")
public class TaskSqlEntity extends TaskEntity{ 

	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTTASK_SQL";
	
	@NotEmpty
	@Column(name ="VCONNID")
	private String vconnid;
	
	@Column(name ="SQL")
	private String sql;
	
	@Column(name ="PARAMETER")
	private String parameter; 
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VCONNID", nullable = false, insertable = false, updatable = false)
	private DBConnectionViewEntity taskDBConnection;
	
	@Builder
	public TaskSqlEntity (String taskId, String taskName, String taskType, boolean useYn, String description,
			String vconnid, DBConnectionViewEntity taskDBConnection, String sql, String parameter) {
		
		super(taskId, taskName, taskType, useYn, description);
		this.vconnid = vconnid;
		this.taskDBConnection = taskDBConnection;
		this.sql = sql;
		this.parameter = parameter;
	}
	
	public final static String TASK_ID="taskId";
	public final static String VCONNID="vconnid";
	public final static String SQL="sql";
	public final static String PARAMETER="parameter";
}