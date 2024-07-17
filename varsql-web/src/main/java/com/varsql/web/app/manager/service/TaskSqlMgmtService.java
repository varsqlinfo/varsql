package com.varsql.web.app.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.varsql.web.dto.task.TaskSqlRequestDTO;
import com.varsql.web.dto.task.TaskSqlResponseDTO;
import com.varsql.web.exception.TaskNotFoundException;
import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.entity.task.TaskSqlEntity;
import com.varsql.web.repository.task.TaskSqlRepository;
import com.varsql.web.util.VarsqlBeanUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.StringUtils;

/**
 * log compoment 관리
* 
* @fileName	: CmpLogMgmtService.java
* @author	: ytkim
 */
@Component
public class TaskSqlMgmtService {
	private final static Logger logger = LoggerFactory.getLogger(TaskSqlMgmtService.class);
	
	@Autowired
	private TaskSqlRepository taskSqlRepository;
	
	public ResponseResult list(SearchParameter searchParameter) {
		Sort sort =Sort.by(Sort.Direction.DESC, AbstractRegAuditorModel.REG_DT);
		
		Page<TaskSqlResponseDTO> result = taskSqlRepository.findAllByNameContaining(searchParameter.getKeyword(), VarsqlUtils.convertSearchInfoToPage(searchParameter, sort));
		
		return VarsqlUtils.getResponseResult(result.getContent(), result.getTotalElements(), searchParameter);
	}

	/**
	 * 정보 저장.  
	 *
	 * @method : save
	 * @param dto
	 * @return
	 */
	public ResponseResult save(TaskSqlRequestDTO dto) {
		TaskSqlEntity tse;
		
		if(!StringUtils.isBlank(dto.getTaskId())) {
			tse = taskSqlRepository.findByTaskId(dto.getTaskId());
			
			if(tse == null) {
				throw new TaskNotFoundException("sql task id not found : "+ dto.getTaskId());
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
	 * @method : remove
	 * @param taskId
	 * @return
	 */
	public ResponseResult remove(String taskId) {
		TaskSqlEntity deleteItem = taskSqlRepository.findByTaskId(taskId);
		
		if(deleteItem == null) {
			throw new TaskNotFoundException("log component id not found : "+ taskId);
		}
		
		taskSqlRepository.delete(deleteItem);
		
		return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	/**
	 * 정보 복사 
	 *
	 * @method : copyInfo
	 * @param taskId
	 * @return
	 */
	public ResponseResult copyInfo(String taskId) {
		
		TaskSqlEntity copyInfo = taskSqlRepository.findByTaskId(taskId);
		
		TaskSqlEntity copyEntity = VarsqlBeanUtils.copyEntity(copyInfo);
	    copyEntity.setTaskId(null);
	    copyEntity.setTaskName(copyEntity.getTaskName() + "-copy");
	    
	    taskSqlRepository.save(copyEntity);
	    
	    return VarsqlUtils.getResponseResultItemOne(TaskSqlResponseDTO.toDto(copyEntity));
	}

}
