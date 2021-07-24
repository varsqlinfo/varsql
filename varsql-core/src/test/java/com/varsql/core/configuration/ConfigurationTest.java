package com.varsql.core.configuration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConfigurationTest {

	@Test
	void testConfigurationFile() {
		assertEquals(Configuration.getInstance().getDbType(), "h2");
	}

	@Test
	void testConnObject() {
		assertNotNull(Configuration.getInstance().getVarsqlDB(), "varsql db info");
	}

}
