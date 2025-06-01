package com.me.frankit.global.error.exception;

import com.me.frankit.global.constant.ApiResponseCode;

public class JwtTokenNotFoundException extends BusinessRuntimeException {
	public JwtTokenNotFoundException() {
		super(ApiResponseCode.JWT_TOKEN_NOT_FOUND);
	}
}
