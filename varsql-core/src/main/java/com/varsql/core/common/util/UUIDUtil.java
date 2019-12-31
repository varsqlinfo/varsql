package com.varsql.core.common.util;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.varsql.core.exception.VarsqlRuntimeException;
import com.vartech.common.constants.ResultConst;

/**
 * 
 * @FileName  : UUIDUtil.java
 * @프로그램 설명 : uuid 생성 클래스
 * @Date      : 2019. 3. 20. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class UUIDUtil {
	
	/**
	 * 
	 * @Method Name  : generateUUID
	 * @Method 설명 : random gen
	 * @작성일   : 2019. 3. 20. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	public static String generateUUID(){
		return UUID.randomUUID().toString().replaceAll("-", ""); 
	}
	
	/**
	 * 
	 * @Method Name  : nameUUIDFromBytes
	 * @Method 설명 : 입력 string으로 gen
	 * @작성일   : 2019. 3. 20. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param source
	 * @return
	 */
	public static String nameUUIDFromBytes(String source){
		
		try {
			byte[] bytes = source.getBytes("UTF-8");
			return UUID.nameUUIDFromBytes(bytes).toString().replaceAll("-", "");	
		}catch(UnsupportedEncodingException e) {
			throw new VarsqlRuntimeException(ResultConst.CODE.ERROR.toInt() ,"error.msg"," UUIDUtil error : "+ e.getMessage(), e); 
		}
	}
}
