package com.varsql.core.sql.template;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jknack.handlebars.Template;
import com.varsql.core.common.TemplateFactory;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.SQLTemplateCode;
import com.varsql.core.sql.TemplateEnum;
import com.vartech.common.io.RESOURCE_TYPE;
import com.vartech.common.io.Resource;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DDLTemplateFactory.java
* @desc		: ddl template load factory
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 9. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SQLTemplateFactory {
	private final Logger logger = LoggerFactory.getLogger(SQLTemplateFactory.class);

	private final String TEMPLATE_PACKAGE= RESOURCE_TYPE.CLASSPATH.getPath("db/template/sql/*.xml");
	//private final String TEMPLATE_PACKAGE= String.format("classpath:db/template/sql/sql_other.xml", SQL_TEMPLATE_PREFIX);
	private final String DEFAULT_FILE= "other";

	private Map<String, HashMap<String,Template>> sqlTemplateInfo = new HashMap<String, HashMap<String,Template>>();
	private Map<String, HashMap<String,String>> sqlTemplateTextInfo = new HashMap<String, HashMap<String,String>>();

	private static class FactoryHolder{
        private static final SQLTemplateFactory instance = new SQLTemplateFactory();
    }

	public static SQLTemplateFactory getInstance() {
		return FactoryHolder.instance;
    }

	private SQLTemplateFactory(){
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

		logger.debug("default sql template file path : {} ", TEMPLATE_PACKAGE);
		Resource[] resources = ResourceUtils.getResources(TEMPLATE_PACKAGE);

		for (Resource resource: resources){
			
			String fileName = resource.getFileName();
			
			logger.debug("sql template fileName : {} ", fileName);

			String dbVender = fileName.replace(".xml", "");

			String xml = ResourceUtils.getResourceString(resource);
			JsonNode rootNode = VartechUtils.xmlToJsonNode(xml, VarsqlConstants.CHAR_SET);

			HashMap<String, Template> templateInfo = new HashMap<String, Template>();
			HashMap<String, String> templateTextInfo = new HashMap<String, String>();
			
			
			for(Class<?> cls :  SQLTemplateCode.class.getDeclaredClasses()) {
				
				String eleName = cls.getSimpleName().toLowerCase(); 
				
				JsonNode eleNode = rootNode.get(eleName);
				
				if(eleNode == null) continue; 
				
				for(Field field :  cls.getDeclaredFields()) {
					if(field.isEnumConstant()) {
						
						JsonNode childNode = eleNode.get(field.getName());
						
						if(childNode == null) continue; 
						
						String addNodeId = eleName+StringUtils.capitalize(field.getName()); 
						String value = StringUtils.trim(childNode.asText());
						
						templateInfo.put(addNodeId, TemplateFactory.getInstance().compileInline(value));
						templateTextInfo.put(addNodeId, value);
					}
				}
			}
			
			sqlTemplateInfo.put(dbVender, templateInfo);
			sqlTemplateTextInfo.put(dbVender, templateTextInfo);
		}
	}

	/**
	 *
	 * @Method Name  : sqlRender
	 * @Method 설명 : query render
	 * @작성일   : 2018. 6. 12.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dbVender
	 * @param templateId
	 * @param param
	 * @return
	 */
	public String sqlRender(DBVenderType dbType, TemplateEnum code, Object param){
		return sqlRender(dbType, code, param, null);
	}
	
	public String sqlRender(DBVenderType dbType, TemplateEnum code, Object param, TemplateEnum defaultCode){
		
		String dbVender= dbType.getDbVenderName();
		
		Template template = getDDLTemplate(dbVender, code.getTemplateId());
		
		if(template == null  && defaultCode != null) {
			template = getDDLTemplate(dbVender, defaultCode.getTemplateId());
		}

		if(template ==null){
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_TEMPLATE_CONFIGURATION ,new StringBuilder().append("sqlRender template ").append("dbVender : [").append(dbVender).append("] templateId : [").append(code.getTemplateId()).append("] template : [").append(template).append("]").toString());
		}

		try {
			return StringUtils.trim(template.apply(param));
		} catch (IOException e) {
			logger.error("sqlRender IOException : {} ",e.getMessage() , e);

		}
		return "";
	}

	public String getTemplate(DBVenderType dbType, TemplateEnum code) {
		String templateId = code.getTemplateId(); 
		
		if(sqlTemplateTextInfo.containsKey(dbType.getDbVenderName())){
			return sqlTemplateTextInfo.get(dbType.getDbVenderName()).get(templateId);
		}

		return sqlTemplateTextInfo.get(DEFAULT_FILE).get(templateId);
	}

	/**
	 *
	 * @Method Name  : getDDLTemplate
	 * @Method 설명 : get sql template
	 * @작성일   : 2018. 6. 12.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dbVender
	 * @param templateId
	 * @return
	 */
	private Template getDDLTemplate(String dbVender ,String templateId){
		if(sqlTemplateInfo.containsKey(dbVender)){
			if(sqlTemplateInfo.get(dbVender).containsKey(templateId)) {
				return sqlTemplateInfo.get(dbVender).get(templateId);
			}
			return sqlTemplateInfo.get(DEFAULT_FILE).get(templateId);
		}
		return sqlTemplateInfo.get(DEFAULT_FILE).get(templateId);
	}

	
}
