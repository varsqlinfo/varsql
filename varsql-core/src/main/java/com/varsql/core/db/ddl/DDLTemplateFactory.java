package com.varsql.core.db.ddl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.exception.VarsqlRuntimeException;
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

	private final String TEMPLATE_PACKAGE= "classpath:db/template/ddl/ddl_*.xml";
	private final String DEFAULT_FILE= "other";

	private Map<String ,HashMap<String,Template>> ddlTemplateInfo = new HashMap<String,HashMap<String,Template>>();

	private Handlebars handlebars;

	private DDLTemplateFactory(){
		handlebars = new Handlebars();
		handlebars.registerHelper("equals", euqalsHelper());

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
		Resource[] resources = ResourceUtils.getPackageResources(TEMPLATE_PACKAGE);

		for (Resource resource: resources){

			String fileName = resource.getFilename();

			fileName = fileName.replaceFirst("ddl_", "");
			String dbVender = fileName.replace(".xml", "");


			String xml = ResourceUtils.getResourceString(resource);
			JsonNode rootNode = VartechUtils.xmlToJsonNode(xml, VarsqlConstants.CHAR_SET);

			Iterator<String> elements= rootNode.fieldNames();

			HashMap<String ,Template> templateInfo = new HashMap<String,Template>();

			while(elements.hasNext()){
				String node =elements.next();
				templateInfo.put(node, handlebars.compileInline(StringUtils.trim(rootNode.get(node).asText())));
			}

			ddlTemplateInfo.put(dbVender, templateInfo);
		}
	}

	/**
	 *
	 * @Method Name  : ddlRender
	 * @Method 설명 : query render
	 * @작성일   : 2018. 6. 12.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dbVender
	 * @param templateId
	 * @param param
	 * @return
	 */
	public String ddlRender(String dbVender ,String templateId , Map param){
		Template template = getDDLTemplate( dbVender, templateId);

		if(template ==null){
			throw new VarsqlRuntimeException(new StringBuilder().append("ddlRender template ").append("dbVender : [").append(dbVender).append("] templateId : [").append(templateId).append("] template : [").append(template).append("]").toString());
		}

		try {
			return template.apply(param);
		} catch (IOException e) {
			logger.error("ddlRender IOException : {} ",e.getMessage() , e);

		}
		return "";
	}

	/**
	 *
	 * @Method Name  : getDDLTemplate
	 * @Method 설명 : get ddl template
	 * @작성일   : 2018. 6. 12.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dbVender
	 * @param templateId
	 * @return
	 */
	private Template getDDLTemplate(String dbVender ,String templateId){
		if(!ddlTemplateInfo.containsKey(dbVender)){
			return ddlTemplateInfo.get(DEFAULT_FILE).get(templateId);
		}else{
			if(!ddlTemplateInfo.get(dbVender).containsKey(templateId)){
				return ddlTemplateInfo.get(DEFAULT_FILE).get(templateId);
			}
		}

		return ddlTemplateInfo.get(dbVender).get(templateId);
	}

	private static Helper<Object> euqalsHelper() {
	    return new Helper<Object>() {
	    	@Override
			public Object apply(Object arg0, Options options) throws IOException {
				Object obj1 = options.param(0);
		        return Objects.equals(obj1, arg0) ? options.fn() : options.inverse();
			}
	    };
	}

	private static class FactoryHolder{
        private static final DDLTemplateFactory instance = new DDLTemplateFactory();
    }

	public static DDLTemplateFactory getInstance() {
		return FactoryHolder.instance;
    }
}
