package com.me.frankit.test;

import com.me.frankit.domains.user.repsoitory.UserRepository;
import com.me.frankit.test.config.JpaTestConfig;
import com.me.frankit.test.config.TestConfig;
import com.me.frankit.test.config.TestProfile;
import com.me.frankit.test.setup.DatabaseCleaner;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(TestProfile.TEST)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class, JpaTestConfig.class, UserRepository.class})
@Getter
public abstract class RepositoryTest {
	@Autowired
	private DatabaseCleaner databaseCleaner;

	@Autowired
	private EntityManager entityManager;
}
