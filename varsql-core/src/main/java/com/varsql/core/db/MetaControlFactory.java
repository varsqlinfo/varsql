package com.varsql.core.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.db.meta.MetaBeanConfig;

/**
 *
 * @FileName  : MetaControlFactory.java
 * @프로그램 설명 : db 메타 컨트롤 factory
 * @Date      : 2019. 11. 26.
 * @작성자      : ytkim
 * @변경이력 :
 */
public final class MetaControlFactory {

	private static Logger logger = LoggerFactory.getLogger(MetaControlFactory.class);
	private final static Map<String, MetaBeanConfig> ALL_CONFIG_BEAN = new HashMap<>();
	private final static Map<String, MetaControlBean> metaControlBeans = new ConcurrentHashMap<String, MetaControlBean>();

	static {
		
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage("com.varsql.db.ext",
						Thread.currentThread().getContextClassLoader()))
				.setScanners(new Scanner[] { new TypeAnnotationsScanner(), new SubTypesScanner() }));
		Set<Class<?>> types = reflections.getTypesAnnotatedWith(MetaBeanConfig.class);
		StringBuffer sb;
		for (Class type : types) {
			sb = new StringBuffer();
			
			MetaBeanConfig annoInfo = (MetaBeanConfig) type.getAnnotation(MetaBeanConfig.class);
			
			ALL_CONFIG_BEAN.put(annoInfo.dbVenderType().getName(), annoInfo);
			sb.append("beanType : [").append(annoInfo.dbVenderType());
			sb.append("] metaBean : [").append(annoInfo.metaBean()).append("]");
			sb.append("] ddlBean : [").append(annoInfo.ddlBean()).append("]");
			sb.append("] dataTypeBean : [").append(annoInfo.dataTypeBean()).append("]");
			sb.append("] tableReportBean : [").append(annoInfo.tableReportBean()).append("]");
			sb.append("] commandTypeBean : [").append(annoInfo.commandTypeBean()).append("]");
			sb.append("] statementSetterBean : [").append(annoInfo.statementSetterBean()).append("]");
			logger.info("meta bean config : {}" , sb.toString());
		}
		
		// init bean
		for(DBVenderType dbType : DBVenderType.values()) {
			String dbname = dbType.getName();
			
			logger.info("meta bean init : {}" , dbname);
			
			if(ALL_CONFIG_BEAN.containsKey(dbname)) {
				metaControlBeans.put(dbname, new MetaControlBean(ALL_CONFIG_BEAN.get(dbname))) ;
			}else {
				metaControlBeans.put(dbname, new MetaControlBean(ALL_CONFIG_BEAN.get(DBVenderType.OTHER.getName()))) ;
			}
		}
	}

	public static MetaControlBean getDbInstanceFactory(DBVenderType type){
		return getDbInstanceFactory(type.name());
	}

	/**
	 *
	 * @Method Name  : getDbInstanceFactory
	 * @Method 설명 : get met bean
	 * @작성일   : 2019. 11. 26.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param type  db type
	 * @return
	 */
	public static MetaControlBean getDbInstanceFactory(String type){
		return metaControlBeans.get(type.toUpperCase());
	}

}
