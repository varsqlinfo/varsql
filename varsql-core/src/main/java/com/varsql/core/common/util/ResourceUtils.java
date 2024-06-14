package com.varsql.core.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.configuration.Configuration;
import com.vartech.common.io.Resource;
import com.vartech.common.io.match.PathMatchingResource;

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

	private ResourceUtils() {}

	/**
	 * @method  : getPackageResources
	 * @desc : 하위 패키지 밑에 경로 가져오기.
	 * @author   : ytkim
	 * @date   : 2020. 9. 18.
	 * @param packagePath ex : classpath:preferences\/*\/*.xml
	 * @return
	 * @throws IOException
	 */
	public static Resource[] getResources(String resourcePath) throws IOException {
		return new PathMatchingResource(ResourceUtils.class.getClassLoader()).getResources(resourcePath) ;
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
	public static Resource getResource(String resourcePath) throws IOException  {
		return new PathMatchingResource(ResourceUtils.class.getClassLoader()).getResource(resourcePath);
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
		try(InputStream is=resource.getInputStream()){
			return IOUtils.toString(is, charset);
		}
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
	
	/**
	 * 설치 경로의 resource path 
	 * 
	 * @param resourcePath resource path
	 * @return File install path +  resource path;
	 */
	public static File getInstallPathFile(String resourcePath) {
		return new File(Configuration.getInstance().getInstallRoot(), resourcePath);
	}
}
