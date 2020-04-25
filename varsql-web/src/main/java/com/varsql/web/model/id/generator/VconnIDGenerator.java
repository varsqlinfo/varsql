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
* @fileName		: VconnIDGenerator.java
* @desc		: vconnid  Generator
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 17. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class VconnIDGenerator implements IdentifierGenerator, Configurable {

	public final static String NAME = VconnIDGenerator.class.getName();

	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {

	}

	private static final String QUERY = "select max(VCONNID)as maxval from VTCONNECTION";

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		try {
			String vconnid= (String) session.createNativeQuery(QUERY).getSingleResult();

			try{
				vconnid=String.format("%05d", Integer.parseInt(vconnid)+1);
			}catch(Exception e){
				vconnid=String.format("%05d", 1);
			}

			return vconnid;
		} catch (RuntimeException he) {
			he.printStackTrace();
			throw he;
		}
	}
}
