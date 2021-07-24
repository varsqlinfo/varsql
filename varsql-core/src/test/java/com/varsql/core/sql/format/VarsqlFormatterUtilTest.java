package com.varsql.core.sql.format;

import org.junit.jupiter.api.Test;

import com.varsql.core.db.DBType;
import com.varsql.core.pattern.convert.SQLCommentRemoveConverter;
import com.varsql.core.sql.format.VarsqlFormatterUtil.FORMAT_TYPE;
import com.varsql.core.test.BaseTest;

class VarsqlFormatterUtilTest extends BaseTest {

	@Test
	void test() {
		String ugly_sql_code = getResourceContent("/query/formatTestQuery.txt");
		String str = new SQLCommentRemoveConverter().convert(ugly_sql_code, DBType.ORACLE);

		String result =VarsqlFormatterUtil.formatResponseResult(str ,DBType.OTHER, FORMAT_TYPE.VARSQL).getItem();

		System.out.println(result);
	}
	
	@Test
	void mybatisTest() {
		String ugly_sql_code = getResourceContent("/query/mybatisFormatTestQuery.txt");
		
		String result =VarsqlFormatterUtil.formatResponseResult(ugly_sql_code ,DBType.MYSQL , FORMAT_TYPE.DRUID).getItem();
		
		
		System.out.println(result);
		
	}

}
