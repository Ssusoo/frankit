package com.me.frankit.domains.goods.dto;

import com.me.frankit.domains.goods.constant.GoodsOptionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GoodsOptionModifyRequest(
		@Schema(description = "옵션 이름", defaultValue = "옵션 이름 수정 Test") @NotEmpty String goodsOptionName,
		@Schema(description = "옵션 타입", defaultValue = "INPUT") @NotNull GoodsOptionType goodsOptionType,
		@NotEmpty @Valid List<GoodsOptionCreateRequest.GoodsOptionTypeInfo> goodsOptionTypeInfoList,
		@Schema(description = "옵션 추가 금액", defaultValue = "5000") @NotNull Integer goodsAdditionalPrice,
		@Schema(description = "사용 여부", defaultValue = "Y") @NotEmpty String useYn
) {
	public record GoodsOptionTypeInfo(
			@Schema(description = "상품 옵션 값", defaultValue = "{검정색, 노란색, 파란색}") @NotEmpty String goodsOptionTypeData
	) {}
}
