package com.me.frankit.domains.goods.dto.payload;

import com.me.frankit.domains.goods.dto.GoodsModifyRequest;

public record GoodsModifyPayload(
		String goodsName,
		String goodsContents,
		Integer goodsPrice,
		Integer deliveryPrice,
		String useYn
) {
	public GoodsModifyPayload(GoodsModifyRequest request) {
		this(
				request.goodsName(),
				request.goodsContents(),
				request.goodsPrice(),
				request.deliveryPrice(),
				request.useYn()
		);
	}
}
