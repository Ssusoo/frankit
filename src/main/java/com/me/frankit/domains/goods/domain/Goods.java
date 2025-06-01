package com.me.frankit.domains.goods.domain;

import com.me.frankit.base.domain.BaseDateEntity;
import com.me.frankit.domains.goods.dto.payload.GoodsCreatePayload;
import com.me.frankit.domains.goods.dto.payload.GoodsModifyPayload;
import com.me.frankit.domains.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.me.frankit.global.constant.CommonConstant.Yn.N;
import static com.me.frankit.global.constant.CommonConstant.Yn.Y;

@Entity
@Table(name = "SSU_GOODS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Goods extends BaseDateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GOODS_ID", nullable = false, updatable = false)
	private Long goodsId; //

	@Column(name = "GOODS_NM", length = 100, nullable = false)
	private String goodsName; // 상품 이름

	@Column(name = "GOODS_CONT", nullable = false, columnDefinition = "TEXT")
	private String goodsContents; // 상품 설명

	@Column(name = "GOODS_PRC", nullable = false)
	private Integer goodsPrice; // 상품 가격

	@Column(name = "DELIV_PRC", nullable = false)
	private Integer deliveryPrice; // 배송비

	@Column(name = "USE_YN", length = 1)
	private String useYn; // 사용 여부

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "USR_ID", nullable = false, updatable = false,
			foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private User user;

	@OneToMany(mappedBy = "goods", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	private final List<GoodsOption> goodsOptions = new ArrayList<>(); // 상품 옵션

	/**
	 * 상품 생성
	 */
	public static Goods create(User user, GoodsCreatePayload payload) {
		return Goods.builder()
				.user(user)
				.goodsName(payload.goodsName())
				.goodsContents(payload.goodsContents())
				.goodsPrice(payload.goodsPrice())
				.deliveryPrice(payload.deliveryPrice())
				.useYn(Y)
				.build();
	}

	/**
	 * 상품 수정
	 */
	public void modify(GoodsModifyPayload payload) {
		this.goodsName = payload.goodsName();
		this.goodsContents = payload.goodsContents();
		this.goodsPrice = payload.goodsPrice();
		this.deliveryPrice = payload.deliveryPrice();
		this.useYn = payload.useYn();
	}

	/**
	 * 상품 삭제
	 *  실제 삭제하지 않고 상태값만 N으로 변경
	 */
	public void delete() {
		this.useYn = N;
	}
}
