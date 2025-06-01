package com.me.frankit.domains.user.domain.exception;

import com.me.frankit.global.constant.ApiResponseCode;
import com.me.frankit.global.error.exception.BusinessRuntimeException;

public class MbtiAnswerExistsException extends BusinessRuntimeException {
	public MbtiAnswerExistsException() {
		super(ApiResponseCode.MBTI_ANSWER_EXISTS);
	}
}
