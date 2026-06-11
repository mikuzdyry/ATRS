package com.xxx.atrs.order.controller;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.common.dto.BookingRequestDTO;
import com.xxx.atrs.common.entity.BookingOrder;
import com.xxx.atrs.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public CommonResponse<BookingOrder> create(@RequestHeader("X-User-Id") Long userId,
                                                @RequestHeader("X-Username") String username,
                                                @Valid @RequestBody BookingRequestDTO dto) {
        return CommonResponse.success(orderService.createOrder(userId, username, dto));
    }

    @PostMapping("/pay/{orderNo}")
    public CommonResponse<BookingOrder> pay(@RequestHeader("X-User-Id") Long userId,
                                             @PathVariable String orderNo) {
        return CommonResponse.success(orderService.pay(userId, orderNo));
    }

    @PostMapping("/cancel/{orderNo}")
    public CommonResponse<String> cancel(@RequestHeader("X-User-Id") Long userId,
                                          @PathVariable String orderNo) {
        orderService.cancel(userId, orderNo);
        return CommonResponse.success("取消成功");
    }

    @GetMapping("/list")
    public CommonResponse<List<BookingOrder>> list(@RequestHeader("X-User-Id") Long userId) {
        return CommonResponse.success(orderService.listByUser(userId));
    }

    @GetMapping("/detail/{orderNo}")
    public CommonResponse<BookingOrder> detail(@PathVariable String orderNo) {
        return CommonResponse.success(orderService.getByOrderNo(orderNo));
    }
}
