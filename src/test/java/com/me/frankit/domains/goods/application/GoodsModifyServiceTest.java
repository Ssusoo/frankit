package com.me.frankit.domains.goods.application;

import com.me.frankit.domains.goods.domain.Goods;
import com.me.frankit.domains.goods.dto.GoodsModifyRequest;
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
import static com.me.frankit.global.constant.CommonConstant.Yn.Y;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class GoodsModifyServiceTest extends MockTest {
	@InjectMocks
	private GoodsModifyService goodsModifyService;

	@Mock
	private GoodsRepository goodsRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private com.me.frankit.domains.goods.domain.Goods goods;

	@Test
	@DisplayName("상품 수정 - 성공")
	void modify_success() {
		try (MockedStatic<JwtClaimsPayload> mocked = mockStatic(JwtClaimsPayload.class)) {
			// given
			mocked.when(JwtClaimsPayload::getUserEmail).thenReturn("test@frankit.com");
			var user = getUser();
			given(userRepository.findUser("test@frankit.com")).willReturn(Optional.of(user));
			given(goodsRepository.findGoods(1L)).willReturn(Optional.of(goods));

			var request = new GoodsModifyRequest("수정된 상품", "수정 설명", 5000, 3000, Y);

			// when & then
			assertDoesNotThrow(() -> goodsModifyService.modify(1L, request));
			verify(goods, times(1)).modify(any());
		}
	}

	@Test
	@DisplayName("상품 수정 - 실패: 유저 없음")
	void modify_failure_user_not_found() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("notfound@frankit.com");
			given(userRepository.findUser("notfound@frankit.com")).willReturn(Optional.empty());
			var request = new GoodsModifyRequest("상품", "설명", 1000, 1000, Y);

			assertThrows(DataNotFoundException.class,
					() -> goodsModifyService.modify(1L, request));
		}
	}

	@Test
	@DisplayName("상품 수정 - 실패: 유저 권한 아님")
	void modify_failure_invalid_role() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("noUserRole@test.com");
			var managerUser = getNoUserRole();
			given(userRepository.findUser("noUserRole@test.com")).willReturn(Optional.of(managerUser));
			var request = new GoodsModifyRequest("상품", "설명", 1000, 1000, Y);

			assertThrows(UserRoleAccessDeniedException.class,
					() -> goodsModifyService.modify(1L, request));
		}
	}

	@Test
	@DisplayName("상품 삭제 - 성공")
	void delete_success() {
		try (MockedStatic<JwtClaimsPayload> mocked = mockStatic(JwtClaimsPayload.class)) {
			mocked.when(JwtClaimsPayload::getUserEmail).thenReturn("test@frankit.com");
			var user = getUser();
			given(userRepository.findUser("test@frankit.com")).willReturn(Optional.of(user));

			var goods = Goods.builder()
					.goodsId(1L)
					.goodsName("상품")
					.goodsContents("상품 설명")
					.useYn(Y)
					.deliveryPrice(3000)
					.goodsPrice(5000)
					.build();
			given(goodsRepository.findGoods(1L)).willReturn(Optional.of(goods));

			goodsModifyService.deleteGoods(1L);
			// when & then
			assertDoesNotThrow(() -> goodsModifyService.deleteGoods(1L));
		}
	}

	@Test
	@DisplayName("상품 삭제 - 실패: 상품 없음")
	void delete_failure_goods_not_found() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("test@frankit.com");
			var user = getUser();
			given(userRepository.findUser("test@frankit.com")).willReturn(Optional.of(user));
			given(goodsRepository.findGoods(1L)).willReturn(Optional.empty());

			assertThrows(DataNotFoundException.class,
					() -> goodsModifyService.deleteGoods(1L));
		}
	}
}
