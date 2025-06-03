package com.me.frankit.domains.goods.application;

import com.me.frankit.domains.goods.dto.GoodsCreateRequest;
import com.me.frankit.domains.goods.repository.GoodsRepository;
import com.me.frankit.domains.user.domain.exception.UserRoleAccessDeniedException;
import com.me.frankit.domains.user.repsoitory.UserRepository;
import com.me.frankit.global.config.security.jwt.JwtClaimsPayload;
import com.me.frankit.global.error.exception.DataNotFoundException;
import com.me.frankit.test.MockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.util.Optional;

import static com.me.frankit.domains.goods.setup.GoodsCreateServiceTestSetup.getNoUserRole;
import static com.me.frankit.domains.goods.setup.GoodsCreateServiceTestSetup.getUser;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

class GoodsCreateServiceTest extends MockTest {
	@InjectMocks
	private GoodsCreateService goodsCreateService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private GoodsRepository goodsRepository;

	@Test
	@DisplayName("상품 생성 - 성공")
	void create_success() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			// given
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("test@frankit.com");
			var user = getUser();
			given(userRepository.findUser("test@frankit.com")).willReturn(Optional.of(user));
			given(goodsRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

			var request = new GoodsCreateRequest("상품명", "설명", 1000, 3500);

			// when & then
			assertDoesNotThrow(() -> goodsCreateService.create(request));
		}
	}

	@Test
	@DisplayName("상품 생성 - 실패: 유저를 찾을 수 없음")
	void create_failure_user_not_found() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("notfound@frankit.com");
			given(userRepository.findUser("notfound@frankit.com")).willReturn(Optional.empty());

			var request = new GoodsCreateRequest("상품명", "설명", 1000, 3500);

			assertThrows(DataNotFoundException.class,
					() -> goodsCreateService.create(request));
		}
	}

	@Test
	@DisplayName("상품 생성 - 실패: 유저 권한이 USER가 아님")
	void create_failure_invalid_role() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("noUserRole@test.com");
			var managerUser = getNoUserRole();
			given(userRepository.findUser("noUserRole@test.com")).willReturn(Optional.of(managerUser));

			var request = new GoodsCreateRequest("상품명", "설명", 1000, 2000);

			assertThrows(UserRoleAccessDeniedException.class,
					() -> goodsCreateService.create(request));
		}
	}
}
