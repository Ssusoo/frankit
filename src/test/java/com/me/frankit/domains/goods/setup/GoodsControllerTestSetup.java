package com.me.frankit.domains.goods.setup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.me.frankit.domains.goods.domain.Goods;
import com.me.frankit.domains.user.domain.User;
import com.me.frankit.domains.user.domain.embed.UserRole;
import com.me.frankit.test.config.TestProfile;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile(TestProfile.TEST)
@RequiredArgsConstructor
@Component
public class GoodsControllerTestSetup {
	private final EntityManager entityManager;
	private User user;

	@Transactional
	public void setup() throws JsonProcessingException {
		createManagerUser();
		user = createUser();
	}

	@Transactional
	public void setupGoods() {
		createTestGoods(100);
	}

	/**
	 * 상품 관리 등록 100개 테스트 케이스
	 * @param count
	 */
	private void createTestGoods(int count) {
		for (int i = 1; i <= count; i++) {
			var goods = Goods.builder()
					.goodsName("테스트 상품 " + i)
					.goodsContents("설명 " + i)
					.goodsPrice(1000 + i)
					.deliveryPrice(500)
					.useYn("Y")
					.user(user)
					.build();
			entityManager.persist(goods);
		}
	}

	private void createManagerUser() {
		var managerUser = User.builder()
				.userEmail("test@manager.com")
				.userPassword("1234")
				.userName("김관리자")
				.userPhone("010-2222-2222")
				.role(UserRole.MANAGER)
				.build();
		entityManager.persist(managerUser);
	}

	private User createUser() {
		var user = User.builder()
				.userEmail("frankit@test.com")
				.userPassword("1234")
				.userName("김테스트")
				.userPhone("010-1234-1234")
				.role(UserRole.USER)
				.build();
		entityManager.persist(user);
		return user;
	}

	@Transactional
	public void setupUserWithWrongRole() {
		var user = User.builder()
				.userEmail("test@admin.com")
				.userName("김관리자")
				.userPassword("1234")
				.userPhone("010-1234-1234")
				.role(UserRole.MANAGER)
				.build();
		entityManager.persist(user);
	}
}
