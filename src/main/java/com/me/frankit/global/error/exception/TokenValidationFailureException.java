package com.me.frankit.global.error.exception;

import com.me.frankit.global.constant.ApiResponseCode;

@SuppressWarnings({"serial", "RedundantSuppression"})
public class TokenValidationFailureException extends BusinessRuntimeException {

	public TokenValidationFailureException() {
		super(ApiResponseCode.JWT_TOKEN_VALIDATION_FAILURE);
	}
}
