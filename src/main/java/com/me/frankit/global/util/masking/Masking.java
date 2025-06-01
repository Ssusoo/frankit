package com.me.frankit.global.util.masking;

public interface Masking<T> {
	default T apply(T originValue) {
		return originValue;
	}

	@SuppressWarnings("unused")
	default T apply(T originValue, MaskingStrategy strategy) {
		return originValue;
	}
}