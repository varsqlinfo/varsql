package com.varsql.web.model.entity.task;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.varsql.web.constants.TaskConstants;

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
@DiscriminatorValue(TaskConstants.SQL_TYPE_NAME)
public class TaskSqlEntity extends TaskEntity{ 

	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTTASK_SQL";

	@Column(name ="VCONNID")
	private String vconnid; 

	@Column(name ="SQL")
	private String sql;
	
	@Column(name ="PARAMETER")
	private String parameter; 
	
	@Builder
	public TaskSqlEntity (String taskId, String taskName, String taskType, boolean useYn, String description,
			String vconnid, String sql, String parameter) {
		
		super(taskId, taskName, taskType, useYn, description);
		this.vconnid = vconnid;
		this.sql = sql;
		this.parameter = parameter;
	}
	
	public final static String TASK_ID="taskId";
	public final static String VCONNID="vconnid";
	public final static String SQL="sql";
	public final static String PARAMETER="parameter";
}