package com.me.frankit.domains.goods.dto;

import com.me.frankit.domains.goods.constant.GoodsOptionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GoodsOptionCreateRequest(
		@Schema(description = "상품 아이디", defaultValue = "1") @NotNull Long goodsId,
		@Schema(description = "상품 옵션 이름", defaultValue = "Test 상품") @NotEmpty String goodsOptionName,
		@Schema(description = "상품 옵션 타입", defaultValue = "SELECT") @NotNull(message = "상품 옵션 타입은 필수입니다.") GoodsOptionType goodsOptionType,
		@NotEmpty @Valid List<GoodsOptionTypeInfo> goodsOptionTypeInfoList,
		@Schema(description = "상품 옵션 금액", defaultValue = "3000") @NotNull Integer GoodsAdditionalPrice
) {
	public record GoodsOptionTypeInfo(
			@Schema(description = "상품 옵션 값", defaultValue = "{검정색, 노란색, 파란색}") @NotEmpty String goodsOptionTypeData
	) {}
}
