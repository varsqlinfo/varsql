package com.varsql.web.util;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.configuration.ApplicationContextProvider;
import com.vartech.common.utils.VartechReflectionUtils;

/**
 * -----------------------------------------------------------------------------
* @fileName		: VarsqlBeanUtils.java
* @desc		: varsql bean util 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 27. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class VarsqlBeanUtils {
    public static Object getStringBean(String beanName) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(beanName);
    }
    
    /**
	 * 
	 * @Method Name  : copyEntity
	 * @Method 설명 : entity copy
	 * @작성일   : 2020. 12. 07. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
    public static <T extends Serializable> T copyEntity(T source) {
    	return SerializationUtils.clone(source);
	}
    
    public static void copyNonNullProperties(Object src, Object target, String... checkProperty) {
	    try {
			BeanUtils.copyProperties(src, target, VartechReflectionUtils.getNullPropertyNames(src, checkProperty));
		} catch (Exception e) {
			throw new VarsqlRuntimeException(VarsqlAppCode.COMM_RUNTIME_ERROR, e);
		}
	}
}
