package com.me.frankit.domains.goods.dto.query;

import com.me.frankit.domains.goods.dto.GoodsFindRequest;
import com.me.frankit.global.constant.SearchType;

import java.time.LocalDate;

public record GoodsFindQuery(
		SearchType searchType,
		LocalDate startDt,
		LocalDate endDt
) {
	public GoodsFindQuery(GoodsFindRequest request) {
		this(
				request.searchType(),
				request.startDt(),
				request.endDt()
		);
	}
	public record Result(
			Long goodsId,
			String goodsName,
			String goodsContents,
			Integer priceAmount,
			Integer deliveryPrice,
			String userYn
	) {
	}
}
