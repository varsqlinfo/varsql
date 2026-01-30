package com.varsql.web.scheduler.job;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.varsql.core.configuration.Configuration;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.scheduler.JobVO;
import com.varsql.web.scheduler.JobType;
import com.varsql.web.scheduler.bean.JobBean;

@Component
public class BackupFileRemoveJob extends JobBean {
	private final Logger logger = LoggerFactory.getLogger(BackupFileRemoveJob.class);
	
	private final static String BACKUP_PATH = Configuration.getInstance().getBackupPath();
	private final static int BACKUP_CLEANUP_DAY = Configuration.getInstance().getBackupCleanupDay();
	private static final Set<Integer> BACKUP_CLEANUP_EXCLUDE_DAYS;
	
	static {
		BACKUP_CLEANUP_EXCLUDE_DAYS = Arrays.stream(Configuration.getInstance().getBackupCleanupExcludeDays().split(","))
	              .map(String::trim)
	              .filter(s -> !s.isEmpty())
	              .map(Integer::parseInt)
	              .collect(Collectors.toSet());
	}
	
	@Override
	public JobResultVO doExecute(JobExecutionContext context, JobVO jsv) throws Exception {
		logger.debug("## backup file delete job start cleanup day: {}, backup path: {}, BACKUP_CLEANUP_EXCLUDE_DAYS : {}", BACKUP_CLEANUP_DAY, BACKUP_PATH, BACKUP_CLEANUP_EXCLUDE_DAYS);

		File chkDir = new File(BACKUP_PATH);
		
		if(!chkDir.exists()) {
			return JobResultVO.builder()
					.jobType(JobType.BACKUP_FILE_REMOVE)
					.message("Folder not exists")
					.build();
		}
		
		File[] files = chkDir.listFiles();
		
		LocalDate currentLdt = LocalDate.now();
		
		if(!isExcludeDay(currentLdt)) {
			for(File chkFile :files) {
				deleteFile(chkFile, currentLdt);
			}
		}
		
		logger.debug("## backup file delete job end ##");
		
		return JobResultVO.builder()
				.jobType(JobType.BACKUP_FILE_REMOVE)
				.build(); 
	}
	
	
	public void deleteFile(File chkFile, LocalDate currentLdt) {
		if(chkFile.isFile()) {
			if(ChronoUnit.DAYS.between(currentLdt, new Date( chkFile.lastModified()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) < BACKUP_CLEANUP_DAY) {
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
				deleteFile(file, currentLdt);
			}
			
			if(chkFile.listFiles().length < 1) {
				chkFile.delete();
			}
		}
	}
	
	private boolean isExcludeDay(LocalDate currentDt) {

	    int today = currentDt.getDayOfMonth();
	    int lastDayOfMonth = currentDt.lengthOfMonth();

	    for (Integer excludeDay : BACKUP_CLEANUP_EXCLUDE_DAYS) {
	        // 매월 마지막 날
	        if (excludeDay == 31 && today == lastDayOfMonth) {
	            return true;
	        }

	        // 일반 일자
	        if (excludeDay != 31 && today == excludeDay) {
	            return true;
	        }
	    }
	    return false;
	}
}