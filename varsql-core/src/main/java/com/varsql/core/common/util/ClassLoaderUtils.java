package com.varsql.core.common.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;

import com.varsql.core.common.beans.FileInfo;
import com.varsql.core.exception.FileNotFoundException;

public final class ClassLoaderUtils {
	private ClassLoaderUtils() {};

	public static Driver getJdbcDriver(String className, String... driverPaths)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<URL> urlList = new ArrayList<>();
		for (String pathStr : driverPaths) {
			Resource resource = ResourceUtils.getResource(pathStr);

			if (resource != null) {
				urlList.add(resource.getURL());
			} else {
				throw new FileNotFoundException("jdbc driver file not found path : " + pathStr);
			}
		}

		return getJdbcDriver(className , urlList.toArray(new URL[0]));
	}

	public static Driver getJdbcDriver(String className, URL... url)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		URLClassLoader ucl = new URLClassLoader(url);
		return (Driver) Class.forName(className, true, ucl).newInstance();
	}

	public static Driver getJdbcDriver(String className, List<FileInfo> jdbcList)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if(jdbcList != null && jdbcList.size() > 0) {
			List<URL> urlList = new ArrayList<>();
			for(FileInfo item: jdbcList) {
				Resource resource = ResourceUtils.getResource(item.getPath());
				if(resource != null) {
					urlList.add(resource.getURL());
				}
			};

			if(urlList.size() > 0) {
				return getJdbcDriver(className , urlList.toArray(new URL[0]));
			}
		}

		return null;
	}
}
