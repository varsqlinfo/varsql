package com.varsql.web.app.scheduler.job;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.varsql.core.configuration.Configuration;
import com.varsql.web.app.scheduler.JobType;
import com.varsql.web.app.scheduler.bean.JobBean;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobVO;

@Component
public class BackupFileRemoveJob extends JobBean {
	private final Logger logger = LoggerFactory.getLogger(BackupFileRemoveJob.class);
	
	private final static String BACKUP_PATH = Configuration.getInstance().getBackupPath();
	private final static int BACKUP_EXPIRE_DAY = Configuration.getInstance().backupExpireDay();
	
	@Override
	public JobResultVO doExecute(JobExecutionContext context, JobVO jsv) throws Exception {
		logger.debug("## backup file delete job start expire day: {}, backup path: {}", BACKUP_EXPIRE_DAY, BACKUP_PATH);

		File chkDir = new File(BACKUP_PATH);
		
		File[] files = chkDir.listFiles();
		
		LocalDate currentLdt = LocalDate.now();
		
		for(File chkFile :files) {
			removeExpireFile(chkFile, currentLdt);
		}
		
		logger.debug("## backup file delete job end ## : {}");
		
		return JobResultVO.builder()
				.jobType(JobType.BF_REMOVE)
				.build(); 
	}
	
	
	public void removeExpireFile(File chkFile, LocalDate currentLdt) {
		if(chkFile.isFile()) {
			if(ChronoUnit.DAYS.between(currentLdt, new Date( chkFile.lastModified()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) < BACKUP_EXPIRE_DAY) {
				logger.debug("remove file info :{}", chkFile.getAbsolutePath());
				chkFile.delete();
			}else {
				
			}
		}else {
			// SymbolicLink check
//			boolean isSymbolicLink = Files.isSymbolicLink(chkFile.toPath());
//			if(isSymbolicLink) {
//				
//			}
			
			for(File file :chkFile.listFiles()) {
				removeExpireFile(file, currentLdt);
			}
		}
	}
}