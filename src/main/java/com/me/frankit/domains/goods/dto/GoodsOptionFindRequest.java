package com.me.frankit.domains.goods.dto;

import com.me.frankit.domains.goods.constant.GoodsOptionType;
import com.me.frankit.domains.goods.dto.query.GoodsOptionFindQuery;
import com.me.frankit.global.constant.SearchType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record GoodsOptionFindRequest(
		SearchType searchType,
		LocalDate startDt,
		LocalDate endDt
) {
	public record Result(
			@Schema(defaultValue = "상품 관리 번호") Long goodsId,
			@Schema(description = "상품 이름") String goodsName,
			@Schema(description = "상품 옵션 번호") Long goodsOptionId,
			@Schema(description = "상품 옵션 이름") String goodsOptionName,
			@Schema(description = "상품 옵션 타입") GoodsOptionType goodsOptionType,
			@Schema(description = "상품 옵션 타입 데이터") String goodsOptionTypeData,
			@Schema(description = "상품 옵션 가격") Integer goodsAdditionalPrice
	) {
		public Result(GoodsOptionFindQuery.Result query) {
			this(
					query.goodsId(),
					query.goodsName(),
					query.goodsOptionId(),
					query.goodsOptionName(),
					query.goodsOptionType(),
					query.goodsOptionTypeData(),
					query.goodsAdditionalPrice()
			);
		}

	}
}
