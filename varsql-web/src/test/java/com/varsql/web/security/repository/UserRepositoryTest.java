package com.varsql.web.security.repository;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.varsql.test.BaseJpaTestCase;
import com.varsql.web.model.entity.user.UserEntity;
import com.vartech.common.crypto.EncryptDecryptException;

class UserRepositoryTest extends BaseJpaTestCase{

	@Autowired
    private UserRepository userRepository;

	private UserEntity userModel;

	private String id = "testtest1";
	private String email = "testtest1@test.com";

	@BeforeEach
    public void setUp() throws EncryptDecryptException {
       // initMocks(this);

        userModel = UserEntity.builder()
                .uname("ytkim")
                .uemail(email)
                .upw("manager")
                .uid(id)
                .userRole("admin")
                .build();

        userRepository.save(userModel);

        // 이메일
		//Mockito.when(userRepository.findByUemail(email)).thenReturn(userModel);
		// id
		//Mockito.when(userRepository.findByUid(id)).thenReturn(userModel);

    }

	@Test
	void testFindByUemail() {
        // Run the test
        final UserEntity result = userRepository.findByUemail(email);
        
        System.out.println(result.getUpw());

        // Verify the results
        assertEquals(email, result.getUemail());
	}

	@Test
	void testFindByUid() {
		// Run the test
		final UserEntity result = userRepository.findByUid(id);

		// Verify the results
		assertEquals(id, result.getUid());
	}

	@Test
	void testPasswordTest() throws EncryptDecryptException {
		// Run the test
		final UserEntity result = userRepository.findByUid(id);

	}

}
