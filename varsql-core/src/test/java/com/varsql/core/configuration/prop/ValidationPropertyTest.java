package com.varsql.core.configuration.prop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.varsql.core.db.DBType;

class ValidationPropertyTest {

	@Test
	void test() {
		assertEquals(ValidationProperty.getInstance().validationQuery(DBType.MYSQL.name()), "select 1");
	}

}
