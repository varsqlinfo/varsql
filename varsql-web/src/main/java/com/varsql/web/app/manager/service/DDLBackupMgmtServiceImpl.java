package com.varsql.web.app.manager.service;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.app.scheduler.job.DDLBackupJob;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.scheduler.JobScheduleDetailDTO;
import com.varsql.web.dto.scheduler.JobScheduleRequestDTO;
import com.varsql.web.dto.scheduler.JobScheduleVO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.scheduler.JobScheduleEntity;
import com.varsql.web.model.mapper.scheduler.JobScheduleMapper;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.scheduler.JobScheduleEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

import lombok.RequiredArgsConstructor;

/**
 * Data backup service
* 
* @fileName	: DataBackupMgmtServiceImpl.java
* @author	: ytkim
 */
@Service
@RequiredArgsConstructor
public class DDLBackupMgmtServiceImpl extends AbstractService{
	
	private final Logger logger = LoggerFactory.getLogger(DDLBackupMgmtServiceImpl.class);
	
	private String DDL_BACKUP_JOB_GROUP = "DDL_BACKUP";
	
	enum JOB_MODE{
		RUN, PAUSE, RESUME
	}
	
	final private DBConnectionEntityRepository  dbConnectionEntityRepository;

	final private JobScheduleEntityRepository jobScheduleEntityRepository;
	
	final private ScheduleMgmtServiceImpl scheduleMgmtServiceImpl;
	
	/**
	 * 목록보기
	 *
	 * @method : findDataBackupJobList
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult findDataBackupJobList(SearchParameter searchParameter) {
		Page<JobScheduleEntity> result = jobScheduleEntityRepository.findByJobGroupAndJobNameContaining(DDL_BACKUP_JOB_GROUP, searchParameter.getKeyword(), VarsqlUtils.convertSearchInfoToPage(searchParameter));
		return VarsqlUtils.getResponseResult(result, searchParameter, JobScheduleMapper.INSTANCE);
	}
	
	/**
	 * 
	 *
	 * @method : dataObjectList
	 * @param vconnid
	 * @return
	 */
	public ResponseResult dataObjectList(String vconnid) {
		
		ResponseResult resultObject = new ResponseResult();
		
		DatabaseInfo databaseInfo = dbConnectionEntityRepository.findDatabaseInfo(vconnid);

		if(databaseInfo==null){
			resultObject.setResultCode(RequestResultCode.ERROR);
			resultObject.setMessage("connection info not found : "+ vconnid);
			return resultObject; 
		}else{
			resultObject.setList(MetaControlFactory.getDbInstanceFactory(databaseInfo.getType()).getServiceMenu());
		}

		return resultObject;
	}
	
	/**
	 * 상세보기.
	 *
	 * @method : findDetailInfo
	 * @param jobName
	 * @return
	 */
	public ResponseResult findDetailInfo(String jobUid) {
		JobScheduleDetailDTO detailDto = jobScheduleEntityRepository.findJobDetailInfo(jobUid);
		
		if(detailDto == null) {
			return ResponseResult.builder().resultCode((RequestResultCode.NOT_FOUND))
					.message("job schedule info not found : "+ jobUid).build();
		}
		
		return VarsqlUtils.getResponseResultItemOne(detailDto);
	}
	
	/**
	 * job 정보 저장. 
	 *
	 * @method : save
	 * @param dto
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult save(JobScheduleRequestDTO dto){
		
		logger.info("job schedule info : {}", dto);
		ResponseResult resultObject = new ResponseResult();
		
		String jobUid =  dto.getJobUid();
		
		JobScheduleEntity entity;
		
		if(StringUtils.isBlank(jobUid)) {
			entity = dto.toEntity();
			
			jobUid = VartechUtils.generateUUID();
			
			dto.setJobUid(jobUid);
			entity.setJobUid(jobUid);
		}else {
			entity = jobScheduleEntityRepository.findByJobUid(jobUid);
			if(entity == null) {
				
				resultObject.setResultCode(RequestResultCode.NOT_FOUND);
				resultObject.setMessage("job schedule info not found : "+ jobUid);
				return resultObject;
			}
			
			entity.setJobName(dto.getJobName());
			entity.setCronExpression(dto.getCronExpression());
			entity.setJobData(dto.getJobData());
			entity.setJobDescription(dto.getJobDescription());
		}
		
		entity.setJobGroup(DDL_BACKUP_JOB_GROUP);
		
		DBConnectionEntity dbConnectionEntity = dbConnectionEntityRepository.findByVconnid(dto.getVconnid());
		entity.setScheduleDBConnection(dbConnectionEntity);
		
		jobScheduleEntityRepository.save(entity);
		
		try {
			scheduleMgmtServiceImpl.saveOrUpdate(DDLBackupJob.class, JobScheduleVO.toVo(entity));
		} catch (SchedulerException e) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER, e);
		}
		
		resultObject.setItemOne(Integer.valueOf(1));
		
		return resultObject;
	}
}