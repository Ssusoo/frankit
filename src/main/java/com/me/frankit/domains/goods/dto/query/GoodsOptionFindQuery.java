package com.me.frankit.domains.goods.dto.query;

import com.me.frankit.domains.goods.constant.GoodsOptionType;
import com.me.frankit.domains.goods.dto.GoodsOptionFindRequest;
import com.me.frankit.global.constant.SearchType;

import java.time.LocalDate;

public record GoodsOptionFindQuery(
		SearchType searchType,
		LocalDate startDt,
		LocalDate endDt
) {
	public GoodsOptionFindQuery(GoodsOptionFindRequest request) {
		this(
				request.searchType(),
				request.startDt(),
				request.endDt()
		);
	}

	public record Result(
			Long goodsId,
			String goodsName,
			Long goodsOptionId,
			String goodsOptionName,
			GoodsOptionType goodsOptionType,
			String goodsOptionTypeData,
			Integer goodsAdditionalPrice,
			String userYn
	) {
	}
}
