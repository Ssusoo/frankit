package com.me.frankit.domains.goods.dto;

import com.me.frankit.domains.goods.dto.query.GoodsFindQuery;
import com.me.frankit.global.constant.SearchType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record GoodsFindRequest(
		SearchType searchType,
		LocalDate startDt,
		LocalDate endDt
) {
	public record Result(
			@Schema(defaultValue = "상품 번호") Long goodsId,
			@Schema(description = "상품 이름") String goodsName,
			@Schema(description = "상품 설명") String goodsContents,
			@Schema(description = "상품 가격") Integer priceAmount,
			@Schema(description = "배송비") Integer deliveryPrice,
			@Schema(description = "사용 여부") String userYn
	) {
		public Result(GoodsFindQuery.Result goods) {
			this(
					goods.goodsId(),
					goods.goodsName(),
					goods.goodsContents(),
					goods.priceAmount(),
					goods.deliveryPrice(),
					goods.userYn()
			);
		}
	}
}
