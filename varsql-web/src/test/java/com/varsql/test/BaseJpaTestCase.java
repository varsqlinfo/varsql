package com.varsql.test;

import javax.transaction.Transactional;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.varsql.web.VarsqlApplication;

@ContextConfiguration
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(
	classes = VarsqlApplication.class
	,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@EnableSpringDataWebSupport
public abstract class BaseJpaTestCase extends BaseTestCase{
	
}