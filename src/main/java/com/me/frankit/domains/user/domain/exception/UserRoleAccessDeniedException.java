package com.me.frankit.domains.user.domain.exception;

import com.me.frankit.global.constant.ApiResponseCode;
import com.me.frankit.global.error.exception.BusinessRuntimeException;

public class UserRoleAccessDeniedException extends BusinessRuntimeException {
	public UserRoleAccessDeniedException() {
		super(ApiResponseCode.ACCESS_DENIED);
	}
}
