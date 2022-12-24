package com.varsql.core.connection.pool;

import java.util.HashMap;
import java.util.Map;

import com.vartech.common.utils.StringUtils;

/**
 * 
 * @FileName  : ConnectionPoolAbstract.java
 * @프로그램 설명 :
 * @Date      : 2018. 2. 8. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public abstract class AbstractConnectionPool implements ConnectionPoolInterface{
	public Map getConnectionOptions(String connectionOptions) {
		
		if(StringUtils.isBlank(connectionOptions)) return null;

		Map options = new HashMap();
		String [] tmpOpt = StringUtils.split(connectionOptions, ";");

		String [] optVal = null;
		String tmpKey = "";
		for (int i = 0; i < tmpOpt.length; i++) {

			tmpKey=tmpOpt[i];

			if("".equals(tmpKey.trim())){
				continue;
			}

			optVal = StringUtils.split(tmpKey, "=");

			options.put(optVal[0], ( optVal.length > 1 ? optVal[1]:"" ) );
		}
		
		return options; 
	}
}
