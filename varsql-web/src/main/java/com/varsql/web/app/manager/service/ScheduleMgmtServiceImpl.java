package com.varsql.web.app.manager.service;
import org.quartz.Job;
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
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.app.scheduler.JOBServiceUtils;
import com.varsql.web.app.scheduler.bean.JobBean;
import com.varsql.web.app.scheduler.job.DataBackupJob;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.scheduler.JobScheduleDetailDTO;
import com.varsql.web.dto.scheduler.JobScheduleVO;
import com.varsql.web.model.entity.scheduler.JobScheduleEntity;
import com.varsql.web.model.entity.scheduler.ScheduleHistoryEntity;
import com.varsql.web.model.mapper.scheduler.ScheduleHistoryMapper;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.scheduler.JobScheduleEntityRepository;
import com.varsql.web.repository.scheduler.ScheduleHistoryEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;

import lombok.RequiredArgsConstructor;

/**
 * schedule management service
* 
* @fileName	: ScheduleMgmtServiceImpl.java
* @author	: ytkim
 */
@Service
@RequiredArgsConstructor
public class ScheduleMgmtServiceImpl extends AbstractService{
	
	private final Logger logger = LoggerFactory.getLogger(ScheduleMgmtServiceImpl.class);
	
	enum JOB_MODE{
		RUN, PAUSE, RESUME
	}
	
	final private DBConnectionEntityRepository  dbConnectionEntityRepository;

	final private JobScheduleEntityRepository jobScheduleEntityRepository;
	
	final private ScheduleHistoryEntityRepository scheduleHistoryEntityRepository;  
	
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
			dpi.setObjectType(ObjectType.TABLE.getObjectTypeId());

			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dpi.getDbType());
			resultObject.setList(dbMetaEnum.getDBObjectList(ObjectType.TABLE.getObjectTypeId(), dpi));
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
	 * @method : saveOrUpdate
	 * @param class1
	 * @param jobScheduleVO
	 * @throws SchedulerException
	 */
	public void saveOrUpdate(Class<? extends JobBean> clazz, JobScheduleVO jobScheduleVO) throws SchedulerException {
		JOBServiceUtils.saveOrUpdate(scheduler, clazz, jobScheduleVO);
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
		logger.info("delete job schedule uid : {}", jobUid);
		JobScheduleEntity entity = jobScheduleEntityRepository.findByJobUid(jobUid);
		
		if(entity == null) {
			return ResponseResult.builder()
					.resultCode(RequestResultCode.NOT_FOUND)
					.message("job schedule info not found : "+ jobUid)
					.build();
		}
		
		jobScheduleEntityRepository.delete(entity);
		
		try {
			JOBServiceUtils.deleteJob(scheduler, JobScheduleVO.toVo(entity));
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
		JobScheduleEntity entity = jobScheduleEntityRepository.findByJobUid(jobUid);
		
		if(entity == null) {
			return ResponseResult.builder()
					.resultCode(RequestResultCode.NOT_FOUND)
					.message("job schedule info not found : "+ jobUid)
					.build();
		}
		
		JOB_MODE jobMode = JOB_MODE.valueOf(mode);
		
		try {
			if(JOB_MODE.RUN.equals(jobMode)) { // 실행
				JOBServiceUtils.runJob(scheduler, JobScheduleVO.toVo(entity));
			}else if(JOB_MODE.PAUSE.equals(jobMode)) { // 멈춤 
				JOBServiceUtils.pauseJob(scheduler, JobScheduleVO.toVo(entity));
			}else if(JOB_MODE.RESUME.equals(jobMode)) { // 재시작
				JOBServiceUtils.resumeJob(scheduler, JobScheduleVO.toVo(entity));
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
		
		Page<ScheduleHistoryEntity> result = scheduleHistoryEntityRepository.findByJobUid(jobUid, VarsqlUtils.convertSearchInfoToPage(schParam, Sort.by(ScheduleHistoryEntity.START_TIME).descending()));
		
		return VarsqlUtils.getResponseResult(result, schParam, ScheduleHistoryMapper.INSTANCE);
	}
}