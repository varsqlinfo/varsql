package com.varsql.web.common.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.model.entity.app.ExceptionLogEntity;
import com.varsql.web.model.entity.db.DBConnHistEntity;
import com.varsql.web.model.entity.scheduler.JobHistoryEntity;
import com.varsql.web.model.entity.scheduler.JobHistoryLogEntity;
import com.varsql.web.model.entity.sql.SqlHistoryEntity;
import com.varsql.web.model.entity.sql.SqlStatisticsEntity;
import com.varsql.web.repository.db.DBConnHistEntityRepository;
import com.varsql.web.repository.scheduler.JobHistoryEntityRepository;
import com.varsql.web.repository.scheduler.JobHistoryLogEntityRepository;
import com.varsql.web.repository.sql.SqlExceptionLogEntityRepository;
import com.varsql.web.repository.sql.SqlHistoryEntityRepository;
import com.varsql.web.repository.sql.SqlStatisticsEntityRepository;
import com.vartech.common.utils.CommUtils;

import lombok.AllArgsConstructor;

/**
 * 공통 로그 서비스
* 
* @fileName	: CommonLogService.java
* @author	: ytkim
 */
@Service
@AllArgsConstructor
public class CommonLogService{
	private final Logger logger = LoggerFactory.getLogger(CommonLogService.class);
	
	final private SqlExceptionLogEntityRepository sqlExceptionLogEntityRepository;

	final private SqlHistoryEntityRepository sqlHistoryEntityRepository;

	final private SqlStatisticsEntityRepository sqlStatisticsEntityRepository;
	
	final private DBConnHistEntityRepository dbConnHistEntityRepository;
	
	final private JobHistoryEntityRepository scheduleHistoryEntityRepository;
	
	final private JobHistoryLogEntityRepository jobHistoryLogEntityRepository;
	
	/**
	 * error insert
	 *
	 * @method : insertExceptionLog
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
	 * @method : saveSqlHistory
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
	 * @method : sqlLogInsert
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
	 * @method : saveDbConnectionHistory
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
	 *
	 * @method : saveJobHistory
	 * @param jobHistoryEntity
	 */
	@Async(ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
	public void saveJobHistory(JobHistoryEntity jobHistoryEntity) {
		try{
			jobHistoryEntity = scheduleHistoryEntityRepository.save(jobHistoryEntity);
		}catch(Exception e){
			logger.error("saveJobHistory {}", e.getMessage());
		}
	}
	
	@Async(ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
	public void saveJobHistory(JobHistoryEntity jobHistoryEntity, JobHistoryLogEntity jobHistoryLogEntity) {
		try{
			jobHistoryEntity = scheduleHistoryEntityRepository.save(jobHistoryEntity);
			jobHistoryLogEntity.setHistSeq(jobHistoryEntity.getHistSeq());
			jobHistoryLogEntityRepository.save(jobHistoryLogEntity);
		}catch(Exception e){
			logger.error("saveJobHistory {}", e.getMessage());
		}
	}
	
}