package com.varsql.web.model.id.generator;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

/**
 * -----------------------------------------------------------------------------
* @fileName		: AppUUIDGenerator.java
* @desc		: varsql uuid generator
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 22. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class AppUUIDGenerator implements IdentifierGenerator, Configurable {

	public static final String PREFIX_PARAMETER = "prefix";
    public static final String PREFIX_DEFAULT = "";
    private String prefix;

	@Override
	public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
		prefix = properties.getProperty(PREFIX_PARAMETER);
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		try {
			String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
			return prefix + uuid;
		} catch (RuntimeException he) {
			throw he;
		}
	}
}
