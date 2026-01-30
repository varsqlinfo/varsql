package com.varsql.core.db.mybatis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.JdbcDriverLoader;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.connection.ConnectionInfoManager;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.db.datasource.SingleConnectionDataSource;
import com.varsql.core.db.datasource.SingleDriverDataSource;
import com.varsql.core.db.meta.DBVersionInfo;
import com.varsql.core.db.mybatis.handler.type.LONGVARCHARHandler;
import com.varsql.core.db.valueobject.CommentInfo;
import com.varsql.core.db.valueobject.ConstraintInfo;
import com.varsql.core.exception.ConnectionException;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.util.JdbcUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.io.Resource;
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

	private final static String LOG_PREFIX = "com.varsql.core.varsql_query";

	private Map<String, SqlSessionFactory> sqlSessionFactoryMap = new ConcurrentHashMap<String, SqlSessionFactory>();

	private static class MybatisConfigHolder {
		private static final SQLManager instance = new SQLManager();// null
	}

	public static SQLManager getInstance() {
		return MybatisConfigHolder.instance;
	}

	private SQLManager() {}

	public SqlSession getSqlSession(String connid) throws ConnectionFactoryException {
		
		if(ConnectionFactory.getInstance().isShutdown(connid)) {
			throw new ConnectionFactoryException(VarsqlAppCode.EC_FACTORY_CONNECTION_ERROR, "connection is shutdown : [" + connid + "]");
		}

		if (!sqlSessionFactoryMap.containsKey(connid)) {
			setSQLMapper(ConnectionInfoManager.getInstance().getConnectionInfo(connid), this);
		}

		return sqlSessionFactoryMap.get(connid).openSession();
	}

	public synchronized void setSQLMapper(ConnectionInfo connInfo , Object obj){
		try{
			logger.debug("connInfo : {}", connInfo);
			if(!(obj instanceof ConnectionFactory ||  obj instanceof SQLManager)){
				logger.error("SQLManager setSQLMapper access denied object {}", obj );
				throw new VarsqlRuntimeException(VarsqlAppCode.EC_DB_POOL,"SQLManager setSQLMapper access denied object "+obj);
			}

			SqlSessionFactory sqlSessionFactory = sqlSessionFactory(connInfo);

			logger.debug("connection check start: {}", connInfo.getConnid());
			try(Connection connChk = sqlSessionFactory.openSession().getConnection();){
				JdbcUtils.close(connChk);
			}
			logger.debug("connection check end: {}", connInfo.getConnid());

			sqlSessionFactoryMap.put(connInfo.getConnid(), sqlSessionFactory);
		} catch (Exception e) {
			logger.error("connection info :  {}, error message : {} ", VartechReflectionUtils.reflectionToString(connInfo) , e.getMessage());
			//logger.error("SQLManager :{} ", e.getMessage() , e);
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
	private SqlSessionFactory sqlSessionFactory(ConnectionInfo connInfo) throws Exception {
		
		
		logger.debug("sqlSessionFactory start : {}", connInfo.getConnid());
		
		Environment environment = new Environment(connInfo.getConnid(), new JdbcTransactionFactory(), dataSource(connInfo));

		Configuration configuration = getConfiguration(environment);

		DBVersionInfo dbVersionInfo=connInfo.getVersion();
		
		Resource[] resources = null; 
		
		if(dbVersionInfo == null|| dbVersionInfo.isDefultFlag() || dbVersionInfo.getMajor()==-1) {
			resources = ResourceUtils.getResources(String.format("classpath:db/ext/%sMapper.xml",connInfo.getType()));
		}else {
			resources= ResourceUtils.getResources(String.format("classpath:db/ext/%sMapper-%s.xml",connInfo.getType(), dbVersionInfo.getVersion()));
		}

		for (Resource resource : resources) {
			if (resource == null || resource.getInputStream() == null) {
				continue;
			}

			try {
				XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration,	resource.toString(), configuration.getSqlFragments());
				xmlMapperBuilder.parse();
			} catch (Exception e) {
				logger.error("mapper load fail : {}", resource.getFile().getAbsolutePath(), e);
			} finally {
				ErrorContext.instance().reset();
			}
		}

		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		
		logger.debug("sqlSessionFactory end : {}", connInfo.getConnid());
		
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
		Driver dbDriver = null;
		
		logger.info("datasource driver start : {} ", connInfo.getConnid());
		
		if (connInfo.getJdbcDriverInfo() != null) {
			dbDriver = JdbcDriverLoader.getInstance().load(connInfo.getJdbcDriverInfo());
			if (dbDriver != null) {
				logger.info("jdbc driver load success driver class : {},majorVersion:{}, minorVersion: {}", dbDriver, dbDriver.getMajorVersion(), dbDriver.getMinorVersion());
			} else {
				logger.error("jdbc driver load fail : {}", connInfo.getJdbcDriverInfo().getDriverFiles());
							
				throw new ConnectionException("jdbc driver load fail : " + connInfo.getJdbcDriverInfo());
			}
		}
		
		logger.info("datasource isEnableConnectionPool : {} ", connInfo.isEnableConnectionPool());
		
		if(!connInfo.isEnableConnectionPool()) {
	    	if(dbDriver != null) {
	    		return new SingleDriverDataSource(dbDriver, connInfo.getUrl(), connInfo.getUsername(), connInfo.getPassword());
	    	}else {
				return new SingleConnectionDataSource(connInfo.getJdbcDriverInfo().getDriverClass(), connInfo.getUrl(), connInfo.getUsername(), connInfo.getPassword());
	    	}
		}
		
		logger.info("datasource datasource : {} ", connInfo.getConnid());
			
		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setUrl(connInfo.getUrl());
		dataSource.setUsername(connInfo.getUsername());
		dataSource.setPassword(connInfo.getPassword());

		// ===== 풀 기본 =====
		dataSource.setInitialSize(2);
		dataSource.setMaxTotal(10);
		dataSource.setMaxIdle(5);
		dataSource.setMinIdle(1);

		// ===== 커넥션 검증 (옵션 조건 처리) =====
		dataSource.setValidationQuery(connInfo.getValidationQuery());

		if(connInfo.isTestWhileIdle()) {
		    dataSource.setTestWhileIdle(true);            // idle 커넥션 검사
		    dataSource.setTestOnBorrow(true);             // borrow 시 검증
		    dataSource.setTestOnReturn(false);            // 반환 시 검증 불필요
		    dataSource.setValidationQueryTimeout(3);      // 검증 쿼리 타임아웃
		}

		// ===== idle 커넥션 정리 =====
		dataSource.setTimeBetweenEvictionRunsMillis(60_000);
		dataSource.setMinEvictableIdleTimeMillis(300_000);
		dataSource.setNumTestsPerEvictionRun(3);

		// ===== 풀 고갈 시 hang 방지 =====
		dataSource.setMaxWaitMillis(5_000);

		// ===== 성능 옵션 =====
		dataSource.setPoolPreparedStatements(true);

		// ===== Driver 설정 =====
		if (dbDriver != null) {
		    dataSource.setDriver(dbDriver);
		} else {
		    dataSource.setDriverClassName(connInfo.getJdbcDriverInfo().getDriverClass());
		}

		return dataSource;
	}

	/**
	 *
	 * @param environment 
	 * @Method Name  : getConfiguration
	 * @Method 설명 : mybatis config
	 * @작성일   : 2020. 10. 20.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	private Configuration getConfiguration(Environment environment) {
		Configuration configuration = new Configuration(environment);
		configuration.setCallSettersOnNulls(true);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setCacheEnabled(true);
		configuration.setLogPrefix(LOG_PREFIX);
		
		Arrays.asList(new Class[] {
			com.varsql.core.db.valueobject.DatabaseParamInfo.class,
			com.varsql.core.db.valueobject.TableInfo.class,
			com.varsql.core.db.valueobject.ColumnInfo.class,
			com.varsql.core.db.valueobject.TriggerInfo.class,
			com.varsql.core.db.valueobject.SequenceInfo.class,
			com.varsql.core.db.valueobject.ObjectInfo.class,
			com.varsql.core.db.valueobject.ObjectColumnInfo.class,
			ConstraintInfo.class,
			CommentInfo.class
		}).stream().forEach(typeAlias-> {
			configuration.getTypeAliasRegistry().registerAlias(typeAlias);
		});
		
		configuration.getTypeAliasRegistry().registerAlias("dataMap", DataMap.class);
		configuration.getTypeHandlerRegistry().register(LONGVARCHARHandler.class);

		return configuration;
	}

	public synchronized void close(String connid) {

	    SqlSessionFactory factory = sqlSessionFactoryMap.remove(connid);
	    if (factory == null) {
	        return;
	    }

	    logger.warn("FORCE CLOSE sqlSessionFactory start1 connid={}", connid);

	    try {
	        DataSource dataSource =
	            factory.getConfiguration().getEnvironment().getDataSource();
	        
	        
	        logger.warn("FORCE CLOSE sqlSessionFactory start2 connid={}", connid);

	        // 1. DBCP 풀 강제 종료
	        if (dataSource instanceof BasicDataSource) {
	            BasicDataSource bds = (BasicDataSource) dataSource;
	            if (!bds.isClosed()) {
	                bds.close(); // 🔥 모든 커넥션 즉시 종료
	            }
	        }
	        
	        logger.warn("FORCE CLOSE sqlSessionFactory start3 connid={}", connid);

	        // 2. SingleConnection 계열 (명시적 종료)
	        if (dataSource instanceof SingleDriverDataSource) {
	        	logger.debug("SingleDriverDataSource closed");
	        }

	        if (dataSource instanceof SingleConnectionDataSource) {
	            // 실제 커넥션 유지 안 하지만, 확장 대비
	            logger.debug("SingleConnectionDataSource closed");
	        }

	    } catch (Exception e) {
	        logger.error("forceClose error connid={}", connid, e);
	        throw new ConnectionException("mybatis datasource close connid:"+connid+", exception : "+e.getMessage() , e);
	    }

	    logger.warn("FORCE CLOSE sqlSessionFactory end connid={}", connid);
	}
}
