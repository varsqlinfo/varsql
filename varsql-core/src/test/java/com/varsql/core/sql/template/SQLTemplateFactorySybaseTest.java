package com.varsql.core.sql.template;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import com.varsql.core.db.DBType;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.sql.SQL;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.varsql.core.test.BaseTest;
import com.vartech.common.utils.VartechUtils;

class SQLTemplateFactorySybaseTest extends BaseTest{

	//@Test
	void testIndexRender() {
		Map param =  new HashMap();
		//param.put("ddlOption", new DDLCreateOption());

		DDLCreateOption ddlOption = new DDLCreateOption();

		param.put("ddlOpt", ddlOption);
		param.put("schema", "schemaTest");
		param.put("objectName", "test_table_21085315141");


		String columnListStr = getResourceContent("/db/ddl/index-ddl-data.json");

		List listMap = VartechUtils.jsonStringToObject(columnListStr, List.class);

		param.put("items", listMap);

		String tempateSource = SQLTemplateFactory.getInstance().sqlRender(DBType.SYBASE, SQL.CREATE.getTemplateId(ObjectType.INDEX), param);


		System.out.println("-----index ddl start------");
		System.out.println(tempateSource);
		System.out.println("-----index ddl end------");

		assertNotNull(tempateSource);
	}



	@Test
	void testTemplate() {
		String tempateSource = SQLTemplateFactory.getInstance().getTemplate(DBType.SYBASE, SQL.CREATE.getTemplateId(ObjectType.TABLE));

		System.out.println(tempateSource);

		assertNotNull(tempateSource);
	}
	//@Test
	void testTableRender() {

		String columnListStr = getResourceContent("/db/ddl/table-ddl-data.json");

		Map param = VartechUtils.jsonStringToObject(columnListStr, HashMap.class);

		String tempateSource = SQLTemplateFactory.getInstance().sqlRender(DBType.SYBASE, SQL.CREATE.getTemplateId(ObjectType.TABLE), param);

		System.out.println("-----table ddl start------");
		System.out.println(tempateSource);
		System.out.println("-----table ddl end------");

		assertNotNull(tempateSource);
	}




}
