package com.me.frankit.domains.goods.repository;

import com.me.frankit.base.repository.BaseRepository;
import com.me.frankit.domains.goods.domain.GoodsOption;
import com.me.frankit.domains.goods.domain.QGoods;
import com.me.frankit.domains.goods.domain.QGoodsOption;
import com.me.frankit.domains.goods.dto.query.GoodsOptionFindQuery;
import com.me.frankit.domains.user.domain.QUser;
import com.me.frankit.global.constant.CommonConstant.Yn;
import com.me.frankit.global.constant.SearchType;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class GoodsOptionRepository extends BaseRepository<GoodsOption, Long> {
	private static final QGoodsOption goodsOption = QGoodsOption.goodsOption;
	private static final QUser user = QUser.user;
	private static final QGoods goods = QGoods.goods;

	public Long countByGoods(Long goodsId) {
		return select(goodsOption.count()).from(goodsOption)
				.where(goodsOption.goods.goodsId.eq(goodsId)
						.and(goodsOption.useYn.eq(Yn.Y))).fetchOne();
	}

	public Optional<GoodsOption> findGoodsOption(Long goodsOptionId) {
		return Optional.ofNullable(selectFrom(goodsOption)
				.where(goodsOption.goodsOptionId.eq(goodsOptionId))
				.fetchFirst());
	}

	public Page<GoodsOptionFindQuery.Result> findGoodsOption(Long userId, GoodsOptionFindQuery request, Pageable pageable) {
		var contentQuery = select(GoodsOptionFindQuery.Result.class,
				goods.goodsId,
				goods.goodsName,
				goodsOption.goodsOptionId,
				goodsOption.goodsOptionName,
				goodsOption.goodsOptionType,
				goodsOption.goodsOptionTypeData,
				goodsOption.goodsAdditionalPrice,
				goodsOption.useYn
		)
				.from(goodsOption)
				.innerJoin(goods).on(goodsOption.goods.goodsId.eq(goods.goodsId))
				.innerJoin(user).on(goods.user.userId.eq(user.userId))
				.where(getConditionBySearchType(request.searchType(), request.startDt(), request.endDt())
						.and(goods.user.userId.eq(userId)));

		var countQuery = select(goodsOption.count())
				.from(goodsOption)
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
			booleanBuilder.and(goodsOption.registerAt.goe(
					startDt.atTime(0, 0)));
		}
		if (endDt != null) {
			booleanBuilder.and(goodsOption.registerAt.lt(
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
			booleanBuilder.and(goodsOption.updateAt.goe(
					startDt.atTime(0, 0)));
		}
		if (endDt != null) {
			booleanBuilder.and(goodsOption.updateAt.lt(
					endDt.atTime(0, 0).plusDays(1)));
		}
		return booleanBuilder;
	}
}
