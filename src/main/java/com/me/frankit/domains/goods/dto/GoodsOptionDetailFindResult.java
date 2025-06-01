package com.me.frankit.domains.goods.dto;

import com.me.frankit.domains.goods.constant.GoodsOptionType;
import com.me.frankit.domains.goods.domain.GoodsOption;

public record GoodsOptionDetailFindResult(
		Long goodsOptionId,
		String goodsOptionName,
		GoodsOptionType goodsOptionType,
		String goodsOptionTypeData,
		Integer GoodsAdditionalPrice,
		String userYn
) {
	public GoodsOptionDetailFindResult(GoodsOption goodsOption) {
		this(
				goodsOption.getGoodsOptionId(),
				goodsOption.getGoodsOptionName(),
				goodsOption.getGoodsOptionType(),
				goodsOption.getGoodsOptionTypeData(),
				goodsOption.getGoodsAdditionalPrice(),
				goodsOption.getUseYn()
		);
	}
}
