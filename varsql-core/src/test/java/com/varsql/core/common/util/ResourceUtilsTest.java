package com.varsql.core.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

class ResourceUtilsTest {

	@Test
	void test() {
		try {
			Resource resource =ResourceUtils.getResource("classpath:config/varsqlConnectionConfig.xml");

			assertNotNull(resource);
			System.out.println("uri : "+resource.getURI());
			System.out.println("patth : "+resource.getURI().toString());

			resource = ResourceUtils.getResource(resource.getURI().getPath());
			assertNotNull(resource);

			System.out.println(resource.getURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
