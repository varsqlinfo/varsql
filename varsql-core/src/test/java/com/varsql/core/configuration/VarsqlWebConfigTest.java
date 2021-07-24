package com.varsql.core.configuration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class VarsqlWebConfigTest {

	@Test
	void test() {
		assertNotNull(VarsqlWebConfig.newIntance().getPage500());
	}

}
