package com.me.frankit.domains.goods.setup;

import com.me.frankit.domains.user.domain.User;
import com.me.frankit.domains.user.domain.embed.UserRole;

public class GoodsCreateServiceTestSetup {
	public static User getUser() {
		return User.builder()
				.userId(1L)
				.userEmail("test@frankit.com")
				.userPassword("1234")
				.userPhone("010-1111-2222")
				.role(UserRole.USER)
				.build();
	}

	public static User getNoUserRole() {
		return User.builder()
				.userId(1L)
				.userEmail("noUserRole@test.com")
				.userName("김테스트")
				.userPassword("1234")
				.role(UserRole.MANAGER) // User 권한 아님
				.build();
	}
}
