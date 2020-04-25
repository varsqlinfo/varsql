package com.varsql.web.security.repository;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.varsql.test.BaseJpaTestCase;
import com.varsql.web.model.entity.user.UserLogHistEntity;
import com.varsql.web.security.repository.UserLogHistRepository;
import com.vartech.common.encryption.EncryptDecryptException;
import com.vartech.common.utils.VartechUtils;

class UserLogHistRepositoryTest extends BaseJpaTestCase{

	@Autowired
    private UserLogHistRepository userLogHistRepository;

	@BeforeEach
    public void setUp() throws EncryptDecryptException {

    }

	@Test
	void testSave() {

		UserLogHistEntity result = userLogHistRepository.save(UserLogHistEntity.builder()
				.viewid(VartechUtils.generateUUID())
				.histType("login")
				.usrIp("123.256.123.153")
				.browser("firefox")
				.deviceType("mobile")
				.platform("win").build());


		assertNotNull(result);

	}

}
