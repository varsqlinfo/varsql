package com.varsql.web.app.scheduler.job;
import java.io.File;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.app.database.service.ExportServiceImpl;
import com.varsql.web.app.scheduler.JobType;
import com.varsql.web.app.scheduler.bean.JobBean;
import com.varsql.web.dto.DDLExportItemVO;
import com.varsql.web.dto.DDLExportVO;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobVO;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.util.ValidateUtils;
import com.vartech.common.utils.FileUtils;
import com.vartech.common.utils.VartechUtils;

@Component
public class DDLBackupJob extends JobBean {
	
	private final Logger logger = LoggerFactory.getLogger(DDLBackupJob.class);
	
	private final static String BACKUP_PATH = Configuration.getInstance().getBackupPath();
	
	@Autowired
	private ExportServiceImpl exportServiceImpl;
	
	@Autowired
	private DBConnectionEntityRepository dbConnectionEntityRepository; 

	@Override
	public JobResultVO doExecute(JobExecutionContext context, JobVO jsv) throws Exception{
		logger.debug("## ddl backup start : {}", jsv);
		
		String vconnid = jsv.getVconnid(); 
		
		DatabaseInfo databaseInfo = dbConnectionEntityRepository.findDatabaseInfo(vconnid);
		
		if(databaseInfo ==null) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER ,"Data backup connection info not found vconnid : "+vconnid);
		}
		
		databaseInfo.setMaxSelectCount(-1);
		
		DDLExportVO ddlExportVO = VartechUtils.jsonStringToObject(jsv.getJobData(), DDLExportVO.class);
		
		String fileName = "(DDL)"+jsv.getJobName()+"-"+VarsqlDateUtils.currentDateFormat();
		
		File dir = new File(FileUtils.pathConcat(BACKUP_PATH, vconnid+"("+databaseInfo.getName()+")", "ddl", VarsqlDateUtils.currentYyyyMM()));
		
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		File zipFile = new File(dir, VarsqlFileType.ZIP.concatExtension(ValidateUtils.getValidFileName(fileName)));
		
		logger.debug("ddl backup file path : {}", zipFile.getAbsolutePath());
		
		for (DDLExportItemVO ddoExportItemVO : ddlExportVO.getExportItems()) {
			ddoExportItemVO.setAllObjectExport(true);
		}
		
		FileInfoEntity fie = exportServiceImpl.exportDDLInfo(databaseInfo, ddlExportVO, FileUtils.writeFileCheck(zipFile));
		
		logger.debug("## ddl backup end ## : {}", zipFile.getAbsolutePath());
		
		return JobResultVO.builder().jobType(JobType.DDL).message(fie.getFileName()).build().addCustomInfo("filePath", fie.getFilePath()); 
	}
}