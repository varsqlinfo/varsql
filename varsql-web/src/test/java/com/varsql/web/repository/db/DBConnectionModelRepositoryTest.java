package com.varsql.web.repository.db;


import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.varsql.test.BaseJpaTestCase;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.spec.DBConnectionSpec;
import com.varsql.web.util.ConvertUtils;

class DBConnectionModelRepositoryTest extends BaseJpaTestCase{

	@Autowired
    private DBConnectionEntityRepository dbConnectionModelRepository;

	@Test
	void testSave() {
		DBConnectionEntity modelData = dbConnectionModelRepository.save(DBConnectionEntity.builder()
				.vname("testdb")
				.vserverip("123.123.123.1")
				.vport(ConvertUtils.longValueOf("12312"))
				.vdatabasename("varsql")
				.vurl("jdbc:h2:file:E://varsql")
				.vdriver("004")
				.vtype("h2")
				.vid("varsql")
				.vpw("")
				.urlDirectYn("Y")
				.vdbversion(ConvertUtils.longValueOf("1"))
				.vdbschema("PUBLIC")
				.useYn("Y")
				.basetableYn("N")
				.schemaViewYn("N")
				.lazyloadYn("Y")
				.maxActive(ConvertUtils.longValueOf(50))
				.minIdle(ConvertUtils.longValueOf(10))
				.timeout(ConvertUtils.longValueOf(3000))
				.exportcount(ConvertUtils.longValueOf(3000))
				.maxSelectCount(ConvertUtils.longValueOf(10000))
				.vquery("select 1").build());

		DBConnectionEntity modelData2= dbConnectionModelRepository.findOne(DBConnectionSpec.detailInfo(modelData.getVconnid())).get();

		assertNotNull(modelData.getVconnid());
		assertEquals(modelData.getVname() , modelData2.getVname());
		assertEquals(modelData.getVurl() , modelData2.getVurl());

	}
}