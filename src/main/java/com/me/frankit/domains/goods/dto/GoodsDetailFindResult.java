package com.me.frankit.domains.goods.dto;

import com.me.frankit.domains.goods.domain.Goods;

public record GoodsDetailFindResult(
		Long goodsId,
		String goodsName,
		String goodsContents,
		Integer priceAmount,
		Integer deliveryPrice,
		String userYn
) {
	public GoodsDetailFindResult(Goods goods) {
		this(
				goods.getGoodsId(),
				goods.getGoodsName(),
				goods.getGoodsContents(),
				goods.getGoodsPrice(),
				goods.getDeliveryPrice(),
				goods.getUseYn()
		);
	}
}
