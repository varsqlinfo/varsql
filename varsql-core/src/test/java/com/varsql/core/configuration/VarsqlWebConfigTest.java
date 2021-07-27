package com.varsql.core.configuration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.vartech.common.utils.VartechReflectionUtils;

class VarsqlWebConfigTest {

	@Test
	void test() {
		
		System.out.println(VartechReflectionUtils.reflectionToString(VarsqlWebConfig.getInstance().getSsoConfig()));
		System.out.println(VartechReflectionUtils.reflectionToString(VarsqlWebConfig.getInstance().getPageConfig()));
		assertNotNull(VarsqlWebConfig.getInstance().getSsoConfig());
	}

}
