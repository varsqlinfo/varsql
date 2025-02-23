package com.varsql.web.app.manager.service;
import java.sql.SQLException;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.servicemenu.DBObjectType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.app.scheduler.job.DataBackupJob;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.scheduler.JobDetailDTO;
import com.varsql.web.dto.scheduler.JobRequestDTO;
import com.varsql.web.dto.scheduler.JobVO;
import com.varsql.web.model.entity.db.DBConnectionViewEntity;
import com.varsql.web.model.entity.scheduler.JobEntity;
import com.varsql.web.model.mapper.scheduler.JobMapper;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.db.DBConnectionViewEntityRepository;
import com.varsql.web.repository.scheduler.JobEntityRepository;
import com.varsql.web.util.DatabaseUtils;
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
public class DataBackupMgmtServiceImpl extends AbstractService{
	
	private final Logger logger = LoggerFactory.getLogger(DataBackupMgmtServiceImpl.class);
	
	private String TABLE_BACKUP_JOB_GROUP = "TABLE_DATA_BACKUP";
	
	final private DBConnectionViewEntityRepository dbConnectionViewEntityRepository;
	
	final private DBConnectionEntityRepository dbConnectionEntityRepository;

	final private JobEntityRepository jobEntityRepository;
	
	final private SchedulerMgmtServiceImpl schedulerMgmtServiceImpl;
	
	/**
	 * 목록보기
	 *
	 * @method : findDataBackupJobList
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult findDataBackupJobList(SearchParameter searchParameter) {
		Page<JobEntity> result = jobEntityRepository.findByJobGroupAndJobNameContaining(TABLE_BACKUP_JOB_GROUP, searchParameter.getKeyword(), VarsqlUtils.convertSearchInfoToPage(searchParameter));
		return VarsqlUtils.getResponseResult(result, searchParameter, JobMapper.INSTANCE);
	}
	
	/**
	 * 
	 *
	 * @method : dataObjectList
	 * @param vconnid
	 * @param schema 
	 * @return
	 */
	public ResponseResult dataObjectList(String vconnid, String schema) {
		
		ResponseResult resultObject = new ResponseResult();
		
		DatabaseInfo databaseInfo = dbConnectionEntityRepository.findDatabaseInfo(vconnid);

		if(databaseInfo==null){
			resultObject.setResultCode(RequestResultCode.ERROR);
			resultObject.setMessage("connection info not found : "+ vconnid);
			return resultObject; 
		}else{
			DatabaseParamInfo dpi = new DatabaseParamInfo(databaseInfo);
			dpi.setSchema(schema);
			dpi.setObjectType(DBObjectType.TABLE.getObjectTypeId());

			resultObject.setList(MetaControlFactory.getDbInstanceFactory(databaseInfo.getType()).getDBObjectList(DBObjectType.TABLE.getObjectTypeId(), dpi));
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
		JobDetailDTO detailDto = jobEntityRepository.findJobDetailInfo(jobUid);
		
		if(detailDto == null) {
			return ResponseResult.builder().resultCode((RequestResultCode.NOT_FOUND))
					.message("job info not found : "+ jobUid).build();
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
	public ResponseResult save(JobRequestDTO dto){
		
		logger.info("job info : {}", dto);
		ResponseResult resultObject = new ResponseResult();
		
		String jobUid =  dto.getJobUid();
		
		JobEntity entity;
		
		if(StringUtils.isBlank(jobUid)) {
			entity = dto.toEntity();
			
			jobUid = VartechUtils.generateUUID();
			
			dto.setJobUid(jobUid);
			entity.setJobUid(jobUid);
		}else {
			entity = jobEntityRepository.findByJobUid(jobUid);
			if(entity == null) {
				
				resultObject.setResultCode(RequestResultCode.NOT_FOUND);
				resultObject.setMessage("job info not found : "+ jobUid);
				return resultObject;
			}
			
			entity.setJobName(dto.getJobName());
			entity.setCronExpression(dto.getCronExpression());
			entity.setJobData(dto.getJobData());
			entity.setJobDescription(dto.getJobDescription());
		}
		
		entity.setJobGroup(TABLE_BACKUP_JOB_GROUP);
		
		DBConnectionViewEntity dbConnectionEntity = dbConnectionViewEntityRepository.findByVconnid(dto.getVconnid());
		entity.setJobDBConnection(dbConnectionEntity);
		
		jobEntityRepository.save(entity);
		
		try {
			schedulerMgmtServiceImpl.saveOrUpdate(DataBackupJob.class, JobVO.toVo(entity));
		} catch (SchedulerException e) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER, e);
		}
		
		resultObject.setItemOne(Integer.valueOf(1));
		
		return resultObject;
	}
	
	/**
	 * 스키마 목록
	 * 
	 * @param vconnid
	 * @return
	 */
	public ResponseResult schemaList(String vconnid) {
		ResponseResult resultObject = new ResponseResult();
		
		DatabaseInfo databaseInfo = dbConnectionEntityRepository.findDatabaseInfo(vconnid);

		if(databaseInfo==null){
			resultObject.setResultCode(RequestResultCode.ERROR);
			resultObject.setMessage("connection info not found : "+ vconnid);
			return resultObject; 
		}else{
			try {
				resultObject.setList(DatabaseUtils.schemaList(new DatabaseParamInfo(databaseInfo)));
			} catch (SQLException e) {
				throw new VarsqlRuntimeException(RequestResultCode.ERROR, e);
			}
		}

		return resultObject;
	}
}