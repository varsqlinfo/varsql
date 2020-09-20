package com.varsql.core.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.configuration.Configuration;
import com.vartech.common.crypto.EncryptDecryptException;
import com.vartech.common.crypto.impl.AESEncryptDecrypt;
import com.vartech.common.crypto.impl.AbstractCrypto;
import com.vartech.common.crypto.impl.VartechSeed;
import com.vartech.common.utils.VartechReflectionUtils;

/**
 *
 * @FileName  : EncryptionFactory.java
 * @프로그램 설명 : 암호 factory
 * @Date      : 2017. 12. 1.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class DBPasswordCryptionFactory {
	private final Logger logger = LoggerFactory.getLogger(DBPasswordCryptionFactory.class);

	private AbstractCrypto abstractCrypto;

	private static class ConfigurationHolder{
        private static final DBPasswordCryptionFactory instance = new DBPasswordCryptionFactory();
    }

	public static DBPasswordCryptionFactory getInstance() {
		return ConfigurationHolder.instance;
    }

	private DBPasswordCryptionFactory(){

		String secretKey = Configuration.getInstance().getDbPwSecurityKey();
		String cryptoType = Configuration.getInstance().getDbPWCryptoType();
		String customClass = Configuration.getInstance().getDbPWCustomClass();
		try {
			logger.debug("db password crypto type  : {}" , cryptoType);
			if("aes".equals(cryptoType.toLowerCase())) {
				abstractCrypto = new AESEncryptDecrypt(secretKey);
			}else if("seed".equals(cryptoType.toLowerCase())) {
				abstractCrypto = new VartechSeed(secretKey);
			}else{
				logger.debug("db password crypto custom class  : {}" , customClass);
				if("".equals(customClass)) {
					throw new EncryptDecryptException("custom crypto class not found : ["+customClass+"]");
				}else {
					abstractCrypto = (AbstractCrypto)VartechReflectionUtils.getConstructorIfAvailable(VartechReflectionUtils.forName(customClass), String.class).newInstance(secretKey);
				}
			}
		}catch(EncryptDecryptException e){
			logger.error("EncryptionFactory init error :  ",e);
		}catch (Exception e) {
			logger.error("db password crypto type  : {} :  ",cryptoType);
			logger.error("db password crypto custom class  : {}" , customClass);
			logger.error("EncryptionFactory init error :  ",e);
		}
	}

	public String encrypt(String enc) throws EncryptDecryptException{
		return abstractCrypto.encrypt(enc);
	}

	public String decrypt(String enc) throws EncryptDecryptException{
		return abstractCrypto.decrypt(enc);
	}
}
