package com.varsql.core.common.util;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.exception.VarsqlRuntimeException;

/**
 *
 * @FileName  : UUIDUtil.java
 * @프로그램 설명 : uuid 생성 클래스
 * @Date      : 2019. 3. 20.
 * @작성자      : ytkim
 * @변경이력 :
 */
public final class UUIDUtil {

	final static boolean USE_CONN_UID = Configuration.getInstance().useConnUID();

	private UUIDUtil() {}

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
			byte[] bytes = source.getBytes(VarsqlConstants.CHAR_SET);
			return UUID.nameUUIDFromBytes(bytes).toString().replaceAll("-", "");
		}catch(UnsupportedEncodingException e) {
			throw new VarsqlRuntimeException(VarsqlAppCode.ERROR, e, " UUIDUtil error : "+ e.getMessage());
		}
	}

	/**
	 *
	 * @Method Name  : vconnidUUID
	 * @Method 설명 : gen connection uuid
	 * @작성일   : 2019. 3. 20.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param source
	 * @return
	 */
	public static String vconnidUUID(String viewid, String vconnid){
		if(!USE_CONN_UID) {
			return vconnid;
		}
		try {
			return UUID.nameUUIDFromBytes((viewid+vconnid).getBytes(VarsqlConstants.CHAR_SET)).toString().replaceAll("-", "");
		}catch(UnsupportedEncodingException e) {
			throw new VarsqlRuntimeException(VarsqlAppCode.ERROR ,e, " UUIDUtil vconnidUUID error : "+ e.getMessage());
		}
	}
}
