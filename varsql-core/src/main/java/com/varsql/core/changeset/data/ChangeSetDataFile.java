package com.varsql.core.changeset.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.changeset.beans.ChangeLogDTO;
import com.varsql.core.configuration.Configuration;
import com.vartech.common.utils.StringUtils;

public class ChangeSetDataFile implements ChangeSetData{
	
	private final Logger logger = LoggerFactory.getLogger(ChangeSetDataFile.class);
	
	
	private final String LOG_NAME_PREFIX = "db-change-";
	private final String LOG_NAME_SUFFIX = ".log";
	
	/**
	 * log keys
	 */
	private String[] LOG_KEYS = {"HASH", "FILENAME","ID","TYPE","VERSION","STATE"};
	
	private String logPath;
	
	private String logFileName; 
	
	public ChangeSetDataFile(String logPath) {
		this.logPath = logPath;
		this.logFileName = LOG_NAME_PREFIX+new SimpleDateFormat("yyyyMMdd").format(new Date())+LOG_NAME_SUFFIX;
		
		if(StringUtils.isBlank(logPath)) {
			String installRoot = Configuration.getInstance().getInstallRoot();
			
			File dir = new File(installRoot, "changeSetResult");
			if(!dir.exists()) {
				dir.mkdir();
			}
			
			String changeLogPath = dir.getAbsolutePath();
			
			changeLogPath= changeLogPath.replaceAll("\\\\", "/");
			
			if(changeLogPath.endsWith("/")) {
				changeLogPath = changeLogPath.substring(0, changeLogPath.length()-1);
			}
			
			this.logPath = changeLogPath;
		}
		
		logger.info("change log history path : {} ", this.logPath);
	}

	@Override
	public Map history() throws Exception {
		
		List<File> changeLogFiles =Arrays.asList( new File(this.logPath).listFiles()).stream().filter(item->{
			return item.getName().startsWith(LOG_NAME_PREFIX) && item.getName().endsWith(LOG_NAME_SUFFIX);
		}) .collect(Collectors.toList());
		
		Collections.sort(changeLogFiles, (o1, o2) -> {
			return o1.getName().compareTo(o2.getName());    
		});
		
		Map changeLogInfo = new LinkedHashMap<>();
		
		for(File file : changeLogFiles) {
			
	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	            String line;
	            Map item = new HashMap();
	            while ((line = br.readLine()) != null) {
	                String [] logArr = line.split(";;");
	                
	                item = new HashMap();
	                
	                for(int i =0 ;i <logArr.length; i++) {
	                	item.put(LOG_KEYS[i], logArr[i]);
	                }
	                
	                changeLogInfo.put(item.get("HASH"), item);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		
		return changeLogInfo; 
	}


	@Override
	public boolean addLog(ChangeLogDTO dto) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(dto.getHash()).append(";;");
		sb.append(dto.getFileName()).append(";;");
		sb.append(dto.getId()).append(";;");
		sb.append(dto.getType()).append(";;");
		sb.append(dto.getVersion()).append(";;");
		sb.append(dto.getState()).append(";;").append( System.lineSeparator());
		
		File file = new File(this.logPath, this.logFileName); 
		
		if(file.exists()) {
			Files.write(file.toPath(), sb.toString().getBytes(), StandardOpenOption.APPEND);
		}else {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
			    bw.write(sb.toString());
			} catch (IOException e) {
			    e.printStackTrace();
			}

		}
		
		
		
		return true; 
	}
	
	
}
