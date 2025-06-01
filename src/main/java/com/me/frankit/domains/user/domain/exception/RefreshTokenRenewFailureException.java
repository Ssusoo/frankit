package com.me.frankit.domains.user.domain.exception;

import com.me.frankit.global.constant.ApiResponseCode;
import com.me.frankit.global.error.exception.BusinessRuntimeException;

public class RefreshTokenRenewFailureException extends BusinessRuntimeException {
	public RefreshTokenRenewFailureException() {
		super(ApiResponseCode.REFRESH_TOKEN_RENEW_FAILURE);
	}
}
