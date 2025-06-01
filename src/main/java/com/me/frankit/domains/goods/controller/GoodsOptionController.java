package com.me.frankit.domains.goods.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.me.frankit.base.controller.BaseController;
import com.me.frankit.domains.goods.application.GoodsOptionCreateService;
import com.me.frankit.domains.goods.application.GoodsOptionFindService;
import com.me.frankit.domains.goods.application.GoodsOptionModifyService;
import com.me.frankit.domains.goods.dto.GoodsOptionCreateRequest;
import com.me.frankit.domains.goods.dto.GoodsOptionDetailFindResult;
import com.me.frankit.domains.goods.dto.GoodsOptionFindRequest;
import com.me.frankit.domains.goods.dto.GoodsOptionModifyRequest;
import com.me.frankit.global.dto.ApiResponse;
import com.me.frankit.global.dto.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품 옵션 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/frankit/goods/option")
public class GoodsOptionController extends BaseController {
	private final GoodsOptionCreateService goodsOptionCreateService;
	private final GoodsOptionFindService goodsOptionFindService;
	private final GoodsOptionModifyService goodsOptionModifyService;

	@Operation(summary = "상품 옵션 목록 조회")
	@GetMapping("/list")
	public ApiResponse<Pagination<GoodsOptionFindRequest.Result>> getLogisticsGoods(
			@Valid GoodsOptionFindRequest request,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return ok(goodsOptionFindService.getGoods(request, page, size));
	}

	@Operation(summary = "상품 옵션 등록")
	@PostMapping("/create")
	public ApiResponse<Object> postLogisticsGoods(@Valid GoodsOptionCreateRequest request) throws JsonProcessingException {
		goodsOptionCreateService.create(request);
		return ok();
	}

	@Operation(summary = "상품 옵션 상세 조회")
	@GetMapping(path = "/{goodsOptionId}")
	public ApiResponse<GoodsOptionDetailFindResult> getGoodsDetail(@PathVariable Long goodsOptionId) {
		return ok(goodsOptionFindService.getGoodsDetail(goodsOptionId));
	}

	@Operation(summary = "상품 옵션 수정")
	@PutMapping(path = "/{goodsOptionId}")
	public ApiResponse<Object> putGoods(@PathVariable Long goodsOptionId,
	                                    @Valid GoodsOptionModifyRequest request) throws JsonProcessingException {
		goodsOptionModifyService.modify(goodsOptionId, request);
		return ok();
	}

	@Operation(summary = "상품 옵션 삭제")
	@PatchMapping(path = "/delete/{goodsOptionId}")
	public ApiResponse<Object> deleteGoods(@PathVariable Long goodsOptionId) {
		goodsOptionModifyService.deleteGoods(goodsOptionId);
		return ok();
	}
}
