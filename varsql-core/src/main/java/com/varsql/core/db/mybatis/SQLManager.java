package com.varsql.core.db.mybatis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.JdbcDriverLoader;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.db.mybatis.type.handler.LONGVARCHARHandler;
import com.varsql.core.exception.ConnectionException;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.util.JdbcUtils;
import com.vartech.common.utils.VartechReflectionUtils;

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

	final private static String LOG_PREFIX = "com.varsql.core.varsql_query";

	private Map<String, SqlSessionTemplate> sqlSessionMap = new ConcurrentHashMap<String, SqlSessionTemplate>();
	private Map<String, SqlSessionFactory> sqlSessionFactoryMap = new ConcurrentHashMap<String, SqlSessionFactory>();

	private static class MybatisConfigHolder {
		private static final SQLManager instance = new SQLManager();// null
	}

	public static SQLManager getInstance() {
		return MybatisConfigHolder.instance;
	}

	private SQLManager() {}

	public SqlSessionTemplate sqlSessionTemplate(String connid) throws ConnectionFactoryException {
		if(ConnectionFactory.getInstance().isShutdown(connid)) {
			return null;
		}

		if (!sqlSessionMap.containsKey(connid)) {
			setSQLMapper(ConnectionFactory.getInstance().getConnectionInfo(connid), this);
		}

		return sqlSessionMap.get(connid);
	}

	public SqlSession openSession(String connid) throws ConnectionFactoryException {
		if (!sqlSessionFactoryMap.containsKey(connid)) {
			setSQLMapper(ConnectionFactory.getInstance().getConnectionInfo(connid), this);
		}

		return SqlSessionUtils.getSqlSession(sqlSessionFactoryMap.get(connid));
	}

	public void closeSession(String connid, SqlSession session) throws ConnectionFactoryException {
		SqlSessionUtils.closeSqlSession(session, sqlSessionFactoryMap.get(connid));
	}

	public void setSQLMapper(ConnectionInfo connInfo , Object obj){
		try{
			if(!(obj instanceof ConnectionFactory ||  obj instanceof SQLManager)){
				logger.error("SQLManager setSQLMapper access denied object {}", obj );
				throw new VarsqlRuntimeException(VarsqlAppCode.EC_DB_POOL,"SQLManager setSQLMapper access denied object "+obj);
			}

			SqlSessionFactory sqlSessionFactory = sqlSessionFactory(connInfo).getObject();

			try(Connection connChk = sqlSessionFactory.openSession().getConnection();){
				JdbcUtils.close(connChk);
			}

			sqlSessionFactoryMap.put(connInfo.getConnid() , sqlSessionFactory);
			sqlSessionMap.put(connInfo.getConnid() , new SqlSessionTemplate(sqlSessionFactory));
		} catch (Exception e) {
			logger.error("connection info :  {} ", VartechReflectionUtils.reflectionToString(connInfo));
			logger.error("SQLManager :{} ", e.getMessage() , e);
			throw new ConnectionException("getSqlSession IOException "+e.getMessage(), e);
		}
	}

	/**
	 *
	 * @Method Name  : sqlSessionFactory
	 * @Method 설명 :get sql session factory
	 * @작성일   : 2020. 10. 20.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connInfo
	 * @return
	 */
	private SqlSessionFactoryBean sqlSessionFactory(ConnectionInfo connInfo) throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource(connInfo));
		sqlSessionFactory.setTransactionFactory(new SpringManagedTransactionFactory());

		sqlSessionFactory.setConfiguration(getConfiguration());

		sqlSessionFactory.setTypeAliases(new Class[] {
			com.varsql.core.db.valueobject.ResultTypeMap.class,
			com.varsql.core.db.valueobject.DatabaseParamInfo.class,
			com.varsql.core.db.valueobject.TableInfo.class,
			com.varsql.core.db.valueobject.ColumnInfo.class,
			com.varsql.core.db.valueobject.TriggerInfo.class,
			com.varsql.core.db.valueobject.SequenceInfo.class,
			com.varsql.core.db.valueobject.ObjectInfo.class,
			com.varsql.core.db.valueobject.ObjectColumnInfo.class,
		});

		sqlSessionFactory.setTypeHandlers(new TypeHandler[] {
			new LONGVARCHARHandler()
		});
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		sqlSessionFactory.setMapperLocations(resolver.getResources(String.format("classpath*:db/ext/%sMapper.xml",connInfo.getType())));
		return sqlSessionFactory;
	}

	/**
	 *
	 * @Method Name  : dataSource
	 * @Method 설명 : connection pool 설정
	 * @작성일   : 2020. 10. 20.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connInfo
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private DataSource dataSource(ConnectionInfo connInfo) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setUrl(connInfo.getUrl());
		dataSource.setUsername(connInfo.getUsername());
		dataSource.setPassword(connInfo.getPassword());

		dataSource.setInitialSize(5);
		dataSource.setMaxTotal(10);
		dataSource.setMaxIdle(10);
		dataSource.setMinIdle(0);
		dataSource.setValidationQuery(connInfo.getValidation_query());

		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		dataSource.setTestWhileIdle(true);
		dataSource.setTimeBetweenEvictionRunsMillis(150000);

		dataSource.setNumTestsPerEvictionRun(5);
		dataSource.setMinEvictableIdleTimeMillis(-1);
		dataSource.setPoolPreparedStatements(true);

		Driver dbDriver = JdbcDriverLoader.getInstance().load(connInfo.getJdbcDriverInfo());
	    if (dbDriver != null) {
	      logger.info("jdbc driver load success");
	      dataSource.setDriver(dbDriver);
	    } else {
	      logger.info("jdbc driver load fail : {}", connInfo.getJdbcDriverInfo().getDriverFiles());
	      throw new ConnectionException("jdbc driver load fail : " + connInfo.getJdbcDriverInfo().getDriverFiles());
	    } 

		return dataSource;
	}

	/**
	 *
	 * @Method Name  : getConfiguration
	 * @Method 설명 : mybatis config
	 * @작성일   : 2020. 10. 20.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	private Configuration getConfiguration() {
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setCallSettersOnNulls(true);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setCacheEnabled(true);
		configuration.setLogPrefix(LOG_PREFIX);

		return configuration;
	}

	public synchronized void close(String connid) {
		logger.info("meta sql mapper datasource close : {} " , connid);
		if (sqlSessionFactoryMap.containsKey(connid)) {
			try {
				((BasicDataSource)sqlSessionFactoryMap.get(connid).getConfiguration().getEnvironment().getDataSource()).close();
				sqlSessionFactoryMap.remove(connid);
				sqlSessionMap.remove(connid);
			} catch (SQLException e) {
				throw new ConnectionException("mybatis datasource close exception : "+e.getMessage() , e);
			}
		}

	}

	public synchronized void reset(String connid) {
		logger.info("meta sql mapper datasource reset : {}" , connid);
		close(connid);
		//setSQLMapper(ConnectionFactory.getInstance().getConnectionInfo(connid), this);

	}

}
