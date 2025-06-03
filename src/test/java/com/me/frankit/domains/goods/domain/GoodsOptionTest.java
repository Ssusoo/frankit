package com.me.frankit.domains.goods.domain;

import com.me.frankit.domains.goods.constant.GoodsOptionType;
import com.me.frankit.domains.goods.dto.payload.GoodsOptionCreatePayload;
import com.me.frankit.domains.goods.dto.payload.GoodsOptionModifyPayload;
import com.me.frankit.test.MockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.me.frankit.global.constant.CommonConstant.Yn.N;
import static com.me.frankit.global.constant.CommonConstant.Yn.Y;
import static org.assertj.core.api.Assertions.assertThat;

class GoodsOptionTest extends MockTest {

	@Test
	@DisplayName("상품 옵션 생성 - 성공")
	void create_goods_option_success() {
		Long userId = 1L;
		Goods goods = Goods.builder()
				.goodsName("테스트 상품")
				.goodsContents("설명")
				.goodsPrice(10000)
				.deliveryPrice(2500)
				.useYn(Y)
				.build();

		GoodsOptionCreatePayload payload = new GoodsOptionCreatePayload(
				"색상",
				GoodsOptionType.SELECT,
				"[\"빨강\", \"파랑\", \"초록\"]",
				2000
		);

		GoodsOption option = GoodsOption.create(userId, goods, payload);

		assertThat(option.getGoodsOptionName()).isEqualTo("색상");
		assertThat(option.getGoodsOptionType()).isEqualTo(GoodsOptionType.SELECT);
		assertThat(option.getGoodsOptionTypeData()).isEqualTo("[\"빨강\", \"파랑\", \"초록\"]");
		assertThat(option.getGoodsAdditionalPrice()).isEqualTo(2000);
		assertThat(option.getUseYn()).isEqualTo(Y);
		assertThat(option.getUserId()).isEqualTo(userId);
		assertThat(option.getGoods()).isEqualTo(goods);
	}

	@Test
	@DisplayName("상품 옵션 수정 - 성공")
	void modify_goods_option_success() {
		GoodsOption option = GoodsOption.builder()
				.goodsOptionName("사이즈")
				.goodsOptionType(GoodsOptionType.INPUT)
				.goodsOptionTypeData("[]")
				.goodsAdditionalPrice(1000)
				.useYn(Y)
				.build();

		GoodsOptionModifyPayload payload = new GoodsOptionModifyPayload(
				"사이즈 선택",
				GoodsOptionType.SELECT,
				"[\"S\", \"M\", \"L\"]",
				3000,
				N
		);

		option.modify(payload);

		assertThat(option.getGoodsOptionName()).isEqualTo("사이즈 선택");
		assertThat(option.getGoodsOptionType()).isEqualTo(GoodsOptionType.SELECT);
		assertThat(option.getGoodsOptionTypeData()).isEqualTo("[\"S\", \"M\", \"L\"]");
		assertThat(option.getGoodsAdditionalPrice()).isEqualTo(3000);
		assertThat(option.getUseYn()).isEqualTo(N);
	}

	@Test
	@DisplayName("상품 옵션 삭제 - 상태값 N으로 변경")
	void delete_goods_option_success() {
		GoodsOption option = GoodsOption.builder()
				.useYn(Y)
				.build();

		option.delete();

		assertThat(option.getUseYn()).isEqualTo(N);
	}
}
