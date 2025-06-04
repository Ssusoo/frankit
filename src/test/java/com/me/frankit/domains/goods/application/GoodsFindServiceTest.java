package com.me.frankit.domains.goods.application;

import com.me.frankit.domains.goods.domain.Goods;
import com.me.frankit.domains.goods.dto.GoodsDetailFindResult;
import com.me.frankit.domains.goods.dto.GoodsFindRequest;
import com.me.frankit.domains.goods.dto.query.GoodsFindQuery;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.me.frankit.domains.goods.setup.GoodsCreateServiceTestSetup.getNoUserRole;
import static com.me.frankit.domains.goods.setup.GoodsCreateServiceTestSetup.getUser;
import static com.me.frankit.global.constant.CommonConstant.Yn.Y;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

class GoodsFindServiceTest extends MockTest {
	@InjectMocks
	private GoodsFindService goodsFindService;

	@Mock
	private GoodsRepository goodsRepository;

	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("상품 목록 조회 - 성공")
	void getGoods_success() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("test@frankit.com");
			var user = getUser();
			given(userRepository.findUser("test@frankit.com")).willReturn(Optional.of(user));

			var request = new GoodsFindRequest(null, null, null);
			var pageable = PageRequest.of(0, 10);

			given(goodsRepository.findGoods(any(GoodsFindQuery.class), any(), any()))
					.willReturn(new PageImpl<>(List.of()));

			assertDoesNotThrow(() -> goodsFindService.getGoods(request, 0, 10));
		}
	}

	@Test
	@DisplayName("상품 상세 조회 - 실패: 유저 없음")
	void getGoodsDetail_failure_user_not_found() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("notfound@frankit.com");
			given(userRepository.findUser("notfound@frankit.com")).willReturn(Optional.empty());

			assertThrows(DataNotFoundException.class,
					() -> goodsFindService.getGoodsDetail(1L));
		}
	}

	@Test
	@DisplayName("상품 상세 조회 - 실패: 권한 없음")
	void getGoodsDetail_failure_invalid_role() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("noUserRole@test.com");
			var managerUser = getNoUserRole();
			given(userRepository.findUser("noUserRole@test.com")).willReturn(Optional.of(managerUser));

			assertThrows(UserRoleAccessDeniedException.class,
					() -> goodsFindService.getGoodsDetail(1L));
		}
	}

	@Test
	@DisplayName("상품 상세 조회 - 실패: 상품 없음")
	void getGoodsDetail_failure_goods_not_found() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("test@frankit.com");
			var user = getUser();
			given(userRepository.findUser("test@frankit.com")).willReturn(Optional.of(user));

			given(goodsRepository.findGoods(1L)).willReturn(Optional.empty());

			assertThrows(DataNotFoundException.class,
					() -> goodsFindService.getGoodsDetail(1L));
		}
	}

	@Test
	@DisplayName("상품 상세 조회 - 성공")
	void getGoodsDetail_success() {
		try (MockedStatic<JwtClaimsPayload> mockedStatic = mockStatic(JwtClaimsPayload.class)) {
			mockedStatic.when(JwtClaimsPayload::getUserEmail).thenReturn("test@frankit.com");
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

			var result = goodsFindService.getGoodsDetail(1L);
			assertNotNull(result);
			assertInstanceOf(GoodsDetailFindResult.class, result);
		}
	}
}
