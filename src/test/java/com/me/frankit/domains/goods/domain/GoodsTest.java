package com.me.frankit.domains.goods.domain;

import com.me.frankit.domains.goods.dto.payload.GoodsCreatePayload;
import com.me.frankit.domains.goods.dto.payload.GoodsModifyPayload;
import com.me.frankit.domains.user.domain.User;
import com.me.frankit.test.MockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.me.frankit.global.constant.CommonConstant.Yn.N;
import static com.me.frankit.global.constant.CommonConstant.Yn.Y;
import static org.assertj.core.api.Assertions.assertThat;

class GoodsTest extends MockTest {

	@Test
	@DisplayName("상품 생성 - 성공")
	void create_goods_success() {
		User user = User.builder()
				.userId(1L)
				.userEmail("user@test.com")
				.build();

		GoodsCreatePayload payload = new GoodsCreatePayload(
				"테스트 상품",
				"이것은 상품 설명입니다.",
				10000,
				2500
		);

		Goods goods = Goods.create(user, payload);

		assertThat(goods.getUser().getUserEmail()).isEqualTo("user@test.com");
		assertThat(goods.getGoodsName()).isEqualTo("테스트 상품");
		assertThat(goods.getGoodsContents()).isEqualTo("이것은 상품 설명입니다.");
		assertThat(goods.getGoodsPrice()).isEqualTo(10000);
		assertThat(goods.getDeliveryPrice()).isEqualTo(2500);
		assertThat(goods.getUseYn()).isEqualTo(Y);
	}

	@Test
	@DisplayName("상품 수정 - 성공")
	void modify_goods_success() {
		Goods goods = Goods.builder()
				.goodsName("이전 상품명")
				.goodsContents("이전 설명")
				.goodsPrice(8000)
				.deliveryPrice(2000)
				.useYn(Y)
				.build();

		GoodsModifyPayload payload = new GoodsModifyPayload(
				"수정된 상품명",
				"수정된 설명",
				12000,
				3000,
				N
		);

		goods.modify(payload);

		assertThat(goods.getGoodsName()).isEqualTo("수정된 상품명");
		assertThat(goods.getGoodsContents()).isEqualTo("수정된 설명");
		assertThat(goods.getGoodsPrice()).isEqualTo(12000);
		assertThat(goods.getDeliveryPrice()).isEqualTo(3000);
		assertThat(goods.getUseYn()).isEqualTo(N);
	}

	@Test
	@DisplayName("상품 삭제 - 상태값 변경")
	void delete_goods_success() {
		Goods goods = Goods.builder()
				.useYn(Y)
				.build();

		goods.delete();

		assertThat(goods.getUseYn()).isEqualTo(N);
	}
}
