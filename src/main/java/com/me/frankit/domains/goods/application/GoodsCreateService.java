package com.me.frankit.domains.goods.application;

import com.me.frankit.domains.goods.domain.Goods;
import com.me.frankit.domains.goods.dto.payload.GoodsCreatePayload;
import com.me.frankit.domains.goods.dto.GoodsCreateRequest;
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
@Transactional
@RequiredArgsConstructor
public class GoodsCreateService {
	private final GoodsRepository goodsRepository;
	private final UserRepository userRepository;

	public void create(GoodsCreateRequest request) {
		// 유저 조회
		var userEmail = JwtClaimsPayload.getUserEmail();
		var user = userRepository.findUser(userEmail).orElseThrow(DataNotFoundException::new);

		// 유저 권한 체크
		if (!UserRole.USER.equals(user.getRole())) {
			throw new UserRoleAccessDeniedException();
		}

		// 상품 등록
		goodsRepository.save(Goods.create(user, new GoodsCreatePayload(request)));
	}
}
