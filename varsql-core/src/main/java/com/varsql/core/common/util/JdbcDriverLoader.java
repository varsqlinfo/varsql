package com.varsql.core.common.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.varsql.core.common.beans.FileInfo;
import com.varsql.core.connection.beans.JDBCDriverInfo;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.exception.JdbcDriverClassException;
import com.vartech.common.utils.StringUtils;

public final class JdbcDriverLoader {
	private final Logger logger = LoggerFactory.getLogger(JdbcDriverLoader.class);
	
	private ConcurrentHashMap<String, Driver> DRIVER_CACHE = new ConcurrentHashMap<>();
	
	private JdbcDriverLoader() {}
	
	private static class FactoryHolder {
		private static final JdbcDriverLoader instance = new JdbcDriverLoader();
	}

	public static JdbcDriverLoader getInstance() {
		return FactoryHolder.instance;
	}
	
	public Driver load(JDBCDriverInfo jdbcDriverInfo) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		return load(jdbcDriverInfo, false);
	}
	
	public synchronized Driver load(JDBCDriverInfo jdbcDriverInfo, boolean reload) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String providerId = jdbcDriverInfo.getProviderId();
		
		if(StringUtils.isBlank(providerId)) {
			throw new JdbcDriverClassException("jdbc provider id is null : ["+ providerId+"]");
		}
		
		if(!reload) {
			if(!DRIVER_CACHE.containsKey(providerId)) {
				reload = true; 
			}
		}
		
		if(reload) {
			List<FileInfo> jdbcList = jdbcDriverInfo.getDriverFiles();
	
			if (jdbcList != null && jdbcList.size() > 0) {
				List<URL> urlList = new ArrayList<>();
				for (FileInfo item : jdbcList) {
					Resource resource = ResourceUtils.getResource(item.getPath());
					if (resource != null)
						urlList.add(resource.getURL());
				}
				if (urlList.size() > 0) {
					DRIVER_CACHE.put(providerId, getJdbcDriver(jdbcDriverInfo));
				}
			}else {
				DRIVER_CACHE.remove(providerId);
			}
		}
		
		return DRIVER_CACHE.getOrDefault(providerId, null);
	}

	private Driver getJdbcDriver(JDBCDriverInfo jdbcDriverInfo)throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<FileInfo> jdbcList = jdbcDriverInfo.getDriverFiles();
		
		if (jdbcList != null && jdbcList.size() > 0) {
			List<URL> urlList = new ArrayList<>();
			for (FileInfo item : jdbcList) {
				Resource resource = ResourceUtils.getResource(item.getPath());
				if (resource != null)
					urlList.add(resource.getURL());
			}
			if (urlList.size() > 0) {
				
				URLClassLoader ucl = URLClassLoader.newInstance(urlList.<URL>toArray(new URL[0]), ClassLoader.getSystemClassLoader().getParent());
				
				Class<?> driverClass = Class.forName(jdbcDriverInfo.getDriverClass(), true, ucl);
				
				try {
					return (Driver) driverClass.getDeclaredConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new JdbcDriverClassException(e);
				}
			}
		}
		
		return null; 
	}

	public static Driver checkDriver(JDBCDriverInfo jdbcDriverInfo)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		return getInstance().getJdbcDriver(jdbcDriverInfo);
	}
	
	public static void allDeregister() {
		getInstance().allDeregisterDriver();
	}
	
	private  void allDeregisterDriver() {
		for (Entry<String, Driver> entry : DRIVER_CACHE.entrySet()) {
			try {
				deregisterDriver(entry.getKey());
			} catch (Exception ex) {
				logger.error("Not deregistering JDBC driver id : {} error message : {}", entry.getKey(), ex.getMessage());
			}
		}
	}

	public void deregisterDriver(String driverId) {
		if(!DRIVER_CACHE.containsKey(driverId)) {
			throw new JdbcDriverClassException("Not deregistering JDBC driver id : "+ driverId);
		}
		
		Driver driver = DRIVER_CACHE.get(driverId);
		try {
			logger.info("DeregisterDriver driver id: {}", driverId);
			DriverManager.deregisterDriver(driver);
		} catch (Exception ex) {
			logger.error("Not deregistering JDBC driver id : {} error message : {}", driverId, ex.getMessage());
		}
	}
}
