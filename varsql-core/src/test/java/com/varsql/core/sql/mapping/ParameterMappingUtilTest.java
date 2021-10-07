package com.varsql.core.sql.mapping;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.varsql.core.db.DBType;
import com.varsql.core.pattern.convert.ConvertResult;
import com.varsql.core.test.BaseTest;

class ParameterMappingUtilTest extends BaseTest{

	@Test
	void testSQLPARAM() {
		ParameterMappingUtil pmu = new ParameterMappingUtil();
		String cont = getResourceContent("/query/sqlParam.txt");

		Map<String,String> data = new HashMap<>();
		data.put("item1", "asdf");
		data.put("page_id", "test");
		ConvertResult convertResult = pmu.sqlParameter(DBType.OTHER, cont,data, "?" );

		List<ParameterMapping> sqlList = (List<ParameterMapping>) convertResult.getParameterInfo();
		System.out.println(sqlList);
		System.out.println(convertResult.getCont());
		assertTrue(" sql parameter size not equal \n"+sqlList, sqlList.size() == 3);

		assertEquals(sqlList.get(0).getProperty(), "schema");
	}
}
