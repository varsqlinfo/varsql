package com.varsql.core.configuration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PreferencesDataFactoryTest {

	@Test
	void test() {
		System.out.println(PreferencesDataFactory.getInstance().getDefaultValue("main.contextmenu.serviceobject"));
		assertNotNull(PreferencesDataFactory.getInstance().getDefaultValue("main.contextmenu.serviceobject"));
	}

}
