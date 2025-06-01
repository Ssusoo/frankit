package com.me.frankit.test;

import com.me.frankit.test.config.TestProfile;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith({MockitoExtension.class})
@ActiveProfiles(TestProfile.TEST)
public abstract class MockTest {
}
