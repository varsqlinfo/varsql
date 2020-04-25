package com.varsql.web.repository.db;


import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.varsql.test.BaseJpaTestCase;
import com.varsql.web.model.entity.db.DBTypeEntity;
import com.varsql.web.repository.db.DBTypeEntityRepository;

class DBTypeModelRepositoryTest extends BaseJpaTestCase{
	@Autowired
    private DBTypeEntityRepository dbTypeModelRepository;
	
	@Test
	void testFindAll() {
		List<DBTypeEntity> result = dbTypeModelRepository.findAll();
		
		System.out.println("result.size() : "+ result.size());
		
		assertFalse(result.size() < 1,  "db type empty");
			
	}
}