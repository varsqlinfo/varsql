package com.varsql.web.app.manager.service;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.ExecuteType;
import com.varsql.core.exception.NotFoundException;
import com.varsql.core.task.transfer.TaskResult;
import com.varsql.web.dto.execution.ExecutionHistoryResponseDTO;
import com.varsql.web.dto.task.TaskExecutionVO;
import com.varsql.web.dto.task.TaskSqlRequestDTO;
import com.varsql.web.dto.task.TaskSqlResponseDTO;
import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.entity.execution.ExecutionHistoryEntity;
import com.varsql.web.model.entity.task.TaskSqlEntity;
import com.varsql.web.repository.execution.ExecutionHistoryEntityRepository;
import com.varsql.web.repository.task.TaskSqlRepository;
import com.varsql.web.scheduler.task.SqlTaskRunner;
import com.varsql.web.util.VarsqlBeanUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;

/**
 * sql task service
 * 
 * @author ytkim
 *
 */
@Component
@RequiredArgsConstructor
public class TaskSqlMgmtService {
	private final static Logger logger = LoggerFactory.getLogger(TaskSqlMgmtService.class);
	
	private final TaskSqlRepository taskSqlRepository;
	
	private final SqlTaskRunner sqlTaskService;
	
	
	private final ExecutionHistoryEntityRepository executionHistoryEntityRepository;
	
	/**
	 * 목록 
	 * 
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult list(SearchParameter searchParameter) {
		Sort sort =Sort.by(Sort.Direction.DESC, AbstractRegAuditorModel.REG_DT);
		
		Page<TaskSqlResponseDTO> result = taskSqlRepository.findAllByNameContaining(searchParameter.getKeyword(), VarsqlUtils.convertSearchInfoToPage(searchParameter, sort));
		
		return VarsqlUtils.getResponseResult(result.getContent(), result.getTotalElements(), searchParameter);
	}

	/**
	 * 정보 저장.  
	 *
	 * @param dto
	 * @return
	 */
	public ResponseResult save(TaskSqlRequestDTO dto) {
		TaskSqlEntity tse;
		
		logger.debug("save :{}", dto);
		
		if(!StringUtils.isBlank(dto.getTaskId())) {
			tse = taskSqlRepository.findByTaskId(dto.getTaskId());
			
			if(tse == null) {
				throw new NotFoundException("id not found : "+ dto.getTaskId());
			}
			BeanUtils.copyProperties(dto.toEntity(), tse, "taskId");
		}else {
			tse = dto.toEntity();
		}
		
		taskSqlRepository.save(tse);
		
		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 * 정보 삭제. 
	 *
	 * @param taskId
	 * @return
	 */
	public ResponseResult remove(String taskId) {
		TaskSqlEntity deleteItem = taskSqlRepository.findByTaskId(taskId);
		
		if(deleteItem == null) {
			throw new NotFoundException("id not found : "+ taskId);
		}
		
		taskSqlRepository.delete(deleteItem);
		
		return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	/**
	 * 정보 복사 
	 *
	 * @param taskId
	 * @return
	 */
	public ResponseResult copyInfo(String taskId) {
		
		TaskSqlEntity copyInfo = taskSqlRepository.findByTaskId(taskId);
		
		TaskSqlEntity copyEntity = VarsqlBeanUtils.copyEntity(copyInfo);
	    copyEntity.setTaskId(null);
	    copyEntity.setTaskName(copyEntity.getTaskName() + "-copy");
	    
	    taskSqlRepository.save(copyEntity);
	    
	    return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	/**
	 * sql task 실행. 
	 * 
	 * @param taskId
	 * @param object 
	 * @param string 
	 * @return
	 * @throws SQLException 
	 */
	public ResponseResult execute(String taskId, String requid) {
		TaskExecutionVO vo = TaskExecutionVO.builder().taskId(taskId).requid(requid).runType(ExecuteType.NORMAL).build();
		TaskResult taskResult;
		ResponseResult result = new ResponseResult();
		try {
			taskResult = sqlTaskService.run(vo);
		} catch (Exception e) {
			result.setResultCode(VarsqlAppCode.EC_TASK);
			result.setMessage(e.getMessage());
		}
		
		return result; 
	}
	
	/**
	 * 이력 조회
	 * 
	 * @param taskId
	 * @param schParam
	 * @return
	 */
	public ResponseResult findHistory(String taskId, SearchParameter schParam) {
		Page<ExecutionHistoryResponseDTO> result = executionHistoryEntityRepository.findByTargetId(taskId, ExecuteType.TASK, VarsqlUtils.convertSearchInfoToPage(schParam, Sort.by(ExecutionHistoryEntity.START_TIME).descending()));
		
		return VarsqlUtils.getResponseResult(result.getContent(), result.getTotalElements(), schParam);
	}

}
