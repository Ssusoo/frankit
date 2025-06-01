package com.me.frankit.domains.goods.repository;

import com.me.frankit.base.repository.BaseRepository;
import com.me.frankit.domains.goods.domain.Goods;
import com.me.frankit.domains.goods.domain.QGoods;
import com.me.frankit.domains.goods.dto.query.GoodsFindQuery;
import com.me.frankit.domains.user.domain.QUser;
import com.me.frankit.global.constant.SearchType;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class GoodsRepository extends BaseRepository<Goods, Long> {
	private static final QGoods goods = QGoods.goods;
	private static final QUser user = QUser.user;

	public Optional<Goods> findGoods(Long goodsId) {
		return Optional.ofNullable(selectFrom(goods)
				.where(goods.goodsId.eq(goodsId))
				.fetchFirst());
	}

	public Page<GoodsFindQuery.Result> findGoods(GoodsFindQuery request, Long userId, Pageable pageable) {
		var contentQuery = select(
				GoodsFindQuery.Result.class,
				goods.goodsId,
				goods.goodsName,
				goods.goodsContents,
				goods.goodsPrice,
				goods.deliveryPrice,
				goods.useYn
		)
				.from(goods)
				.innerJoin(user).on(goods.user.userId.eq(user.userId))
				.where(getConditionBySearchType(request.searchType(), request.startDt(), request.endDt())
						.and(goods.user.userId.eq(userId)));

		var countQuery = select(goods.count())
				.from(goods)
				.where(getConditionBySearchType(request.searchType(), request.startDt(), request.endDt())
						.and(goods.user.userId.eq(userId)));

		return applyPagination(contentQuery, countQuery, pageable);
	}


	/**
	 * 기간 조회
	 */
	private BooleanBuilder getConditionBySearchType(SearchType searchType, LocalDate startDt, LocalDate endDt) {
		var condition = new BooleanBuilder();
		if (searchType != null) {
			switch (searchType) {
				case REGISTER_DATE:
					condition.and(betweenRegisterAt(startDt, endDt));
					break;
				case UPDATE_DATE:
					condition.and(betweenUpdateAt(startDt, endDt));
					break;
				default:
					break;
			}
		}
		return condition;
	}

	/**
	 * 등록 일시 기준 조회
	 *
	 * @param startDt : 시작일
	 * @param endDt   : 종료일
	 */
	private BooleanBuilder betweenRegisterAt(LocalDate startDt, LocalDate endDt) {
		var booleanBuilder = new BooleanBuilder();
		if (startDt == null && endDt == null) {
			return booleanBuilder;
		}
		if (startDt != null) {
			booleanBuilder.and(goods.registerAt.goe(
					startDt.atTime(0, 0)));
		}
		if (endDt != null) {
			booleanBuilder.and(goods.registerAt.lt(
					endDt.atTime(0, 0).plusDays(1)));
		}
		return booleanBuilder;
	}

	/**
	 * 수정 일시 기준 조회
	 *
	 * @param startDt : 시작일
	 * @param endDt   : 종료일
	 */
	private BooleanBuilder betweenUpdateAt(LocalDate startDt, LocalDate endDt) {
		var booleanBuilder = new BooleanBuilder();
		if (startDt == null && endDt == null) {
			return booleanBuilder;
		}
		if (startDt != null) {
			booleanBuilder.and(goods.updateAt.goe(
					startDt.atTime(0, 0)));
		}
		if (endDt != null) {
			booleanBuilder.and(goods.updateAt.lt(
					endDt.atTime(0, 0).plusDays(1)));
		}
		return booleanBuilder;
	}
}
