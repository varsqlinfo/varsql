package com.varsql.web.common.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.web.app.user.service.UserNoteServiceImpl;
import com.varsql.web.constants.MessageType;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.execution.ExecutionHistoryResponseDTO;
import com.varsql.web.dto.scheduler.JobDetailDTO;
import com.varsql.web.dto.user.NoteRequestDTO;
import com.varsql.web.dto.user.RegInfoDTO;
import com.varsql.web.dto.websocket.MessageDTO;
import com.varsql.web.model.entity.app.ExceptionLogEntity;
import com.varsql.web.model.entity.db.DBConnHistEntity;
import com.varsql.web.model.entity.execution.ExecutionHistoryEntity;
import com.varsql.web.model.entity.execution.ExecutionHistoryLogEntity;
import com.varsql.web.model.entity.sql.SqlHistoryEntity;
import com.varsql.web.model.entity.sql.SqlStatisticsEntity;
import com.varsql.web.model.mapper.execution.ExecutionHistoryMapper;
import com.varsql.web.repository.db.DBConnHistEntityRepository;
import com.varsql.web.repository.execution.ExecutionHistoryEntityRepository;
import com.varsql.web.repository.execution.ExecutionHistoryLogEntityRepository;
import com.varsql.web.repository.sql.SqlExceptionLogEntityRepository;
import com.varsql.web.repository.sql.SqlHistoryEntityRepository;
import com.varsql.web.repository.sql.SqlStatisticsEntityRepository;
import com.vartech.common.utils.CommUtils;
import com.vartech.common.utils.DateUtils;
import com.vartech.common.utils.VartechUtils;

import lombok.RequiredArgsConstructor;

/**
 * 공통 로그 서비스
* 
* @fileName	: CommonLogService.java
* @author	: ytkim
 */
@Service
@RequiredArgsConstructor
public class CommonLogService{
	private final Logger logger = LoggerFactory.getLogger(CommonLogService.class);
	
	private final SqlExceptionLogEntityRepository sqlExceptionLogEntityRepository;

	private final SqlHistoryEntityRepository sqlHistoryEntityRepository;

	private final SqlStatisticsEntityRepository sqlStatisticsEntityRepository;
	
	private final DBConnHistEntityRepository dbConnHistEntityRepository;
	
	private final ExecutionHistoryEntityRepository scheduleHistoryEntityRepository;
	
	private final ExecutionHistoryLogEntityRepository executionHistoryLogEntityRepository;
	
	private final UserNoteServiceImpl userNoteServiceImpl;
	
	/**
	 * error log insert
	 *
	 * @param exceptionType
	 * @param e
	 */
	@Async(ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
	public void insertExceptionLog(String exceptionType, Throwable e) {
		try{
			String exceptionTitle = e.getMessage();
			
			sqlExceptionLogEntityRepository.save(ExceptionLogEntity.builder()
					.excpType(exceptionType)
					.excpCont(CommUtils.getExceptionStr(e).substring(0 , 2000))
					.excpTitle(exceptionTitle.length() > 1500 ?exceptionTitle.substring(0,1500) :  exceptionTitle)
					.serverId(CommUtils.getHostname()).build());
		}catch(Throwable e1) {
			logger.error("insertExceptionLog Cause : {}", e1.getMessage());
		}
	}

	/**
	 * sql history 저장.
	 *
	 * @param sqlHistoryEntity
	 */
	@Async(ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
	public void saveSqlHistory(SqlHistoryEntity sqlHistoryEntity) {
		try {
			sqlHistoryEntityRepository.save(sqlHistoryEntity);
		}catch(Throwable e) {
			logger.error("sqlData sqlHistoryEntity : {}", e.getMessage());
		}
	}

	/**
	 * 사용자 sql 로그 저장
	 *
	 * @param allSqlStatistics
	 */
	@Async(ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
	public void sqlLogInsert(List<SqlStatisticsEntity> allSqlStatistics) {
		try{
			if(allSqlStatistics.size() == 1) {
				sqlStatisticsEntityRepository.save(allSqlStatistics.get(0));
			}else if(allSqlStatistics.size() > 1) {
				sqlStatisticsEntityRepository.saveAll(allSqlStatistics);
			}
	    }catch(Exception e){
	    	logger.error("sqlLogInsert {}", e.getMessage());
	    }
	}
	
	/**
	 * 사용자 접속 로그 저장
	 *
	 * @param dbConnHistEntity
	 */
	@Async(ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
	public void saveDbConnectionHistory(DBConnHistEntity dbConnHistEntity) {
		try{
			dbConnHistEntityRepository.save(dbConnHistEntity);
		}catch(Exception e){
			logger.error("saveDbConnectionHistory {}", e.getMessage());
		}
	}
	
	/**
	 * 실행 이력 등록.
	 * @param regInfoDTO 
	 * 
	 * @param executionHistoryEntity
	 * @param executionHistoryLogEntity 
	 */
	public void saveExecutionHistory(MessageDTO messageDTO, ExecutionHistoryEntity executionHistoryEntity) {
		this.saveExecutionHistory(messageDTO, executionHistoryEntity, null);
	}
	
	/**
	 * 실행 이력 등록. 
	 * 
	 * @param executionHistoryEntity
	 * @param executionHistoryLogEntity
	 */
	@Async(ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
	public void saveExecutionHistory(MessageDTO messageDTO, ExecutionHistoryEntity executionHistoryEntity, ExecutionHistoryLogEntity executionHistoryLogEntity) {
		try{
			executionHistoryEntity = scheduleHistoryEntityRepository.save(executionHistoryEntity);
			if(executionHistoryLogEntity != null) {
				executionHistoryLogEntity.setHistSeq(executionHistoryEntity.getHistSeq());
				executionHistoryLogEntityRepository.save(executionHistoryLogEntity);
			}
			
			ExecutionHistoryResponseDTO dto = ExecutionHistoryMapper.INSTANCE.toDto(executionHistoryEntity);
			
			NoteRequestDTO noteInfo = new NoteRequestDTO();
			noteInfo.setNoteType(MessageType.fromString(dto.getTargetType()));
			noteInfo.setNoteTitle(messageDTO.getTitle());
			noteInfo.setNoteCont(VartechUtils.objectToJsonString(dto));
			
			userNoteServiceImpl.insertSendNoteInfo(noteInfo, messageDTO.getRecvIds(), false);
			
		}catch(Exception e){
			logger.error("saveExecutionHistory {}", e.getMessage());
		}
	}
}