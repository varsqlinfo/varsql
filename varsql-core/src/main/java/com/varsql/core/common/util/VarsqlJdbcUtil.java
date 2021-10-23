package com.varsql.core.common.util;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.stringtemplate.v4.ST;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.connection.beans.JdbcURLFormatParam;
import com.varsql.core.exception.VarsqlRuntimeException;

/**
 *
 * @FileName  : VarsqlJdbcUtil.java
 * @프로그램 설명 :
 * @Date      : 2019. 6. 9.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class VarsqlJdbcUtil {
	private VarsqlJdbcUtil() {}

	public static String getJdbcUrl(String urlFormat, JdbcURLFormatParam opt) {
		ST template = new ST(urlFormat, '{', '}');
		setArguments(template, opt);
		return template.render();
	}

	public static String getSampleJdbcUrl(String urlFormat) {
		ST template = new ST(urlFormat, '{', '}');

		setArguments(template, JdbcURLFormatParam.builder()
					.serverIp("127.0.0.1")
					.port(12312)
					.databaseName("databseName")
					.optStr("")
					.build());

		return template.render();
	}

	private static ST setArguments(ST tempate, JdbcURLFormatParam param) {
		try {
			Map<String, Object> describe = BeanUtils.describe(param);
			for(Map.Entry<String, Object> entry : describe.entrySet()) {
				if("port".equals(entry.getKey())) {
					if(param.getPort() < 1) {
						tempate.add("port", false);
					}else {
						tempate.add(entry.getKey(), entry.getValue());
					}
				}else {
					tempate.add(entry.getKey(), entry.getValue());
				}

			}
		} catch (Exception e) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_DB_CONNECTION, e.getMessage(), e);
		}

		return tempate;
	}
}
