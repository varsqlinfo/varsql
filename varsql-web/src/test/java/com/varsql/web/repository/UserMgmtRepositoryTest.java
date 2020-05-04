package com.varsql.web.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.varsql.core.common.util.StringUtil;
import com.varsql.test.BaseJpaTestCase;
import com.varsql.web.model.entity.db.DBManagerEntity;
import com.varsql.web.repository.user.UserMgmtRepository;

class UserMgmtRepositoryTest extends BaseJpaTestCase{

	@Autowired
    private UserMgmtRepository userMgmtRepositoryTest;

	List<DBManagerEntity> addManagerList;

	@BeforeEach
	public void initBefore() {
		String[] viewidArr = StringUtil.split("000001",",");
		String vconnid = "99999";

		addManagerList = new ArrayList<>();
		for(String id: viewidArr){
			addManagerList.add(DBManagerEntity.builder().vconnid(vconnid).viewid(id).build());
        }
	}
	@Test
	void testSave() {





	}

	@Test
	void testRemove() {

	}
}