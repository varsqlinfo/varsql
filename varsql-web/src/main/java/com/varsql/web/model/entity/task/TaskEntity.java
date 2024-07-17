package com.varsql.web.model.entity.task;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.varsql.web.model.base.AbstractAuditorModel;
import com.varsql.web.model.converter.BooleanToYnConverter;
import com.varsql.web.model.id.generator.AppUUIDGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * task 
 * @author ytkim
 *
 */
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TASK_TYPE")
@Table(name = TaskEntity._TB_NAME)
public class TaskEntity  extends AbstractAuditorModel{

	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTTASK";

	@Id
	@GenericGenerator(name = "taskIdGenerator"
		, strategy = "com.varsql.web.model.id.generator.AppUUIDGenerator"
		, parameters = @Parameter(
            name = AppUUIDGenerator.PREFIX_PARAMETER,
            value = "TSK_"
		)
	)
    @GeneratedValue(generator = "taskIdGenerator")
	@Column(name ="TASK_ID")
	private String taskId; 

	@Column(name ="TASK_NAME")
	private String taskName; 

	@Column(name ="TASK_TYPE", insertable = false, updatable = false)
	private String taskType; 

	@Column(name ="USE_YN")
	@Convert(converter = BooleanToYnConverter.class)
	private boolean useYn; 

	@Column(name ="DESCRIPTION")
	private String description; 

	public TaskEntity (String taskId, String taskName, String taskType, boolean useYn, String description) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskType = taskType;
		this.useYn = useYn;
		this.description = description;
	}
	public final static String TASK_ID="taskId";
	public final static String TASK_NAME="taskName";
	public final static String TASK_TYPE="taskType";
	public final static String USE_YN="useYn";
	public final static String DEL_YN="delYn";
	public final static String DESCRIPTION="description";
}