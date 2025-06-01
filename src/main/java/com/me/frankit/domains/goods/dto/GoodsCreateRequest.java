package com.me.frankit.domains.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GoodsCreateRequest (
		@Schema(description = "상품 이름", defaultValue = "Test 상품") @NotEmpty String goodsName,
		@Schema(description = "상품 설명", defaultValue = "Test 상품 설명입니다.") @NotEmpty String goodsContents,
		@Schema(description = "상품 가격", defaultValue = "20000") @NotNull Integer goodsPrice,
		@Schema(description = "배송비", defaultValue = "3000") @NotNull Integer deliveryPrice
) {
}
