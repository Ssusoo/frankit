package com.me.frankit.domains.goods.dto.payload;

import com.me.frankit.domains.goods.dto.GoodsCreateRequest;

public record GoodsCreatePayload (
		String goodsName,
		String goodsContents,
		Integer goodsPrice,
		Integer deliveryPrice
) {
	public GoodsCreatePayload(GoodsCreateRequest request) {
		this(
				request.goodsName(),
				request.goodsContents(),
				request.goodsPrice(),
				request.deliveryPrice()
		);
	}
}
