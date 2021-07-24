package com.varsql.core.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.vartech.common.crypto.EncryptDecryptException;

class DBPasswordCryptionFactoryTest {

	@Test
	void test() throws EncryptDecryptException {
		String inputStr = "passwordIm";

		String enc = DBPasswordCryptionFactory.getInstance().encrypt(inputStr);
		String dec = DBPasswordCryptionFactory.getInstance().decrypt(enc);

		System.out.println("input : "+ inputStr);
		System.out.println("enc : "+ enc);
		System.out.println("dec : "+ dec);

		assertEquals(dec, inputStr);
	}

}
