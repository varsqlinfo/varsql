package com.varsql.web.app.manager.service;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.app.scheduler.job.SqlExecuteJob;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.scheduler.JobDetailDTO;
import com.varsql.web.dto.scheduler.JobRequestDTO;
import com.varsql.web.dto.scheduler.JobVO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.scheduler.JobEntity;
import com.varsql.web.model.mapper.scheduler.JobMapper;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.scheduler.JobEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

import lombok.RequiredArgsConstructor;

/**
 * sql job service
* 
* @fileName	: SqlJobServiceImpl.java
* @author	: ytkim
 */
@Service
@RequiredArgsConstructor
public class SqlJobServiceImpl extends AbstractService{
	
	private final Logger logger = LoggerFactory.getLogger(SqlJobServiceImpl.class);
	
	private String JOB_GROUP = "SQL_EXECUTE_JOB";
	
	final private DBConnectionEntityRepository  dbConnectionEntityRepository;

	final private JobEntityRepository jobEntityRepository;
	
	final private SchedulerMgmtServiceImpl schedulerMgmtServiceImpl;
	
	/**
	 * 목록보기
	 *
	 * @method : findJobList
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult findJobList(SearchParameter searchParameter) {
		Page<JobEntity> result = jobEntityRepository.findByJobGroupAndJobNameContaining(JOB_GROUP, searchParameter.getKeyword(), VarsqlUtils.convertSearchInfoToPage(searchParameter));
		return VarsqlUtils.getResponseResult(result, searchParameter, JobMapper.INSTANCE);
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
		
		entity.setJobGroup(JOB_GROUP);
		
		DBConnectionEntity dbConnectionEntity = dbConnectionEntityRepository.findByVconnid(dto.getVconnid());
		entity.setJobDBConnection(dbConnectionEntity);
		
		jobEntityRepository.save(entity);
		
		try {
			schedulerMgmtServiceImpl.saveOrUpdate(SqlExecuteJob.class, JobVO.toVo(entity));
		} catch (SchedulerException e) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER, e);
		}
		
		resultObject.setItemOne(Integer.valueOf(1));
		
		return resultObject;
	}
}