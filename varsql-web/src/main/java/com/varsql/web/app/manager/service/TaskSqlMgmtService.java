package com.varsql.web.app.manager.service;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.exception.NotFoundException;
import com.varsql.core.sql.util.SQLUtils;
import com.varsql.core.task.Task;
import com.varsql.core.task.sql.SQLTask;
import com.varsql.web.dto.task.TaskSqlRequestDTO;
import com.varsql.web.dto.task.TaskSqlResponseDTO;
import com.varsql.web.exception.DatabaseNotFoundException;
import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.entity.task.TaskSqlEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.task.TaskSqlRepository;
import com.varsql.web.util.VarsqlBeanUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.StringUtils;

/**
 * sql task service
 * 
 * @author ytkim
 *
 */
@Component
public class TaskSqlMgmtService {
	private final static Logger logger = LoggerFactory.getLogger(TaskSqlMgmtService.class);
	
	@Autowired
	private TaskSqlRepository taskSqlRepository;
	
	@Autowired
	private DBConnectionEntityRepository dbConnectionEntityRepository;
	
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
	 * @param string 
	 * @return
	 * @throws SQLException 
	 */
	public ResponseResult execute(String taskId, DataMap reqParam, String ip) {
		ResponseResult result = new ResponseResult();
		
		TaskSqlEntity item = taskSqlRepository.findByTaskId(taskId);
		
		if(item == null) {
			throw new NotFoundException(String.format("[%s] task not found ",  taskId));
		}
		
		DatabaseInfo dbinfo = dbConnectionEntityRepository.findDatabaseInfo(item.getVconnid());
		
		if(dbinfo==null) {
			throw new DatabaseNotFoundException(String.format("[%s] db not found ", item.getVconnid()));
		}
		
		SqlStatementInfo ssi = new SqlStatementInfo();
		
		ssi.setDatabaseInfo(dbinfo);
		ssi.setSql(item.getSql());
		ssi.setSqlParamMap(SQLUtils.stringParamListToMap(item.getParameter()));
		
		ssi.setLimit(-1);

		Task sqlTask = new SQLTask(ssi);
		
		try {
			sqlTask.submit();
			result = sqlTask.result().getCustomResult();
		} catch (Exception e) {
			result = ResponseResult.builder().status(RequestResultCode.ERROR.getCode()).message(e.getMessage()).build();
		}
		
		return result; 
	}

}
