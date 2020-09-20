package com.varsql.core.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.utils.VartechReflectionUtils;

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

	private final static Map<String, MetaControlBean> metaControlBeans = new ConcurrentHashMap<String, MetaControlBean>();

	static {
		// init bean
		for(DBType dbType : DBType.values()) {
			String dbname = dbType.name();
			try {
				metaControlBeans.put(dbname, new MetaControlBean(dbname)) ;
			}catch(Exception e) {
				logger.error("MetaControlFactory error : {}",e.getMessage() ,e);
			}
		}
	}
	public static MetaControlBean getConnidToDbInstanceFactory(String connid){
		return getDbInstanceFactory(SecurityUtil.userDBInfo(connid).getType());
	}

	public static MetaControlBean getDbInstanceFactory(DBType type){
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
