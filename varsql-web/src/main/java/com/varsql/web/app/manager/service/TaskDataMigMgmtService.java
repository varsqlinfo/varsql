package com.varsql.web.app.manager.service;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.varsql.core.common.beans.ProgressInfo;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.ExecuteType;
import com.varsql.core.exception.NotFoundException;
import com.varsql.core.task.transfer.TaskResult;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.constants.HttpSessionConstants;
import com.varsql.web.dto.execution.ExecutionHistoryResponseDTO;
import com.varsql.web.dto.task.TaskExecutionVO;
import com.varsql.web.dto.task.TaskTransferRequestDTO;
import com.varsql.web.dto.task.TaskTransferResponseDTO;
import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.entity.execution.ExecutionHistoryEntity;
import com.varsql.web.model.entity.task.TaskTransferEntity;
import com.varsql.web.repository.execution.ExecutionHistoryEntityRepository;
import com.varsql.web.repository.task.TaskTransferRepository;
import com.varsql.web.scheduler.task.TransferTaskRunner;
import com.varsql.web.util.VarsqlBeanUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;

/**
 * table data mig task service
 * 
 * @author ytkim
 *
 */
@Component
@RequiredArgsConstructor
public class TaskDataMigMgmtService {
	private final static Logger logger = LoggerFactory.getLogger(TaskDataMigMgmtService.class);
	
	private final TaskTransferRepository taskTransferRepository;
	
	private final TransferTaskRunner transferTaskRunner;
	
	private final ExecutionHistoryEntityRepository executionHistoryEntityRepository;
	
	/**
	 * 목록 
	 * 
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult list(SearchParameter searchParameter) {
		Sort sort =Sort.by(Sort.Direction.DESC, AbstractRegAuditorModel.REG_DT);
		
		Page<TaskTransferResponseDTO> result = taskTransferRepository.findAllByNameContaining(searchParameter.getKeyword(), VarsqlUtils.convertSearchInfoToPage(searchParameter, sort));
		
		return VarsqlUtils.getResponseResult(result.getContent(), result.getTotalElements(), searchParameter);
	}

	/**
	 * 정보 저장.  
	 *
	 * @param dto
	 * @return
	 */
	public ResponseResult save(TaskTransferRequestDTO dto) {
		TaskTransferEntity tse;
		
		logger.debug("save :{}", dto);
		
		if(!StringUtils.isBlank(dto.getTaskId())) {
			tse = taskTransferRepository.findByTaskId(dto.getTaskId());
			
			if(tse == null) {
				throw new NotFoundException("id not found : "+ dto.getTaskId());
			}
			BeanUtils.copyProperties(dto.toEntity(), tse, "taskId");
		}else {
			tse = dto.toEntity();
		}
		
		taskTransferRepository.save(tse);
		
		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 * 정보 삭제. 
	 *
	 * @param taskId
	 * @return
	 */
	public ResponseResult remove(String taskId) {
		TaskTransferEntity deleteItem = taskTransferRepository.findByTaskId(taskId);
		
		if(deleteItem == null) {
			throw new NotFoundException("task id not found : "+ taskId);
		}
		
		taskTransferRepository.delete(deleteItem);
		
		return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	/**
	 * 정보 복사 
	 *
	 * @param taskId
	 * @return
	 */
	public ResponseResult copyInfo(String taskId) {
		
		TaskTransferEntity copyInfo = taskTransferRepository.findByTaskId(taskId);
		
		TaskTransferEntity copyEntity = VarsqlBeanUtils.copyEntity(copyInfo);
	    copyEntity.setTaskId(null);
	    copyEntity.setTaskName(copyEntity.getTaskName() + "-copy");
	    
	    taskTransferRepository.save(copyEntity);
	    
	    return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	/**
	 * sql task 실행. 
	 * 
	 * @param taskId
	 * @param string 
	 * @return
	 * @throws SQLException 
	 */
	public ResponseResult execute(String taskId, DataMap reqParam, HttpServletRequest req) throws SQLException {
		ResponseResult result = new ResponseResult();
		
		String progressUid = HttpUtils.getString(req, "progressUid");
		String sessAttrKey = HttpSessionConstants.progressKey(progressUid);
		HttpSession session = req.getSession();
		
		ProgressInfo progressInfo = new ProgressInfo();
		
		session.setAttribute(sessAttrKey, progressInfo);
		
		TaskExecutionVO taskExecutionVO = TaskExecutionVO.builder().taskId(taskId).requid(reqParam.getString(HttpParamConstants.REQ_UID)).runType(ExecuteType.NORMAL).build();
		taskExecutionVO.setProgressInfo(progressInfo);
		TaskResult taskResult;
		try {
			taskResult = transferTaskRunner.run(taskExecutionVO);
		} catch (Exception e) {
			result.setResultCode(VarsqlAppCode.EC_TASK);
			result.setMessage(e.getMessage());
		}
		
		session.setAttribute(sessAttrKey, "complete");
		
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
