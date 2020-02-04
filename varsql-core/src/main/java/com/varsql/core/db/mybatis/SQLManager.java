package com.varsql.core.db.mybatis;

import java.beans.PropertyDescriptor;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.exception.ConnectionException;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.resultset.handler.ResultSetHandlerImplOTHER;

/**
 * 
 * @FileName  : SQLManager.java
 * @프로그램 설명 : mybatis manager
 * @Date      : 2018. 4. 12. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public final class SQLManager {
	
	private static Logger logger = LoggerFactory.getLogger(SQLManager.class);
	
	private Map<String, SqlSessionFactory> sqlSessionMap = new ConcurrentHashMap<String, SqlSessionFactory>();
	private String resource = "com/varsql/core/db/mybatis/query/mybatis.template";
	
	private String defaultConfigTemplate; 
	private PropertyDescriptor[] propertyDescs = PropertyUtils.getPropertyDescriptors(ConnectionInfo.class);
	private static class MybatisConfigHolder {
		private static final SQLManager instance = new SQLManager();// null
	}

	public static SQLManager getInstance() {
		return MybatisConfigHolder.instance;
	}

	private SQLManager() {
		try{
			defaultConfigTemplate = IOUtils.toString( Thread.currentThread().getContextClassLoader().getResourceAsStream(resource));
			
			logger.info("SQLManager defaultConfigTemplate {} ", defaultConfigTemplate);
		}catch(Exception e){
			logger.error("SQLManager defaultConfigTemplate ", e);
			throw new VarsqlRuntimeException("MybatisSessionFactory IOException", e);
		}
	}
	
	public SqlSession getSqlSession(String sessionType) throws ConnectionFactoryException {
		if (!sqlSessionMap.containsKey(sessionType)) {
			setSQLMapper(ConnectionFactory.getInstance().getConnectionInfo(sessionType), this);
		}
		return sqlSessionMap.get(sessionType).openSession();
	}
	
	public Connection getConnection(String sessionType) throws ConnectionException {
		try {
			return getSqlSession(sessionType).getConnection();
		}catch(Throwable e) {
			throw new ConnectionException("connection error Cause: "+ e.getMessage());
		}
	}
	
	public void setSQLMapper(ConnectionInfo connInfo , Object obj){
		String xmlTemplate ="";
		StringBuilder sb = new StringBuilder();
		try{
			
			if(!(obj instanceof ConnectionFactory ||  obj instanceof SQLManager)){
				logger.error("SQLManager setSQLMapper access denied object {}", obj );
				throw new VarsqlRuntimeException("SQLManager setSQLMapper access denied object "+obj); 
			}
			
			String sessionType = connInfo.getConnid();
			
			PropertyDescriptor propertyDesc = null;
			xmlTemplate =defaultConfigTemplate;
			String propName;
			Object propValObj ; 
			String propVal ; 
			for (int i = 0; i < propertyDescs.length; i++) {
				propertyDesc = propertyDescs[i];
				propName =propertyDesc.getName();
				propValObj = PropertyUtils.getProperty(connInfo, propName);
				propVal = (propValObj==null ?"" : propValObj.toString() );
				
				propVal = propVal.replaceAll("&amp;", "&").replaceAll("&", "&amp;");
				
				xmlTemplate = xmlTemplate.replaceAll("#\\{"+propName+"\\}",  propVal);
			}
			String mapperPath = String.format("com/varsql/db/ext/%s/sql/%sMapper.xml",connInfo.getType(), connInfo.getType()); 
			if(Thread.currentThread().getContextClassLoader().getResourceAsStream(mapperPath) != null){
				xmlTemplate = xmlTemplate.replaceAll("#\\{mapperArea\\}", "<mapper resource=\""+mapperPath+"\"/>" );
			}else{
				xmlTemplate = xmlTemplate.replaceAll("#\\{mapperArea\\}", "");
			}
			
			Reader reader = new StringReader(xmlTemplate);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, sessionType);
			sqlSessionMap.put(connInfo.getConnid() , sqlSessionFactory);
		} catch (Exception e) {
			logger.error("xml template :  {} ", xmlTemplate);
			logger.error("SQLManager :{} ", e.getMessage() , e);
			throw new VarsqlRuntimeException("getSqlSession IOException "+e.getMessage(), e);
		}
	}

	public <T> T selectOne(String sessionType,String statement) {
		return getSqlSession(sessionType).<T>selectOne(statement);
	}

	public <T> T selectOne(String sessionType, String statement, Object parameter) {
		return getSqlSession(sessionType).<T>selectOne(statement, parameter);
	}

	public <K, V> Map<K, V> selectMap(String sessionType,String statement, String mapKey) {
		return getSqlSession(sessionType).<K, V>selectMap(statement, mapKey);
	}

	public <K, V> Map<K, V> selectMap(String sessionType,String statement, Object parameter, String mapKey) {
		return getSqlSession(sessionType).<K, V>selectMap(statement, parameter, mapKey);
	}

	public <K, V> Map<K, V> selectMap(String sessionType,String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return getSqlSession(sessionType).<K, V>selectMap(statement, parameter, mapKey, rowBounds);
	}

	public <T> Cursor<T> selectCursor(String sessionType,String statement) {
		return getSqlSession(sessionType).selectCursor(statement);
	}

	public <T> Cursor<T> selectCursor(String sessionType,String statement, Object parameter) {
		return getSqlSession(sessionType).selectCursor(statement, parameter);
	}

	public <T> Cursor<T> selectCursor(String sessionType,String statement, Object parameter, RowBounds rowBounds) {
		return getSqlSession(sessionType).selectCursor(statement, parameter, rowBounds);
	}

	public <E> List<E> selectList(String sessionType,String statement) {
		return getSqlSession(sessionType).<E>selectList(statement);
	}

	public <E> List<E> selectList(String sessionType,String statement, Object parameter) {
		return getSqlSession(sessionType).<E>selectList(statement, parameter);
	}

	public <E> List<E> selectList(String sessionType,String statement, Object parameter, RowBounds rowBounds) {
		return getSqlSession(sessionType).<E>selectList(statement, parameter, rowBounds);
	}

	public void select(String sessionType,String statement, ResultHandler handler) {
		getSqlSession(sessionType).select(statement, handler);
	}

	public void select(String sessionType,String statement, Object parameter, ResultHandler handler) {
		getSqlSession(sessionType).select(statement, parameter, handler);
	}

	public void select(String sessionType,String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		getSqlSession(sessionType).select(statement, parameter, rowBounds, handler);
	}
}
