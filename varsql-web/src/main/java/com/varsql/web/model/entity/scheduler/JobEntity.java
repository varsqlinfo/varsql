package com.varsql.web.model.entity.scheduler;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.varsql.web.model.base.AbstractAuditorModel;
import com.varsql.web.model.entity.db.DBConnectionEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = JobEntity._TB_NAME)
@ToString(exclude = {"jobDBConnection"})
public class JobEntity extends AbstractAuditorModel{

	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTQTZ_JOB_SCHEDULE";
	
	@Id
	@Column(name ="JOB_UID")
	private String jobUid;
	
	@Column(name ="JOB_NAME")
	private String jobName;
	
	@Column(name ="JOB_GROUP")
	private String jobGroup; 

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VCONNID", nullable = false)
	private DBConnectionEntity jobDBConnection;

	@Column(name ="JOB_DATA")
	private String jobData; 

	@Column(name ="CRON_EXPRESSION")
	private String cronExpression;
	
	@Column(name ="JOB_DESCRIPTION")
	private String jobDescription; 
	
	@Builder
	public JobEntity (String jobUid, String jobName, String jobGroup, DBConnectionEntity jobDBConnection, String jobData, String cronExpression, String jobDescription) {
		this.jobUid = jobUid;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobDBConnection = jobDBConnection;
		this.jobData = jobData;
		this.cronExpression = cronExpression;
		this.jobDescription = jobDescription;
	}

	public final static String JOB_UID="jobUid";
	public final static String JOB_NAME="jobName";
	public final static String JOB_GROUP="jobGroup";
	public final static String SCHEDULE_DBCONNECTION="jobDBConnection";
	public final static String JOB_DATA="jobData";
	public final static String CRON_EXPRESSION="cronExpression";
	public final static String JOB_DESCRIPTION="jobDescription";
	
}