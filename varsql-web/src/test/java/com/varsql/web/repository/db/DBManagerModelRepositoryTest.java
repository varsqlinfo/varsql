package com.varsql.web.repository.db;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.varsql.test.BaseJpaTestCase;
import com.varsql.web.model.entity.db.DBManagerEntity;
import com.vartech.common.utils.StringUtils;

class DBManagerModelRepositoryTest extends BaseJpaTestCase{

	@Autowired
    private DBManagerEntityRepository dbManagerModelRepository;

	List<DBManagerEntity> addManagerList;
	String vconnid = "99999";

	@BeforeEach
	public void initBefore() {
		String[] viewidArr = StringUtils.split("000001",",");


		addManagerList = new ArrayList<>();
		for(String id: viewidArr){
			addManagerList.add(DBManagerEntity.builder().vconnid(vconnid).viewid(id).build());
        }
	}
	@Test
	void testSave() {

		List<DBManagerEntity> result = dbManagerModelRepository.saveAll(addManagerList);

		result.forEach(m->{
			assertEquals(m.getVconnid(), vconnid);
		});
	}

	@Test
	void testRemove() {
		dbManagerModelRepository.deleteAll(addManagerList);
	}
}