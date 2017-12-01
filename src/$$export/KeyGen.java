package $$export;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

public class KeyGen {
	public static void main(String[] args) {
		Base64 baa = new Base64();
		String uuid =UUID.nameUUIDFromBytes("vartech_varsql".getBytes()).toString(); 
		System.out.println(baa.encodeAsString(uuid.getBytes()));
	}
}
