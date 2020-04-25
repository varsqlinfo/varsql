package com.varsql.core.db.ddl.template;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.varsql.core.db.DBType;
import com.varsql.core.db.ddl.DDLTemplateFactory;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;

class DDLTemplateFactoryTest {

	@Test
	void testRender() {
		Map param = new HashMap();

		param.put("ddlOption", new DDLCreateOption());

		String tempateSource = DDLTemplateFactory.getInstance().ddlRender(DBType.ORACLE.getDbVenderName(), "sequenceScript", param);

		assertNotNull(tempateSource);
	}

}
