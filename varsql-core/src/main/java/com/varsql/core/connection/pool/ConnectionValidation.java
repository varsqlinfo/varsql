package com.varsql.core.connection.pool;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.exception.ConfigurationException;

/**
 *
 * @FileName : ValicationSql.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 21.
 * @프로그램설명:
 * @변경이력	:
 */
public class ConnectionValidation {
	private final Logger log = LoggerFactory.getLogger(ConnectionValidation.class);

	private static Properties props = new Properties();

	public static final String validation_key = "com.varsql.validation.config";
	private final String prop_name = "validation";
	private final String full_prop_name = prop_name+".properties";


	private ConnectionValidation(){
		initialize();
	}

	protected void initialize() {
		try{
			String propPath = getSystemProperty(validation_key);

			log.info("valication system property : {}",propPath);

			if(null==propPath){
				URL pathUrl =ConnectionValidation.class.getClassLoader().getResource(full_prop_name);

				log.info("valication classpath property : {}", pathUrl);

				if(pathUrl==null){
					ResourceBundle rb = ResourceBundle.getBundle(prop_name);

				    Iterator<String> iter = rb.keySet().iterator();
				    String tmpKey = "";
				    while (iter.hasNext()) {
				      tmpKey = iter.next();
				      props.put(tmpKey, rb.getString(tmpKey));
				    }
				    return ;
				}
				propPath = java.net.URLDecoder.decode(pathUrl.getPath(),VarsqlConstants.CHAR_SET);
			}

			File propFile = new File(propPath);

			if ( ! propFile.canRead() ){

				log.info("Can't open jdf configuration file path: {}", propFile);

				throw new ConfigurationException( this.getClass().getName() + " - Can't open jdf configuration file path: " + propFile);
			}

			FileInputStream jdf_fin = new FileInputStream(propFile);
			try{
				props.load(jdf_fin);
				jdf_fin.close();
			}finally{
				if(jdf_fin != null) jdf_fin.close();
			}
		}catch(Exception e){
			log.error(this.getClass().getName(), e);
			throw new RuntimeException(e);
		}
	}

	public Properties getProperties() {
		return props;
	}

	public String getVaidationQuery(String dbtype){
		String v =props.getProperty(dbtype);
		if("".equals(v) || null == v){
			log.info(" validation query not found : {}"+dbtype);
			throw new RuntimeException(" validation query not found : "+dbtype);
		}
		return v;
	}

	private String getSystemProperty(String key){
		return System.getProperty(key);
	}

	private static class ValidationHolder{
        private static final ConnectionValidation instance = new ConnectionValidation();
    }

	public static ConnectionValidation getInstance() {
		return ValidationHolder.instance;
    }
}
