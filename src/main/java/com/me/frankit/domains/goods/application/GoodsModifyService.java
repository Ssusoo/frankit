package com.me.frankit.domains.goods.application;

import com.me.frankit.domains.goods.dto.payload.GoodsModifyPayload;
import com.me.frankit.domains.goods.dto.GoodsModifyRequest;
import com.me.frankit.domains.goods.repository.GoodsRepository;
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
@RequiredArgsConstructor
@Transactional
public class GoodsModifyService {
	private final GoodsRepository goodsRepository;
	private final UserRepository userRepository;

	/**
	 * 상품 수정
	 */
	public void modify(Long goodsId, GoodsModifyRequest request) {
		// 유저 체크
		validationUser();

		var goods = goodsRepository.findGoods(goodsId)
				.orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

		goods.modify(new GoodsModifyPayload(request));
	}

	/**
	 * 상품 삭제
	 */
	public void deleteGoods(Long goodsId) {
		// 유저 체크
		validationUser();

		var goods = goodsRepository.findGoods(goodsId)
				.orElseThrow(() -> new DataNotFoundException("상품 관리 정보를 찾을 수 없습니다."));

		goods.delete();
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
