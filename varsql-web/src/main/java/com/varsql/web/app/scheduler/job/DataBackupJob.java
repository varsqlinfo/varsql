package com.varsql.web.app.scheduler.job;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.executor.SQLExecuteResult;
import com.varsql.web.app.database.service.ExportServiceImpl;
import com.varsql.web.app.scheduler.JobType;
import com.varsql.web.app.scheduler.bean.JobBean;
import com.varsql.web.dto.DataExportVO;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobScheduleVO;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.util.ValidateUtils;
import com.vartech.common.utils.FileUtils;
import com.vartech.common.utils.VartechUtils;

@Component
public class DataBackupJob extends JobBean {
	private final Logger logger = LoggerFactory.getLogger(DataBackupJob.class);
	private static String BACKUP_PATH = Configuration.getInstance().getBackupPath();
	
	@Autowired
	private ExportServiceImpl exportServiceImpl;
	
	@Autowired
	private DBConnectionEntityRepository dbConnectionEntityRepository; 

	@Override
	public JobResultVO doExecute(JobExecutionContext context, JobScheduleVO jsv) throws Exception {
		logger.debug("## data backup start : {}", jsv);
		
		String vconnid = jsv.getVconnid(); 
		
		DatabaseInfo databaseInfo = dbConnectionEntityRepository.findDatabaseInfo(vconnid);
		
		if(databaseInfo ==null) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER ,"Data backup connection info not found vconnid : "+vconnid);
		}
		
		databaseInfo.setMaxSelectCount(-1);
		
		DataExportVO dataExportVO = VartechUtils.jsonStringToObject(jsv.getJobData(), DataExportVO.class);
		
		String fileName = "(DATA)"+jsv.getJobName()+"-"+VarsqlDateUtils.currentDateFormat();
		
		File dir = new File(FileUtils.pathConcat(BACKUP_PATH, vconnid+"("+databaseInfo.getName()+")", "data", VarsqlDateUtils.currentYyyyMM()));
		
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		File zipFile = new File(dir, VarsqlFileType.ZIP.concatExtension(ValidateUtils.getValidFileName(fileName)));
		
		FileInfoEntity fie = exportServiceImpl.exportTableData(databaseInfo, dataExportVO, FileUtils.writeFileCheck(zipFile));
		
		Map<String, SQLExecuteResult> tableExportCount = fie.getCustomInfo();
		
		List tableLog = new ArrayList();
		
		for (Map.Entry<String, SQLExecuteResult> entry : tableExportCount.entrySet()) {
			String key = entry.getKey();
			SQLExecuteResult val = entry.getValue();
			
			tableLog.add(ImmutableMap.<String, Object>builder()
			        .put("name", key)
			        .put("start", VarsqlDateUtils.timestampFormat(val.getStartTime()))
			        .put("end", VarsqlDateUtils.timestampFormat(val.getEndTime()))
			        .put("delay",VarsqlDateUtils.calcDuration(val.getEndTime() - val.getStartTime()))
			        .put("total", val.getTotalCount())
			        .build());
			
		}
		
		logger.debug("## data backup end ## : {}", zipFile.getAbsolutePath());
		
		return JobResultVO.builder()
				.jobType(JobType.DATA)
				.message("size:"+FileUtils.displaySize(fie.getFileSize())+", path:"+fie.getFileName())
				.log(VartechUtils.objectToJsonString(tableLog))
				.build().addCustomInfo("filePath", fie.getFilePath()); 
	}
}