package com.me.frankit.domains.goods.exception;

import com.me.frankit.global.constant.ApiResponseCode;
import com.me.frankit.global.error.exception.BusinessRuntimeException;

public class GoodsOptionCreateException extends BusinessRuntimeException {
	public GoodsOptionCreateException() {
		super(ApiResponseCode.GOODS_OPTION_FAILURE);
	}

	public GoodsOptionCreateException(String errorMessage) {
		super(ApiResponseCode.GOODS_OPTION_FAILURE, errorMessage);
	}
}
