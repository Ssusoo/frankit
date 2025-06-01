package com.me.frankit.domains.goods.dto.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.me.frankit.domains.goods.constant.GoodsOptionType;
import com.me.frankit.domains.goods.dto.GoodsOptionModifyRequest;
import com.me.frankit.global.util.ConverterUtil;

public record GoodsOptionModifyPayload(
		String goodsOptionName,
		GoodsOptionType goodsOptionType,
		String goodsOptionTypeData,
		Integer goodsAdditionalPrice,
		String useYn
) {
	public GoodsOptionModifyPayload(GoodsOptionModifyRequest request) throws JsonProcessingException {
		this(
				request.goodsOptionName(),
				request.goodsOptionType(),
				ConverterUtil.convertObjectToJson(request.goodsOptionTypeInfoList()),
				request.goodsAdditionalPrice(),
				request.useYn()
		);
	}
}
