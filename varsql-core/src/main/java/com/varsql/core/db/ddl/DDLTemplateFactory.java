package com.varsql.core.db.ddl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.varsql.core.db.DBType;
import com.varsql.core.exception.VarsqlRuntimeException;

public class DDLTemplateFactory {
	private final static Logger logger = LoggerFactory.getLogger(DDLTemplateFactory.class);

	private final String TEMPLATE_PACKAGE= "template/ddl/";
	private final String XML_PREFIX= "ddl_";
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

	private void initConfig() {
		String templatePath = getTemplateName(DEFAULT_FILE);
		logger.debug("default ddl template file path : {}", templatePath);
		getDDLParse(DEFAULT_FILE , Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath));

		for(DBType mcb : DBType.values()){
			String venderName = mcb.getDbVenderName();
			templatePath = getTemplateName(venderName);

			logger.debug("{} ddl template file path : {}",  venderName, templatePath);

			getDDLParse(venderName , Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath));
		}
	}

	private String getTemplateName(String venderName){
		return TEMPLATE_PACKAGE + XML_PREFIX + venderName+".xml";
	}
	/**
	 *
	 * @Method Name  : getDDLParse
	 * @Method 설명 : ddl template xml  parsing
	 * @작성일   : 2018. 6. 12.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dbVender
	 * @param is
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void getDDLParse(String dbVender, InputStream is) {
		logger.debug(" dbVender : {}  ,InputStream : {}", dbVender, is);

		if(is == null) return ;

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList ddlNodeList = doc.getElementsByTagName("ddl");

			HashMap<String ,Template> templateInfo = new HashMap<String,Template>();

			for (int i = 0 , len  = ddlNodeList.getLength(); i < len; i++) {
				Element eElement = (Element) ddlNodeList.item(i);

				String id = eElement.getAttribute("id");
				String ddl = eElement.getTextContent().trim();

				templateInfo.put(id, handlebars.compileInline(ddl));
			}

			ddlTemplateInfo.put(dbVender, templateInfo);

			is.close();
		} catch (ParserConfigurationException e) {
			logger.error("ParserConfigurationException: {}", e.getMessage() , e);
		} catch (SAXException e) {
			logger.error("SAXException : {} ",e.getMessage() , e);
		} catch (IOException e) {
			logger.error("IOException : {} ",e.getMessage() , e);
		}finally{
			if(is !=null){
				try{is.close();}catch(Exception e){};
			}
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
