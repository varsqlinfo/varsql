package com.varsql.core.changeset;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.varsql.core.changeset.beans.ChangeSetInfo;
import com.varsql.core.changeset.beans.ChangeSql;
import com.varsql.core.changeset.parser.ChangesetXmlParser;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.connection.beans.ConnectionInfo;

public class DatabaseChangeExecutor {
	
	private final Logger logger = LoggerFactory.getLogger(DatabaseChangeExecutor.class);
	
	private ConnectionInfo connectionInfo;
	
	public DatabaseChangeExecutor(ConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}
	
	public static void main(String[] args) {
		new DatabaseChangeExecutor(Configuration.getInstance().getVarsqlDB()).execute();
	}

	private void execute() {
		Resource[] changeXmls = null;
		try {
			
			changeXmls = ResourceUtils.getResources(String.format("%sconfig/db/changeset/%s/*.xml", ResourceUtils.CLASS_PREFIX, connectionInfo.getType()));
			System.out.println(String.format("%sconfig/db/changeset/%s/*.xml", ResourceUtils.CLASS_PREFIX, connectionInfo.getType()) + " ;; "+ connectionInfo);
		} catch (IOException e) {
			logger.error("database changeset not found : {}", e.getMessage());
		}
		
		ChangeSetInfo[] changeSets = new ChangeSetInfo[changeXmls.length];
		
		for (int i = 0; i < changeXmls.length; i++) {
			Resource xml = changeXmls[i];
			String[] fileNameArr =xml.getFilename().replaceAll("\\.(xml|json|yaml|sql)$", "").split("_"); 
	    	String type = fileNameArr[0];
	    	int version = Integer.parseInt(fileNameArr[1]);
	    	
	    	ChangeSetInfo changeSetInfo= ChangeSetInfo.builder()
	    		.type(type)
	    		.version(version)
	    		.resource(xml)
	    		.applySqls(getApplyList(xml))
	    	.build();
	    	
	    	changeSets[i] = changeSetInfo;
		}
		
		if (changeXmls != null && changeXmls.length > 0) {
			Arrays.sort(changeSets, new Comparator<ChangeSetInfo>() {
			    @Override
			    public int compare(ChangeSetInfo i1, ChangeSetInfo i2) {
			    	
			    	if ((i1.getType()).equals(i2.getType())) {  
	                    return i1.getVersion()-i2.getVersion();
	                }else{
	                    return i1.getType().compareTo(i2.getType());
	                }
			    }
			});
		}
		
		for (int i = 0; i < changeSets.length; i++) {
			ChangeSetInfo xml = changeSets[i];
			xml.getApplySqls().stream().forEach(System.out::println);
		}
		
	}

	private List<ChangeSql> getApplyList(Resource xml) {
		return new ChangesetXmlParser(xml).parser();
	}
}
