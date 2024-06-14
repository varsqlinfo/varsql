package com.varsql.web.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.configuration.ApplicationContextProvider;

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
		return ApplicationContextProvider.getApplicationContext().getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> requiredType) {
		return (T) ApplicationContextProvider.getApplicationContext().getBean(beanName, requiredType);
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
			BeanUtils.copyProperties(src, target, getNullPropertyNames(src, checkProperty));
		} catch (Exception e) {
			throw new VarsqlRuntimeException(VarsqlAppCode.COMM_RUNTIME_ERROR, e);
		}
	}
    
    public static String[] getNullPropertyNames (Object source, String[] checkProperty) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
	    List<String> prop = Arrays.asList(checkProperty);
	    Set<String> emptyNames = new HashSet<String>();
	    for(java.beans.PropertyDescriptor pd : pds) {
	    	String name = pd.getName();

	    	if(prop.contains(name)) {
	    		Object srcValue = src.getPropertyValue(pd.getName());
		        if (srcValue == null || "".equals(srcValue)) {
		        	emptyNames.add(name);
		        }else {
		        	if("".equals(srcValue.toString().trim())) {
		        		PropertyUtils.setProperty(source, name, srcValue.toString().trim());
		        	}
		        }
	    	}
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
}
