package com.varsql.web.app.manager.service;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.servicemenu.DBObjectType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.app.scheduler.JOBServiceUtils;
import com.varsql.web.app.scheduler.bean.JobBean;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.scheduler.JobDetailDTO;
import com.varsql.web.dto.scheduler.JobVO;
import com.varsql.web.dto.scheduler.JobHistoryResponseDTO;
import com.varsql.web.model.entity.scheduler.JobEntity;
import com.varsql.web.model.entity.scheduler.JobHistoryEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.scheduler.JobEntityRepository;
import com.varsql.web.repository.scheduler.JobHistoryEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;

import lombok.RequiredArgsConstructor;

/**
 * scheduler management service
* 
* @fileName	: SchedulerMgmtServiceImpl.java
* @author	: ytkim
 */
@Service
@RequiredArgsConstructor
public class SchedulerMgmtServiceImpl extends AbstractService{
	
	private final Logger logger = LoggerFactory.getLogger(SchedulerMgmtServiceImpl.class);
	
	enum JOB_MODE{
		RUN, PAUSE, RESUME
	}
	
	final private DBConnectionEntityRepository  dbConnectionEntityRepository;

	final private JobEntityRepository jobEntityRepository;
	
	final private JobHistoryEntityRepository jobHistoryEntityRepository;  
	
	@Qualifier(ResourceConfigConstants.APP_SCHEDULER) 
	final private Scheduler scheduler;
	
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
			DatabaseParamInfo dpi = new DatabaseParamInfo(databaseInfo);
			dpi.setObjectType(DBObjectType.TABLE.getObjectTypeId());

			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dpi.getDbType());
			resultObject.setList(dbMetaEnum.getDBObjectList(DBObjectType.TABLE.getObjectTypeId(), dpi));
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
	 * @method : saveOrUpdate
	 * @param class1
	 * @param jobVO
	 * @throws SchedulerException
	 */
	public void saveOrUpdate(Class<? extends JobBean> clazz, JobVO jobVO) throws SchedulerException {
		JOBServiceUtils.saveOrUpdate(scheduler, clazz, jobVO);
	}
	
	/**
	 * job 정보 삭제.
	 *
	 * @method : delete
	 * @param jobUid
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult delete(String jobUid) {
		logger.info("delete job uid : {}", jobUid);
		JobEntity entity = jobEntityRepository.findByJobUid(jobUid);
		
		if(entity == null) {
			return ResponseResult.builder()
					.resultCode(RequestResultCode.NOT_FOUND)
					.message("job info not found : "+ jobUid)
					.build();
		}
		
		jobEntityRepository.delete(entity);
		
		try {
			JOBServiceUtils.deleteJob(scheduler, JobVO.toVo(entity));
		} catch (SchedulerException e) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER, e);
		}
		
		return VarsqlUtils.getResponseResultItemOne(Integer.valueOf(1));
	}
	
	/**
	 * job control 
	 *
	 * @method : jobCtrl
	 * @param jobUid
	 * @param mode  run, pause, resume
	 * @return
	 */
	public ResponseResult jobCtrl(String jobUid, String mode) { 
		JobEntity entity = jobEntityRepository.findByJobUid(jobUid);
		
		if(entity == null) {
			return ResponseResult.builder()
					.resultCode(RequestResultCode.NOT_FOUND)
					.message("job info not found : "+ jobUid)
					.build();
		}
		
		JOB_MODE jobMode = JOB_MODE.valueOf(mode);
		
		try {
			if(JOB_MODE.RUN.equals(jobMode)) { // 실행
				JOBServiceUtils.runJob(scheduler, JobVO.toVo(entity));
			}else if(JOB_MODE.PAUSE.equals(jobMode)) { // 멈춤 
				JOBServiceUtils.pauseJob(scheduler, JobVO.toVo(entity));
			}else if(JOB_MODE.RESUME.equals(jobMode)) { // 재시작
				JOBServiceUtils.resumeJob(scheduler, JobVO.toVo(entity));
			}
			return findDetailInfo(jobUid);
		} catch (SchedulerException e) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER, e);
		}
	}

	/**
	 * schedule history
	 *
	 * @method : findHistory
	 * @param jobUid
	 * @param schParam
	 * @return
	 */
	public ResponseResult findHistory(String jobUid, SearchParameter schParam) {
		
		Page<JobHistoryResponseDTO> result = jobHistoryEntityRepository.findByJobUid(jobUid, VarsqlUtils.convertSearchInfoToPage(schParam, Sort.by(JobHistoryEntity.START_TIME).descending()));
		
		return VarsqlUtils.getResponseResult(result.getContent(), result.getTotalElements(), schParam);
	}
}