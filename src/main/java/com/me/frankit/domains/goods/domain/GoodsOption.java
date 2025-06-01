package com.me.frankit.domains.goods.domain;

import com.me.frankit.base.domain.BaseDateEntity;
import com.me.frankit.domains.goods.constant.GoodsOptionType;
import com.me.frankit.domains.goods.dto.payload.GoodsOptionCreatePayload;
import com.me.frankit.domains.goods.dto.payload.GoodsOptionModifyPayload;
import jakarta.persistence.*;
import lombok.*;

import static com.me.frankit.global.constant.CommonConstant.Yn.N;
import static com.me.frankit.global.constant.CommonConstant.Yn.Y;

@Entity
@Table(name = "SSU_GOODS_OPT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class GoodsOption extends BaseDateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GOODS_OPT_ID", nullable = false, updatable = false)
	private Long goodsOptionId;

	@Column(name = "GOODS_OPT_NM", nullable = false, length = 100)
	private String goodsOptionName; // 옵션 이름

	@Enumerated(EnumType.STRING)
	@Column(name = "GOODS_OPT_TYPE", nullable = false, length = 10)
	private GoodsOptionType goodsOptionType; // 옵션 타입(INPUT 또는 SELECT)

	@Column(name = "GOOD_OPT_TYPE_DATA", columnDefinition = "TEXT", nullable = false)
	private String goodsOptionTypeData; // 옵션 타입 데이터(Json으로 저장)

	@Column(name = "GOODS_ADD_PRC", nullable = false)
	private Integer goodsAdditionalPrice; // 옵션 추가 금액

	@Column(name = "USE_YN", length = 1)
	private String useYn; // 사용 여부

	@Column(name = "USER_ID")
	private Long userId; // 유저 아이디

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOODS_ID", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Goods goods;

	public static GoodsOption create(Long userId, Goods goods, GoodsOptionCreatePayload payload) {
		return GoodsOption.builder()
				.userId(userId)
				.goods(goods)
				.goodsOptionName(payload.goodsOptionName())
				.goodsOptionType(payload.goodsOptionType())
				.goodsOptionTypeData(payload.goodsOptionTypeData())
				.goodsAdditionalPrice(payload.GoodsAdditionalPrice())
				.useYn(Y)
				.build();
	}

	public void modify(GoodsOptionModifyPayload payload) {
		this.goodsOptionName = payload.goodsOptionName();
		this.goodsOptionType = payload.goodsOptionType();
		this.goodsOptionTypeData = payload.goodsOptionTypeData();
		this.goodsAdditionalPrice = payload.goodsAdditionalPrice();
		this.useYn = payload.useYn();


	}

	/**
	 * 상품 옵션 목록 삭제
	 *  실제 삭제하지 않고 상태값만 N으로 변경
	 */
	public void delete() {
		this.useYn = N;
	}
}
