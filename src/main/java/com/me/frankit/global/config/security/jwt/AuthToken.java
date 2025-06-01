package com.me.frankit.global.config.security.jwt;

public interface AuthToken<T> {
	boolean validate();

	T getData();
}
