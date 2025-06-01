package com.me.frankit.domains.goods.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.me.frankit.domains.goods.domain.GoodsOption;
import com.me.frankit.domains.goods.dto.GoodsOptionCreateRequest;
import com.me.frankit.domains.goods.dto.payload.GoodsOptionCreatePayload;
import com.me.frankit.domains.goods.exception.GoodsOptionCreateException;
import com.me.frankit.domains.goods.repository.GoodsOptionRepository;
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
public class GoodsOptionCreateService {
	private final GoodsRepository goodsRepository;
	private final GoodsOptionRepository goodsOptionRepository;
	private final UserRepository userRepository;

	public void create(GoodsOptionCreateRequest request) throws JsonProcessingException {
		// 유저 조회
		var userEmail = JwtClaimsPayload.getUserEmail();
		var user = userRepository.findUser(userEmail).orElseThrow(DataNotFoundException::new);

		// 유저 권한 체크
		if (!UserRole.USER.equals(user.getRole())) {
			throw new UserRoleAccessDeniedException();
		}

		var goods = goodsRepository.findGoods(request.goodsId())
				.orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

		Long existingCount = goodsOptionRepository.countByGoods(goods.getGoodsId());

		if (existingCount >= 3) {
			throw new GoodsOptionCreateException();
		}

		goodsOptionRepository.save(GoodsOption.create(user.getUserId(), goods, new GoodsOptionCreatePayload(request)));
	}
}
