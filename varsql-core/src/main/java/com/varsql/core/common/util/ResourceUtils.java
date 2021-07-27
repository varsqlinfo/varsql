package com.varsql.core.common.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.configuration.Configuration;

/**
 * -----------------------------------------------------------------------------
* @fileName		: ResourceUtil.java
* @desc		: resource load util
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 9. 18. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class ResourceUtils {

	private final static String FILE_PREFIX = "file:";
	private final static String CLASS_PREFIX = "classpath:";

	private ResourceUtils() {}

	/**
	 * @method  : getPackageResources
	 * @desc : 하위 패키지 밑에 경로 가져오기.
	 * @author   : ytkim
	 * @date   : 2020. 9. 18.
	 * @param packagePath ex : classpath*:preferences\/*\/*.xml
	 * @return
	 * @throws IOException
	 */
	public static Resource[] getPackageResources(String packagePath) throws IOException {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
		return resolver.getResources(packagePath) ;
	}

	/**
	 * @method  : getClassPathResources
	 * @desc : class path 하위  자원 구하기.
	 * @author   : ytkim
	 * @date   : 2020. 9. 18.
	 * @param resourcePath
	 * @return
	 * @throws IOException
	 */
	public static Resource getResource(String resourcePath) throws IOException {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());

		Resource reval;
		if(resourcePath.startsWith(CLASS_PREFIX) || resourcePath.startsWith(FILE_PREFIX)) {
			reval = resolver.getResource(resourcePath);
		}else {
			if(new File(resourcePath).exists()) {
				reval = resolver.getResource(FILE_PREFIX+resourcePath);
			}else {
				reval = resolver.getResource(resourcePath);
			}
		}

		if(reval.exists()) {
			return reval;
		}else {
			return null;
		}
	}

	/**
	 * @method  : getResourceString
	 * @desc : resource -> string
	 * @author   : ytkim
	 * @date   : 2020. 9. 18.
	 * @param resourcePath
	 * @return
	 * @throws IOException
	 */
	public static String getResourceString(String resourcePath) throws IOException {
		return getResourceString(getResource(resourcePath));
	}

	/**
	 * @method  : getResourceString
	 * @desc : resource -> string
	 * @author   : ytkim
	 * @date   : 2020. 9. 18.
	 * @param resourcePath
	 * @return
	 * @throws IOException
	 */
	public static String getResourceString(Resource resource) throws IOException {
		return getResourceString(resource, VarsqlConstants.CHAR_SET);
	}

	public static String getResourceString(Resource resource, String charset) throws IOException {
		return IOUtils.toString(resource.getInputStream(), charset);
	}
	
	/**
	 * @method  : getInstallPathResource
	 * @desc : resourcePath -> resource
	 * @author   : ytkim
	 * @date   : 2020. 9. 18.
	 * @param resourcePath
	 * @return
	 * @throws IOException
	 */
	public static Resource getInstallPathResource(String resourcePath) throws IOException {
		Resource configResource; 
		
		File file = new File(Configuration.getInstance().getInstallRoot(), resourcePath);
		if(file.exists()) {
			configResource = ResourceUtils.getResource(file.getPath());
		}else {
			configResource = ResourceUtils.getResource(resourcePath);
		}
		
		return configResource; 
	}
}
