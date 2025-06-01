package com.me.frankit.domains.user.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AccessTokenResult(
		String accessToken,
		String refreshToken,
		LocalDateTime refreshTokenExpireAt) {
}
