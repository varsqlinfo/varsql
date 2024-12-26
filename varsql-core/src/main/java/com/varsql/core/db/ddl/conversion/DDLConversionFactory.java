package com.varsql.core.db.ddl.conversion;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.db.DBVenderType;
import com.vartech.common.io.Resource;
import com.vartech.common.utils.VartechUtils;

/**
 * DDL conversion factory
 * 
 * @author ytkim
 *
 */
public class DDLConversionFactory {
	private final Logger logger = LoggerFactory.getLogger(DDLConversionFactory.class);

	private final String TEMPLATE_PACKAGE= "classpath:db/conversion/*DataType.json";

	private Map<String, ConversionInfo> ddlConversionTypeMap = new HashMap<String, ConversionInfo>();

	private DDLConversionFactory(){
		initialize();
	}

	protected void initialize() {
		try{
			initConfig();
		}catch(Exception e){
			logger.error(this.getClass().getName(), e);
			throw new RuntimeException(e);
		}
	}
	
	private void initConfig() throws IOException {

		logger.debug("default conversion file path : {} ", TEMPLATE_PACKAGE);
		Resource[] resources = ResourceUtils.getResources(TEMPLATE_PACKAGE);

		for (Resource resource: resources){
			logger.debug("conversion resource : {} ", resource.getFileName());
			String fileName = resource.getFileName();

			String dbVender = fileName.replace("DataType.json", "");

			String json = ResourceUtils.getResourceString(resource);

			ConversionInfo conversionInfo = VartechUtils.jsonStringToObject(json, ConversionInfo.class);
			
			ddlConversionTypeMap.put(dbVender, conversionInfo);
		}
	}
	
	private static class FactoryHolder{
        private static final DDLConversionFactory instance = new DDLConversionFactory();
    }

	public static DDLConversionFactory getInstance() {
		return FactoryHolder.instance;
    }
	
	
	public ConversionType getConversionType(DBVenderType source, DBVenderType target, String typeName) {
		typeName = typeName.toUpperCase();
		
		if(!ddlConversionTypeMap.containsKey(source.getDbVenderName())){
			return null; 
		}
		
		if(!ddlConversionTypeMap.get(source.getDbVenderName()).containsKey(typeName)) {
			return null;
		}
		
		ConversionType conversionType = null;
		if(ddlConversionTypeMap.get(source.getDbVenderName()).get(typeName).containsKey(target.getDbVenderName())) {
			conversionType = ddlConversionTypeMap.get(source.getDbVenderName()).get(typeName).get(target.getDbVenderName());
		}else {
			conversionType = ddlConversionTypeMap.get(source.getDbVenderName()).get(typeName).get("default");
		}
		
		return conversionType == null ?  null : conversionType; 
	}
}
