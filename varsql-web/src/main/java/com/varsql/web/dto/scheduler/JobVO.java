package com.varsql.web.dto.scheduler;
import java.io.Serializable;

import com.varsql.web.model.entity.scheduler.JobEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class JobVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String jobUid;
	
	private String jobName;
	
	private String jobGroup;
	
	private String vconnid; 

	private String jobDescription; 

	private String cronExpression;
	
	private String jobData;
	
	@Builder
	public JobVO(String jobUid, String jobName, String jobGroup, String vconnid, String jobDescription, 
			String cronExpression, String jobData){
		this.jobUid = jobUid;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.vconnid = vconnid;
		this.jobDescription = jobDescription;
		this.cronExpression = cronExpression;
		this.jobData = jobData;
	}
	
	public static JobVO toVo(JobRequestDTO dto) {
		return JobVO.builder()
			.jobUid(dto.getJobUid())
			.jobName(dto.getJobName())
			.jobGroup(dto.getJobGroup())
			.vconnid(dto.getVconnid())
			.cronExpression(dto.getCronExpression())
			.jobDescription(dto.getJobDescription())
			.jobData(dto.getJobData())
			.build();
	}
	
	public static JobVO toVo(JobEntity entity) {
		return JobVO.builder()
				.jobUid(entity.getJobUid())
				.jobName(entity.getJobName())
				.jobGroup(entity.getJobGroup())
				.vconnid(entity.getJobDBConnection().getVconnid())
				.cronExpression(entity.getCronExpression())
				.jobDescription(entity.getJobDescription())
				.jobData(entity.getJobData())
				.build();
	}
	
	public static JobVO taskEntityToVo(JobEntity entity) {
		return JobVO.builder()
				.jobUid(entity.getJobUid())
				.jobName(entity.getJobName())
				.jobGroup(entity.getJobGroup())
				.cronExpression(entity.getCronExpression())
				.jobDescription(entity.getJobDescription())
				.jobData(entity.getJobData())
				.build();
	}

}