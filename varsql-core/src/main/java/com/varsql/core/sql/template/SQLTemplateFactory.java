package com.varsql.core.sql.template;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
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
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.SQLTemplateCode;
import com.varsql.core.sql.SQLTemplateEnum;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

/**
 * -----------------------------------------------------------------------------
* @fileName		: SQLTemplateFactory.java
* @desc		: sql template load factory
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 9. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SQLTemplateFactory {
	private final Logger logger = LoggerFactory.getLogger(SQLTemplateFactory.class);
	private final String SQL_TEMPLATE_PREFIX = "sql_";

	private final String TEMPLATE_PACKAGE= String.format("classpath:db/template/sql/%s*.xml", SQL_TEMPLATE_PREFIX);
	//private final String TEMPLATE_PACKAGE= String.format("classpath:db/template/sql/sql_other.xml", SQL_TEMPLATE_PREFIX);
	private final String DEFAULT_FILE= "other";

	private Map<String, HashMap<String,Template>> sqlTemplateInfo = new HashMap<String, HashMap<String,Template>>();
	private Map<String, HashMap<String,String>> sqlTemplateTextInfo = new HashMap<String, HashMap<String,String>>();

	private Handlebars handlebars;

	private SQLTemplateFactory(){

		handlebars = new Handlebars();

		handlebars.setPrettyPrint(true);
		handlebars.registerHelper("equals", euqalsHelper());
		handlebars.registerHelper("xif", xifHelper());

		for(TemplateHelpers helper : TemplateHelpers.values()) {
			handlebars.registerHelper(helper.name(), helper);
		}

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
	
	public static void main(String[] args) {
		System.out.println(SQLTemplateFactory.getInstance().getTemplate(DBVenderType.OTHER, SQLTemplateCode.TABLE.create));
	}

	private void initConfig() throws IOException {

		logger.debug("default sql template file path : {} ", TEMPLATE_PACKAGE);
		Resource[] resources = ResourceUtils.getPackageResources(TEMPLATE_PACKAGE);

		for (Resource resource: resources){

			String fileName = resource.getFilename();

			fileName = fileName.replaceFirst(SQL_TEMPLATE_PREFIX, "");
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
						
						templateInfo.put(addNodeId, handlebars.compileInline(value));
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
	public String sqlRender(DBVenderType dbType, SQLTemplateEnum code, Map param){
		String dbVender= dbType.getDbVenderName();
		
		String templateId = code.getTemplateId(); 
		Template template = getDDLTemplate( dbVender, templateId);

		if(template ==null){
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_TEMPLATE_CONFIGURATION ,new StringBuilder().append("sqlRender template ").append("dbVender : [").append(dbVender).append("] templateId : [").append(templateId).append("] template : [").append(template).append("]").toString());
		}

		try {
			return template.apply(param);
		} catch (IOException e) {
			logger.error("sqlRender IOException : {} ",e.getMessage() , e);

		}
		return "";
	}

	public String getTemplate(DBVenderType dbType, SQLTemplateEnum code) {
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
			return sqlTemplateInfo.get(dbVender).get(templateId);
		}

		return sqlTemplateInfo.get(DEFAULT_FILE).get(templateId);
	}

	private static class FactoryHolder{
        private static final SQLTemplateFactory instance = new SQLTemplateFactory();
    }

	public static SQLTemplateFactory getInstance() {
		return FactoryHolder.instance;
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

	private static Helper<Object> xifHelper() {
		return new Helper<Object>() {
			@Override
			public Object apply(Object arg0, Options options) throws IOException {
				Object obj1 = options.param(0);
				Object obj2 = options.param(1);

				if("==".equals(obj1)) {
					return Objects.equals(arg0,obj2) ? options.fn() : options.inverse();
				}

				if("!=".equals(obj1)) {
					return !Objects.equals(arg0,obj2) ? options.fn() : options.inverse();
				}

				if("<".equals(obj1)) {
					return Integer.parseInt(arg0.toString()) < Integer.parseInt(obj2.toString()) ? options.fn() : options.inverse();
				}

				if("<=".equals(obj1)) {
					return Integer.parseInt(arg0.toString()) <= Integer.parseInt(obj2.toString()) ? options.fn() : options.inverse();
				}

				if(">".equals(obj1)) {
					return Integer.parseInt(arg0.toString()) > Integer.parseInt(obj2.toString()) ? options.fn() : options.inverse();
				}

				if(">=".equals(obj1)) {
					return Integer.parseInt(arg0.toString()) >= Integer.parseInt(obj2.toString()) ? options.fn() : options.inverse();
				}
				return options.inverse();
			}
		};
	}
}
