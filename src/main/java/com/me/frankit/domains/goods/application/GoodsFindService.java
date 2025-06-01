package com.me.frankit.domains.goods.application;

import com.me.frankit.domains.goods.dto.GoodsDetailFindResult;
import com.me.frankit.domains.goods.dto.GoodsFindRequest;
import com.me.frankit.domains.goods.dto.query.GoodsFindQuery;
import com.me.frankit.domains.goods.repository.GoodsRepository;
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
public class GoodsFindService {
	private final GoodsRepository goodsRepository;
	private final UserRepository userRepository;

	/**
	 * 상품 목록
	 */
	public Pagination<GoodsFindRequest.Result> getGoods(GoodsFindRequest request, int page, int size) {
		// 유저 체크
		var user = validationUser();

		// 페이징 처리
		var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "goodsId"));

		// 상품 관리 목록 조회
		var query = new GoodsFindQuery(request);
		var goods = goodsRepository.findGoods(query, user.getUserId(), pageable)
				.map(GoodsFindRequest.Result::new);

		return new Pagination<>(goods);
	}

	/**
	 * 상품 상세
	 */
	public GoodsDetailFindResult getGoodsDetail(Long goodsId) {
		// 유저 체크
		validationUser();

		var goods = goodsRepository.findGoods(goodsId)
				.orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

		return new GoodsDetailFindResult(goods);
	}

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
