package com.varsql.web.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.varsql.core.common.util.StringUtil;
import com.varsql.test.BaseJpaTestCase;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBManagerEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.UserMgmtRepository;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.spec.DBConnectionSpec;
import com.varsql.web.util.ValidateUtils;
import com.vartech.common.app.beans.ResponseResult;

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