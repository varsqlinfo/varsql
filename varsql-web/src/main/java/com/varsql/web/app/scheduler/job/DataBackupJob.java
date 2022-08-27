package com.varsql.web.app.scheduler.job;
import java.io.File;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.app.database.service.ExportServiceImpl;
import com.varsql.web.app.scheduler.bean.VarsqlJobBean;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.DataExportVO;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobScheduleVO;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.util.ValidateUtils;
import com.vartech.common.utils.DateUtils;
import com.vartech.common.utils.FileUtils;
import com.vartech.common.utils.VartechUtils;

@Component
public class DataBackupJob extends VarsqlJobBean {
	private final Logger logger = LoggerFactory.getLogger(DataBackupJob.class);
	private static String DATA_BACKUP_PATH = Configuration.getInstance().getBackupPath()+File.separator+"databackup";
	
	static {
		File file = new File (DATA_BACKUP_PATH);
		
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	@Autowired
	private ExportServiceImpl exportServiceImpl;
	
	@Autowired
	private DBConnectionEntityRepository dbConnectionEntityRepository; 

	@Override
	public JobResultVO doExecute(JobExecutionContext context, JobScheduleVO jsv) throws Exception {
		logger.debug("## data backup start : {}", jsv);
		
		DatabaseInfo databaseInfo = dbConnectionEntityRepository.findDatabaseInfo(jsv.getVconnid());
		databaseInfo.setMaxSelectCount(-1);
		
		if(databaseInfo ==null) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER ,"Data backup connection info not found vconnid : "+jsv.getVconnid());
		}
		
		DataExportVO dataExportVO = VartechUtils.jsonStringToObject(jsv.getJobData(), DataExportVO.class);
		
		String fileName = "DATA_BACKUP_"+VarsqlDateUtils.currentDateFormat();
		
		File dir = new File(DATA_BACKUP_PATH , VarsqlDateUtils.currentYyyyMM());
		
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		File zipFile = new File(dir, VarsqlFileType.ZIP.concatExtension(ValidateUtils.getValidFileName(fileName)));
		
		logger.debug("data backup file path : {}", zipFile.getAbsolutePath());
		
		
		FileInfoEntity fie = exportServiceImpl.exportTableData(databaseInfo, dataExportVO, FileUtils.writeFileCheck(zipFile));
		
		logger.debug("## data backup end ## ");
		
		return JobResultVO.builder().message(fie.getFilePath()).build(); 
	}
}