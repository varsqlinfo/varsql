package com.varsql.core.sql.mapping;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.varsql.core.sql.mapping.ParameterMapping;
import com.varsql.core.sql.mapping.ParameterMappingUtil;
import com.varsql.core.test.BaseTest;

class ParameterMappingUtilTest extends BaseTest{

	@Test
	void testSQLPARAM() {
		ParameterMappingUtil pmu = new ParameterMappingUtil();
		String cont = getResourceContent("/comment/sql.txt");
		List<ParameterMapping> sqlList = pmu.sqlParameter(cont);
		
		assertTrue(" sql parameter size not equal \n"+sqlList, sqlList.size() == 2);
		
		assertEquals(sqlList.get(0).getProperty(), "schema");
		assertEquals(sqlList.get(1).getProperty(), "item1");
		
	}

}
