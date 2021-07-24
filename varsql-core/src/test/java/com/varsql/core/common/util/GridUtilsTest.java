package com.varsql.core.common.util;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class GridUtilsTest {

	@Test
	void testKeys() {
		Set<String> aaa = new HashSet<>();
		boolean duplicateFlag = false; 
		for (int i = 0; i < 3000; i++) {
			String ukey = GridUtils.getAliasKey(i);
			
			if(aaa.contains(ukey)) {
				duplicateFlag = true; 
				break; 
			}else {
			//	System.out.println(i + " :: "+ ukey);
			}
			aaa.add(ukey);
		}
		
		assertFalse(duplicateFlag ," GridUtils key duplicate");
	}

}
