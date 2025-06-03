package com.me.frankit.domains.goods.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.me.frankit.domains.goods.dto.GoodsCreateRequest;
import com.me.frankit.domains.goods.dto.GoodsModifyRequest;
import com.me.frankit.domains.goods.setup.GoodsControllerTestSetup;
import com.me.frankit.global.util.ConverterUtil;
import com.me.frankit.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GoodsControllerTest extends IntegrationTest {
	@Autowired
	private GoodsControllerTestSetup goodsControllerTestSetup;

	@BeforeEach
	public void beforeEach() throws JsonProcessingException {
		super.setup();
		super.getDatabaseCleaner().truncateAll();
		goodsControllerTestSetup.setup();
	}

	@Test
	@DisplayName("상품 관리 등록 - 성공")
	void goods_create_success() throws Exception {
		// when
		var request = new GoodsCreateRequest(
				"테스트 상품",
				"테스트 상품입니다.",
				20000,
				3000
		);
		var resultActions = postGoods(request);
		resultActions.andExpect(status().isOk());
	}

	@ParameterizedTest
	@DisplayName("상품 관리 등록 - 실패")
	@ValueSource(strings = {"유저 없음", "권한 없음"})
	void goods_create_failure(String testCase) throws Exception {
		var request = new GoodsCreateRequest(
				"테스트 상품s",
				"테스트 상품입니다.",
				20000,
				3000
		);

		if (testCase.equals("권한 없음")) {
			goodsControllerTestSetup.setupUserWithWrongRole();
		}

		// when
		var resultActions = getMockMvc().perform(post("/frankit/goods/create")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", String.format("Bearer %s",
								testCase.equals("권한 없음")
										? getMockManagerJwtToken()
										: getMockUserJwtTokenInvalid())) // 유저가 없을 경우 잘못된 토큰 사용
						.content(ConverterUtil.convertObjectToJson(request))
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print());

		// then
		if (testCase.equals("유저 없음")) {
			resultActions.andExpect(status().isNotFound());
		} else if (testCase.equals("권한 없음")) {
			resultActions.andExpect(status().isForbidden());
		}
	}

	@Test
	@DisplayName("상품 목록 조회 - 성공")
	void goods_list_success() throws Exception {
		// given
		goodsControllerTestSetup.setupGoods();

		// when & then
		getGoods().andExpect(status().isOk())
				.andExpect(jsonPath("$.data.contents[0].goodsName").value("테스트 상품 1"))
				.andExpect(jsonPath("$.data.contents[0].goodsContents").value("설명 1"))
				.andExpect(jsonPath("$.data.contents[0].priceAmount").value(1001))
				.andExpect(jsonPath("$.data.contents[0].deliveryPrice").value(500))
				.andExpect(jsonPath("$.data.contents[19].goodsName").value("테스트 상품 20"))
				.andExpect(jsonPath("$.data.contents[19].goodsContents").value("설명 20"))
				.andExpect(jsonPath("$.data.contents[19].priceAmount").value(1020))
				.andExpect(jsonPath("$.data.contents[19].deliveryPrice").value(500))
				.andExpect(jsonPath("$.data.totalCount").value(100))
				.andExpect(jsonPath("$.data.contents.length()").value(20)) // 기본 pageSize 20
		;
	}

	@Test
	@DisplayName("상품 상세 조회 - 성공")
	void goods_detail_success() throws Exception {
		// given
		goodsControllerTestSetup.setupGoods(); // 상품 100개 생성됨
		Long goodsId = 1L;

		// then
		getGoodsDetail(goodsId).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.goodsId").value(1))
				.andExpect(jsonPath("$.data.goodsName").value("테스트 상품 1"))
				.andExpect(jsonPath("$.data.goodsContents").value("설명 1"))
				.andExpect(jsonPath("$.data.priceAmount").value(1001))
				.andExpect(jsonPath("$.data.deliveryPrice").value(500));
	}

	@ParameterizedTest
	@DisplayName("상품 상세 조회 - 실패")
	@ValueSource(strings = {"유저 없음", "권한 없음", "상품 관리 없음"})
	void goods_detail_failure(String testCase) throws Exception {
		// given
		var invalidGoodsId = 9999L;

		if (testCase.equals("권한 없음")) {
			goodsControllerTestSetup.setupUserWithWrongRole();
		}

		// when
		var resultActions = getMockMvc().perform(get("/frankit/goods/{goodsId}",
						testCase.equals("상품 관리 없음") ? invalidGoodsId : 1L)
						.header("Authorization", String.format("Bearer %s",
								testCase.equals("권한 없음")
										? getMockManagerJwtToken()
										: getMockUserJwtTokenInvalid())) // 유저가 없을 경우 잘못된 토큰 사용
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print());

		// then
		if (testCase.equals("유저 없음")) {
			resultActions.andExpect(status().isNotFound());
		} else if (testCase.equals("권한 없음")) {
			resultActions.andExpect(status().isForbidden());
		} else if (testCase.equals("상품 관리 없음")) {
			resultActions.andExpect(status().isNotFound());
		}
	}

	private ResultActions getGoodsDetail(Long goodsId) throws Exception {
		return getMockMvc().perform(get("/frankit/goods/{goodsId}", goodsId)
						.header("Authorization", String.format("Bearer %s", getMockUserJwtToken()))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}

	@Test
	@DisplayName("상품 수정 - 성공")
	void goods_update_success() throws Exception {
		// given
		goodsControllerTestSetup.setupGoods();
		var request = new GoodsModifyRequest(
				"수정 상품",
				"수정 설명",
				15000,
				1000,
				"Y"
		);
		var resultActions = modifyGoods(1L, request);

		// then
		resultActions.andExpect(status().isOk());
	}

	@ParameterizedTest
	@DisplayName("상품 수정 - 실패")
	@ValueSource(strings = {"유저 없음", "권한 없음", "상품 관리 없음", "유효성 오류"})
	void goods_modify_failure(String testCase) throws Exception {
		// given
		goodsControllerTestSetup.setupGoods();
		var request = testCase.equals("유효성 오류")
				? new GoodsModifyRequest(null, null, null, null, null)
				: new GoodsModifyRequest("정상", "정상", 10000, 1000, "Y");

		var invalidGoodsId = 9999L;

		if (testCase.equals("권한 없음")) {
			goodsControllerTestSetup.setupUserWithWrongRole();
		}

		// when
		var resultActions = getMockMvc().perform(put("/frankit/goods/{goodsId}",
						testCase.equals("상품 관리 없음") ? invalidGoodsId : 1L)
						.header("Authorization", String.format("Bearer %s",
								testCase.equals("권한 없음")
										? getMockManagerJwtToken()
										: getMockUserJwtTokenInvalid())) // 유저가 없을 경우 잘못된 토큰 사용
						.contentType(MediaType.APPLICATION_JSON)
						.content(ConverterUtil.convertObjectToJson(request)))
				.andDo(print());

		// then
		if (testCase.equals("유저 없음")) {
			resultActions.andExpect(status().isNotFound());
		} else if (testCase.equals("권한 없음")) {
			resultActions.andExpect(status().isForbidden());
		} else if (testCase.equals("상품 관리 없음")) {
			resultActions.andExpect(status().isNotFound());
		} else if (testCase.equals("유효성 오류")) {
			resultActions.andExpect(status().isBadRequest());
		}
	}

	@Test
	@DisplayName("상품 관리 삭제 - 성공")
	void delete_goods_success() throws Exception {
		// given
		goodsControllerTestSetup.setupGoods(); // ID 1번 생성됨

		// when
		ResultActions resultActions = deleteGoods(1L);

		// then
		resultActions.andExpect(status().isOk());
	}

	private ResultActions getGoods() throws Exception {
		return getMockMvc().perform(get("/frankit/goods/list")
						.param("searchType", "REGISTER_DATE") // 이 부분은 enum에 맞게 선택적으로
						.param("startDt", "2024-01-01")
						.param("endDt", "2026-12-31")
						.param("page", "0")
						.param("size", "20")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", String.format("Bearer %s", getMockUserJwtToken()))
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}

	private ResultActions postGoods(GoodsCreateRequest request) throws Exception {
		var body = ConverterUtil.convertObjectToJson(request);
		return getMockMvc().perform(post("/frankit/goods/create")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", String.format("Bearer %s", getMockUserJwtToken()))
						.accept(MediaType.APPLICATION_JSON)
						.content(body))
				.andDo(print());
	}

	private ResultActions modifyGoods(Long goodsId, GoodsModifyRequest request) throws Exception {
		return getMockMvc().perform(put("/frankit/goods/{goodsId}", goodsId)
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", String.format("Bearer %s", getMockUserJwtToken()))
						.content(ConverterUtil.convertObjectToJson(request))
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}

	private ResultActions deleteGoods(Long goodsId) throws Exception {
		return getMockMvc().perform(patch("/frankit/goods/delete/{goodsId}", goodsId)
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", String.format("Bearer %s", getMockUserJwtToken()))
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
}