package com.me.frankit.domains.goods.controller;

import com.me.frankit.base.controller.BaseController;
import com.me.frankit.domains.goods.application.GoodsCreateService;
import com.me.frankit.domains.goods.application.GoodsFindService;
import com.me.frankit.domains.goods.application.GoodsModifyService;
import com.me.frankit.domains.goods.dto.GoodsCreateRequest;
import com.me.frankit.domains.goods.dto.GoodsDetailFindResult;
import com.me.frankit.domains.goods.dto.GoodsFindRequest;
import com.me.frankit.domains.goods.dto.GoodsModifyRequest;
import com.me.frankit.global.dto.ApiResponse;
import com.me.frankit.global.dto.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/frankit/goods")
public class GoodsController extends BaseController {
	private final GoodsCreateService goodsCreateService;
	private final GoodsFindService goodsFindService;
	private final GoodsModifyService goodsModifyService;

	@Operation(summary = "상품 목록 조회")
	@GetMapping("/list")
	public ApiResponse<Pagination<GoodsFindRequest.Result>> getGoods(
			@Valid GoodsFindRequest request,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return ok(goodsFindService.getGoods(request, page, size));
	}

	@Operation(summary = "상품 등록")
	@PostMapping("/create")
	public ApiResponse<Object> postGoods(@Valid @RequestBody GoodsCreateRequest request) {
		goodsCreateService.create(request);
		return ok();
	}

	@Operation(summary = "상품 상세 조회")
	@GetMapping(path = "/{goodsId}")
	public ApiResponse<GoodsDetailFindResult> getGoodsDetail(@PathVariable Long goodsId) {
		return ok(goodsFindService.getGoodsDetail(goodsId));
	}

	@Operation(summary = "상품 수정")
	@PutMapping(path = "/{goodsId}")
	public ApiResponse<Object> putGoods(@PathVariable Long goodsId, @Valid @RequestBody GoodsModifyRequest request) {
		goodsModifyService.modify(goodsId, request);
		return ok();
	}

	@Operation(summary = "상품 삭제")
	@PatchMapping(path = "/delete/{goodsId}")
	public ApiResponse<Object> deleteGoods(@PathVariable Long goodsId) {
		goodsModifyService.deleteGoods(goodsId);
		return ok();
	}
}
