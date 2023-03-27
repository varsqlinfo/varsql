package com.varsql.web.model.entity.scheduler;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.varsql.web.model.id.TriggersID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@IdClass(TriggersID.class)
@Table(name = TriggersEntity._TB_NAME)
public class TriggersEntity{

	public final static String _TB_NAME="VTQTZ_TRIGGERS";

	@Id
	private String schedName; 

	@Id
	private String triggerName; 

	@Id
	private String triggerGroup; 

	@Column(name ="JOB_NAME")
	private String jobName; 

	@Column(name ="JOB_GROUP")
	private String jobGroup; 

	@Column(name ="DESCRIPTION")
	private String description; 

	@Column(name ="NEXT_FIRE_TIME")
	private long nextFireTime; 

	@Column(name ="PREV_FIRE_TIME")
	private long prevFireTime; 

	@Column(name ="PRIORITY")
	private int priority; 

	@Column(name ="TRIGGER_STATE")
	private String triggerState; 

	@Column(name ="TRIGGER_TYPE")
	private String triggerType; 

	@Column(name ="START_TIME")
	private long startTime; 

	@Column(name ="END_TIME")
	private long endTime; 

	@Column(name ="CALENDAR_NAME")
	private String calendarName; 

	@Column(name ="MISFIRE_INSTR")
	private int misfireInstr; 
	
	@Builder
	public TriggersEntity (String schedName, String triggerName, String triggerGroup, String jobName, String jobGroup, String description, long nextFireTime, long prevFireTime, int priority, String triggerState, String triggerType, long startTime, long endTime, String calendarName, int misfireInstr) {
		this.schedName = schedName;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.description = description;
		this.nextFireTime = nextFireTime;
		this.prevFireTime = prevFireTime;
		this.priority = priority;
		this.triggerState = triggerState;
		this.triggerType = triggerType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.calendarName = calendarName;
		this.misfireInstr = misfireInstr;
	}

	public final static String SCHED_NAME="schedName";
	public final static String TRIGGER_NAME="triggerName";
	public final static String TRIGGER_GROUP="triggerGroup";
	public final static String JOB_NAME="jobName";
	public final static String JOB_GROUP="jobGroup";
	public final static String DESCRIPTION="description";
	public final static String NEXT_FIRE_TIME="nextFireTime";
	public final static String PREV_FIRE_TIME="prevFireTime";
	public final static String PRIORITY="priority";
	public final static String TRIGGER_STATE="triggerState";
	public final static String TRIGGER_TYPE="triggerType";
	public final static String START_TIME="startTime";
	public final static String END_TIME="endTime";
	public final static String CALENDAR_NAME="calendarName";
	public final static String MISFIRE_INSTR="misfireInstr";
}