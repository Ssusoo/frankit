package com.me.frankit.domains.goods.application;

import com.me.frankit.domains.goods.dto.GoodsOptionDetailFindResult;
import com.me.frankit.domains.goods.dto.GoodsOptionFindRequest;
import com.me.frankit.domains.goods.dto.query.GoodsOptionFindQuery;
import com.me.frankit.domains.goods.repository.GoodsOptionRepository;
import com.me.frankit.domains.user.domain.User;
import com.me.frankit.domains.user.domain.embed.UserRole;
import com.me.frankit.domains.user.domain.exception.UserRoleAccessDeniedException;
import com.me.frankit.domains.user.repsoitory.UserRepository;
import com.me.frankit.global.config.security.jwt.JwtClaimsPayload;
import com.me.frankit.global.dto.Pagination;
import com.me.frankit.global.error.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsOptionFindService {
	private final GoodsOptionRepository goodsOptionRepository;
	private final UserRepository userRepository;

	/**
	 * 상품 옵션 상세 조회
	 */
	public GoodsOptionDetailFindResult getGoodsDetail(Long goodsOptionId) {
		// 유저 체크
		validationUser();

		// 상품 옵션 조회
		var goodsOption = goodsOptionRepository
				.findGoodsOption(goodsOptionId).orElseThrow(DataNotFoundException::new);

		return new GoodsOptionDetailFindResult(goodsOption);
	}

	/**
	 * 상품 옵션 목록 조회
	 */
	public Pagination<GoodsOptionFindRequest.Result> getGoods(GoodsOptionFindRequest request, int page, int size) {
		// 유저 체크
		var user = validationUser();

		// 페이징 처리
		var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "goodsId"));

		// 상품 옵션 목록 조회
		var query = new GoodsOptionFindQuery(request);
		var goodsOption = goodsOptionRepository.findGoodsOption(user.getUserId(), query, pageable)
				.map(GoodsOptionFindRequest.Result::new);

		return new Pagination<>(goodsOption);
	}

	/**
	 * 유저 체크
	 */
	private User validationUser() {
		// 유저 조회
		var userEmail = JwtClaimsPayload.getUserEmail();
		var user = userRepository.findUser(userEmail).orElseThrow(DataNotFoundException::new);

		// 유저 권한 체크
		if (!UserRole.USER.equals(user.getRole())) {
			throw new UserRoleAccessDeniedException();
		}
		return user;
	}
}
