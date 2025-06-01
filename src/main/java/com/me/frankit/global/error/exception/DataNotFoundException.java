package com.me.frankit.global.error.exception;

import com.me.frankit.global.constant.ApiResponseCode;

public class DataNotFoundException extends BusinessRuntimeException {
	public DataNotFoundException() {
		super(ApiResponseCode.DATA_NOT_FOUND);
	}

	public DataNotFoundException(String message) {
		super(ApiResponseCode.DATA_NOT_FOUND, message);
	}
}
