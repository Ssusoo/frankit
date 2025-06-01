package com.me.frankit.domains.user.domain.exception;

import com.me.frankit.global.constant.ApiResponseCode;
import com.me.frankit.global.error.exception.BusinessRuntimeException;

public class EmailExistsException extends BusinessRuntimeException {
	public EmailExistsException() {
		super(ApiResponseCode.USER_EMAIL_EXISTS);
	}
}
