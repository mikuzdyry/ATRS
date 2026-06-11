package com.xxx.atrs.order.controller;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.common.constant.OrderStatus;
import com.xxx.atrs.common.entity.BookingOrder;
import com.xxx.atrs.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/order/admin")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/list")
    public CommonResponse<Page<BookingOrder>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return CommonResponse.success(orderService.listAll(PageRequest.of(page, size)));
    }

    @GetMapping("/detail/{id}")
    public CommonResponse<BookingOrder> detail(@PathVariable Long id) {
        return CommonResponse.success(orderService.getById(id));
    }

    @PostMapping("/cancel/{id}")
    public CommonResponse<String> cancel(@PathVariable Long id) {
        orderService.adminCancel(id);
        return CommonResponse.success("取消成功");
    }

    @GetMapping("/statistics")
    public CommonResponse<?> statistics() {
        long total = orderService.listAll(PageRequest.of(0, 1)).getTotalElements();
        long paid = orderService.countByStatus(OrderStatus.PAID);
        return CommonResponse.success(Map.of("totalOrders", total, "paidOrders", paid));
    }
}
