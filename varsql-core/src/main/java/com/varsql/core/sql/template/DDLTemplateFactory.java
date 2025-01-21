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
import com.varsql.core.sql.DDLTemplateCode;
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
public class DDLTemplateFactory {
	private final Logger logger = LoggerFactory.getLogger(DDLTemplateFactory.class);

	private final String TEMPLATE_PACKAGE= RESOURCE_TYPE.CLASSPATH.getPath("db/template/ddl/*.xml");
	//private final String TEMPLATE_PACKAGE= String.format("classpath:db/template/sql/sql_other.xml", SQL_TEMPLATE_PREFIX);
	private final String DEFAULT_FILE= "other";

	private Map<String, HashMap<String,Template>> ddlTemplateInfo = new HashMap<String, HashMap<String,Template>>();
	private Map<String, HashMap<String,String>> ddlTemplateTextInfo = new HashMap<String, HashMap<String,String>>();

	private static class FactoryHolder{
        private static final DDLTemplateFactory instance = new DDLTemplateFactory();
    }

	public static DDLTemplateFactory getInstance() {
		return FactoryHolder.instance;
    }

	private DDLTemplateFactory(){
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

		logger.debug("default ddl template file path : {} ", TEMPLATE_PACKAGE);
		Resource[] resources = ResourceUtils.getResources(TEMPLATE_PACKAGE);

		for (Resource resource: resources){
			
			String fileName = resource.getFileName();

			String dbVender = fileName.replace(".xml", "");
			
			if(ddlTemplateInfo.containsKey(dbVender)) {
				logger.warn("The {} already exists. file name : {}, path : {}", dbVender, fileName, resource.getPath());
				continue; 
			}else {
				logger.debug("ddl template fileName : {}, path : {}", fileName, resource.getPath());
			}
			
			String xml = ResourceUtils.getResourceString(resource);
			JsonNode rootNode = VartechUtils.xmlToJsonNode(xml, VarsqlConstants.CHAR_SET);

			HashMap<String, Template> templateInfo = new HashMap<String, Template>();
			HashMap<String, String> templateTextInfo = new HashMap<String, String>();
			
			
			for(Class<?> cls :  DDLTemplateCode.class.getDeclaredClasses()) {
				
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
			
			ddlTemplateInfo.put(dbVender, templateInfo);
			ddlTemplateTextInfo.put(dbVender, templateTextInfo);
		}
	}

	/**
	 *
	 * @Method Name  : render
	 * @Method 설명 : query render
	 * @작성일   : 2018. 6. 12.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dbVender
	 * @param templateId
	 * @param param
	 * @return
	 */
	public String render(DBVenderType dbType, TemplateEnum code, Object param){
		return render(dbType, code, param, null);
	}
	
	public String render(DBVenderType dbType, TemplateEnum code, Object param, TemplateEnum defaultCode){
		
		String dbVender= dbType.getDbVenderName();
		
		Template template = getDDLTemplate(dbVender, code.getTemplateId());
		
		if(template == null  && defaultCode != null) {
			template = getDDLTemplate(dbVender, defaultCode.getTemplateId());
		}

		if(template ==null){
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_TEMPLATE_CONFIGURATION ,new StringBuilder().append("ddlRender template ").append("dbVender : [").append(dbVender).append("] templateId : [").append(code.getTemplateId()).append("] template : [").append(template).append("]").toString());
		}

		try {
			return StringUtils.trim(template.apply(param));
		} catch (IOException e) {
			logger.error("ddlRender IOException : {} ",e.getMessage() , e);

		}
		return "";
	}

	public String getTemplate(DBVenderType dbType, TemplateEnum code) {
		String templateId = code.getTemplateId(); 
		
		if(ddlTemplateTextInfo.containsKey(dbType.getDbVenderName())){
			return ddlTemplateTextInfo.get(dbType.getDbVenderName()).get(templateId);
		}

		return ddlTemplateTextInfo.get(DEFAULT_FILE).get(templateId);
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
		if(ddlTemplateInfo.containsKey(dbVender)){
			if(ddlTemplateInfo.get(dbVender).containsKey(templateId)) {
				return ddlTemplateInfo.get(dbVender).get(templateId);
			}
			return ddlTemplateInfo.get(DEFAULT_FILE).get(templateId);
		}
		return ddlTemplateInfo.get(DEFAULT_FILE).get(templateId);
	}

	
}
