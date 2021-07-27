package com.varsql.core.sql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.varsql.core.db.DBType;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.sql.SQLTemplateFactory;
import com.varsql.core.sql.SQL;

class SQLTemplateFactoryTest {

	@Test
	void testRender() {
		Map param = new HashMap();

		param.put("ddlOption", new DDLCreateOption());

		String tempateSource = SQLTemplateFactory.getInstance().sqlRender(DBType.CUBRID.getDbVenderName(), SQL.CREATE.getTemplateId(ObjectType.SEQUENCE), param);
		
		
		System.out.println("11111111111");
		System.out.println(tempateSource);
		System.out.println("11111111111");

		assertNotNull(tempateSource);
	}

}
