package com.me.frankit.domains.goods.dto.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.me.frankit.domains.goods.constant.GoodsOptionType;
import com.me.frankit.domains.goods.dto.GoodsOptionCreateRequest;
import com.me.frankit.global.util.ConverterUtil;

public record GoodsOptionCreatePayload(
		String goodsOptionName,
		GoodsOptionType goodsOptionType,
		String goodsOptionTypeData,
		Integer GoodsAdditionalPrice
) {
	public GoodsOptionCreatePayload(GoodsOptionCreateRequest request) throws JsonProcessingException {
		this(
			request.goodsOptionName(),
			request.goodsOptionType(),
			ConverterUtil.convertObjectToJson(request.goodsOptionTypeInfoList()),
			request.GoodsAdditionalPrice()
		);
	}
}
