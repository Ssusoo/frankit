package com.me.frankit.domains.goods.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.me.frankit.domains.goods.dto.GoodsOptionModifyRequest;
import com.me.frankit.domains.goods.dto.payload.GoodsOptionModifyPayload;
import com.me.frankit.domains.goods.repository.GoodsOptionRepository;
import com.me.frankit.domains.user.domain.embed.UserRole;
import com.me.frankit.domains.user.domain.exception.UserRoleAccessDeniedException;
import com.me.frankit.domains.user.repsoitory.UserRepository;
import com.me.frankit.global.config.security.jwt.JwtClaimsPayload;
import com.me.frankit.global.error.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GoodsOptionModifyService {
	private final GoodsOptionRepository goodsOptionRepository;
	private final UserRepository userRepository;

	/**
	 * 상품 옵션 수정
	 */
	public void modify(Long goodsOptionId, GoodsOptionModifyRequest request) throws JsonProcessingException {
		// 유저 체크
		validationUser();

		var goodsOption = goodsOptionRepository.findGoodsOption(goodsOptionId)
				.orElseThrow(DataNotFoundException::new);

		goodsOption.modify(new GoodsOptionModifyPayload(request));
	}

	/**
	 * 상품 옵션 삭제
	 */
	public void deleteGoods(Long goodsOptionId) {
		// 유저 체크
		validationUser();

		var goodsOption = goodsOptionRepository.findGoodsOption(goodsOptionId)
				.orElseThrow(() -> new DataNotFoundException("상품 옵션 정보를 찾을 수 없습니다."));

		goodsOption.delete();
	}

	/**
	 * 유저 체크
	 */
	private void validationUser() {
		// 유저 조회
		var userEmail = JwtClaimsPayload.getUserEmail();
		var user = userRepository.findUser(userEmail).orElseThrow(DataNotFoundException::new);

		// 유저 권한 체크
		if (!UserRole.USER.equals(user.getRole())) {
			throw new UserRoleAccessDeniedException();
		}
	}
}
