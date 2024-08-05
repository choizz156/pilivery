package com.team33.moduleapi;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.team33.moduleadmin.repository.UserBatchRepository;
import com.team33.moduleadmin.service.UserBatchService;
import com.team33.modulecore.core.user.domain.Address;
import com.team33.modulecore.core.user.domain.UserRoles;
import com.team33.modulecore.core.user.domain.UserStatus;
import com.team33.modulecore.core.user.domain.entity.User;

@Commit
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@SpringBootTest
public class UserBatchTest {

	@Autowired
	private UserBatchService userBatchService;

	@Autowired
	private UserBatchRepository userBatchRepositoryImpl;

	private List<User> users;

	@BeforeAll
	void setUpEach(){
		var value = new AtomicInteger(0);
		users = FixtureMonkeyFactory.get().giveMeBuilder(User.class)
			.setNull("id")
			.setLazy("email", () -> "test" + value.addAndGet(1) + "@gmail.com")
			.setLazy("displayName", () -> "displayName" + value.addAndGet(1))
			.setLazy("phone", () -> "010-0000-000" + value.addAndGet(1))
			.set("password", "password")
			.set("address", new Address("서울시 부평구 송도동", "101 번지"))
			.setLazy("realName", () -> "홍길동" + value.addAndGet(1))
			.set("roles", UserRoles.USER)
			.set("userStatus", UserStatus.USER_ACTIVE)
			.setNull("subscriptionCartId")
			.setNull("normalCartId")
			.setNull("oauthId")
			.setNull("reviewIds")
			.sampleList(100000);
	}

	@Test
	@DisplayName("Identity 전략 jdbc batchInsert 싱글스레드")
	void test3() throws Exception {
		userBatchRepositoryImpl.saveAll(users);
	}

	@Test
	@DisplayName("Identity 전략 jdbc batchInsert 멀티스레드")
	void test4() throws Exception {
		userBatchService.saveAll(users);
	}
}
