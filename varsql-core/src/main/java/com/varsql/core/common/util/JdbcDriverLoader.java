package com.varsql.core.common.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.Resource;

import com.varsql.core.common.beans.FileInfo;
import com.varsql.core.connection.beans.JDBCDriverInfo;

public final class JdbcDriverLoader {
	
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
		
		if(!reload) {
			if(!DRIVER_CACHE.containsKey(jdbcDriverInfo.getDriverId())) {
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
					DRIVER_CACHE.put(jdbcDriverInfo.getDriverId(), getJdbcDriver(jdbcDriverInfo));
				}
			}else {
				DRIVER_CACHE.remove(jdbcDriverInfo.getDriverId());
			}
		}
		
		return DRIVER_CACHE.getOrDefault(jdbcDriverInfo.getDriverId(), null);
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
				URLClassLoader ucl = new URLClassLoader(urlList.<URL>toArray(new URL[0]));
				return (Driver) Class.forName(jdbcDriverInfo.getDriverClass(), true, ucl).newInstance(); 
			}
		}
		
		return null; 
	}

	public static Driver checkDriver(JDBCDriverInfo jdbcDriverInfo)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		return getInstance().getJdbcDriver(jdbcDriverInfo);
	}
	
}
