package com.xxx.atrs.order.service;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.common.constant.OrderStatus;
import com.xxx.atrs.common.constant.SeatClass;
import com.xxx.atrs.common.dto.BookingRequestDTO;
import com.xxx.atrs.common.dto.PassengerDTO;
import com.xxx.atrs.common.entity.*;
import com.xxx.atrs.common.exception.BusinessException;
import com.xxx.atrs.common.feign.FlightFeignClient;
import com.xxx.atrs.common.util.OrderNoGenerator;
import com.xxx.atrs.common.util.TicketNoGenerator;
import com.xxx.atrs.common.util.TransactionNoGenerator;
import com.xxx.atrs.order.repository.BookingOrderRepository;
import com.xxx.atrs.order.repository.OrderPassengerRepository;
import com.xxx.atrs.order.repository.PaymentRecordRepository;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final BookingOrderRepository orderRepo;
    private final OrderPassengerRepository passengerRepo;
    private final PaymentRecordRepository paymentRepo;
    private final FlightFeignClient flightFeignClient;

    @GlobalTransactional(name = "create-order", rollbackFor = Exception.class)
    public BookingOrder createOrder(Long userId, String username, BookingRequestDTO dto) {
        // 1. 获取航班信息
        CommonResponse<Flight> flightResp = flightFeignClient.getFlight(dto.getFlightId());
        if (flightResp == null || flightResp.getData() == null) {
            throw new BusinessException("航班不存在");
        }
        Flight flight = flightResp.getData();

        // 2. 获取座位库存
        CommonResponse<?> seatResp = flightFeignClient.getSeatInventory(dto.getFlightId(), dto.getSeatClass());
        if (seatResp == null || seatResp.getData() == null) {
            throw new BusinessException("舱位不存在");
        }
        Map<String, Object> seatData = (Map<String, Object>) seatResp.getData();
        int availableSeats = (Integer) seatData.get("availableSeats");
        BigDecimal price = new BigDecimal(seatData.get("price").toString());

        if (availableSeats < dto.getPassengers().size()) {
            throw new BusinessException("座位不足，当前可用：" + availableSeats);
        }

        // 3. 扣减座位（Seata 分布式事务）
        Map<String, Object> deductReq = new HashMap<>();
        deductReq.put("flightId", dto.getFlightId());
        deductReq.put("seatClass", dto.getSeatClass());
        deductReq.put("count", dto.getPassengers().size());
        CommonResponse<Void> deductResp = flightFeignClient.deductSeats(deductReq);
        if (deductResp == null || deductResp.getCode() != 200) {
            throw new BusinessException("扣减座位失败");
        }

        // 4. 构建订单
        String orderNo = OrderNoGenerator.generate();
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(dto.getPassengers().size()));

        BookingOrder order = BookingOrder.builder()
                .orderNo(orderNo)
                .passengerCount(dto.getPassengers().size())
                .seatClass(SeatClass.valueOf(dto.getSeatClass()))
                .totalPrice(totalPrice)
                .contactName(dto.getContactName())
                .contactPhone(dto.getContactPhone())
                .contactEmail(dto.getContactEmail())
                .remark(dto.getRemark())
                .status(OrderStatus.PENDING)
                .build();

        // 关联 User 和 Flight（仅设 ID，避免跨库查询问题）
        User userRef = new User();
        userRef.setId(userId);
        order.setUser(userRef);
        order.setFlight(flight);

        // 添加乘客
        List<OrderPassenger> passengers = new ArrayList<>();
        for (PassengerDTO pDto : dto.getPassengers()) {
            OrderPassenger p = new OrderPassenger();
            BeanUtils.copyProperties(pDto, p);
            p.setOrder(order);
            passengers.add(p);
        }
        order.setPassengers(passengers);

        orderRepo.save(order);
        log.info("订单创建成功: {}", orderNo);
        return order;
    }

    @GlobalTransactional(name = "pay-order", rollbackFor = Exception.class)
    public BookingOrder pay(Long userId, String orderNo) {
        BookingOrder order = orderRepo.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("订单状态不允许支付");
        }

        // 创建支付记录
        PaymentRecord payment = PaymentRecord.builder()
                .order(order)
                .transactionNo(TransactionNoGenerator.generate())
                .amount(order.getTotalPrice())
                .paymentMethod("SIMULATED")
                .paymentStatus("SUCCESS")
                .paidAt(LocalDateTime.now())
                .build();
        paymentRepo.save(payment);
        order.setPaymentRecord(payment);

        // 生成票号
        for (int i = 0; i < order.getPassengers().size(); i++) {
            order.getPassengers().get(i).setTicketNo(TicketNoGenerator.generate(orderNo, i));
        }

        order.setStatus(OrderStatus.PAID);
        orderRepo.save(order);
        log.info("支付成功: {}", orderNo);
        return order;
    }

    @GlobalTransactional(name = "cancel-order", rollbackFor = Exception.class)
    public void cancel(Long userId, String orderNo) {
        BookingOrder order = orderRepo.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.REFUNDED) {
            throw new BusinessException("订单已取消");
        }

        releaseSeatsCall(order);
        order.setStatus(order.getStatus() == OrderStatus.PAID ? OrderStatus.REFUNDED : OrderStatus.CANCELLED);
        orderRepo.save(order);
        log.info("订单取消成功: {}", orderNo);
    }

    @GlobalTransactional(name = "admin-cancel-order", rollbackFor = Exception.class)
    public void adminCancel(Long orderId) {
        BookingOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.REFUNDED) {
            throw new BusinessException("订单已取消");
        }

        releaseSeatsCall(order);
        order.setStatus(order.getStatus() == OrderStatus.PAID ? OrderStatus.REFUNDED : OrderStatus.CANCELLED);
        orderRepo.save(order);
        log.info("管理员取消订单: {}", order.getOrderNo());
    }

    private void releaseSeatsCall(BookingOrder order) {
        Map<String, Object> req = new HashMap<>();
        req.put("flightId", order.getFlight().getId());
        req.put("seatClass", order.getSeatClass().name());
        req.put("count", order.getPassengerCount());
        flightFeignClient.releaseSeats(req);
    }

    public BookingOrder getByOrderNo(String orderNo) {
        return orderRepo.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    public List<BookingOrder> listByUser(Long userId) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Page<BookingOrder> listAll(Pageable pageable) {
        return orderRepo.findAllByOrderByCreatedAtDesc(pageable);
    }

    public BookingOrder getById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    public long countByStatus(OrderStatus status) {
        return orderRepo.countByStatus(status);
    }

    /** 定时任务：每 5 分钟自动取消 30 分钟未支付的订单 */
    @Scheduled(fixedRate = 300000)
    public void autoCancelPendingOrders() {
        LocalDateTime deadline = LocalDateTime.now().minusMinutes(30);
        List<BookingOrder> expired = orderRepo.findByStatusAndCreatedAtBefore(OrderStatus.PENDING, deadline);
        for (BookingOrder order : expired) {
            try {
                releaseSeatsCall(order);
                order.setStatus(OrderStatus.CANCELLED);
                orderRepo.save(order);
                log.info("超时自动取消订单: {}", order.getOrderNo());
            } catch (Exception e) {
                log.error("自动取消订单失败: {}", order.getOrderNo(), e);
            }
        }
    }
}
