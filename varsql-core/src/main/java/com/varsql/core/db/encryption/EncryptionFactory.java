package com.varsql.core.db.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.configuration.Configuration;
import com.vartech.common.encryption.AESEncryptDecrypt;
import com.vartech.common.encryption.EncryptDecryptException;

/**
 * 
 * @FileName  : EncryptionFactory.java
 * @프로그램 설명 : 암호 factory
 * @Date      : 2017. 12. 1. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class EncryptionFactory {
	private final static Logger logger = LoggerFactory.getLogger(EncryptionFactory.class);
	
	private AESEncryptDecrypt aesEncryptDecrypt;
	
	private static class ConfigurationHolder{
        private static final EncryptionFactory instance = new EncryptionFactory();
    }
	
	public static EncryptionFactory getInstance() {
		return ConfigurationHolder.instance;
    }
	
	private EncryptionFactory(){
		try {
			aesEncryptDecrypt = new AESEncryptDecrypt(Configuration.getInstance().getProperties().getProperty("varsql.secret.key","MTZkNzMwM2QtMDQ4NS0zOTlhLWEyZmMtODAwNTg0NDY0NzZk"));
		}catch(EncryptDecryptException e){
			logger.error("EncryptionFactory init error :  ",e);
		}
	}
	
	public String encrypt(String enc) throws EncryptDecryptException{
		return aesEncryptDecrypt.encrypt(enc);
	}
	
	public String decrypt(String enc) throws EncryptDecryptException{
		return aesEncryptDecrypt.decrypt(enc);
	}
}
