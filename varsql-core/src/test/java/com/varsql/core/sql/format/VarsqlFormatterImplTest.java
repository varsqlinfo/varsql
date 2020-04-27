package com.varsql.core.sql.format;

import org.junit.jupiter.api.Test;

import com.varsql.core.test.BaseTest;

class VarsqlFormatterImplTest extends BaseTest {

	@Test
	void test() {
		String ugly_sql_code = getResourceContent("/query/formatTestQuery.txt");

		VarsqlFormatterImpl aaa = new VarsqlFormatterImpl();

		System.out.println(aaa.execute(ugly_sql_code));
	}

}
