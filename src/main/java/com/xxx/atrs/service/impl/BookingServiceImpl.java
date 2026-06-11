package com.xxx.atrs.service.impl;

import com.xxx.atrs.common.constant.FlightStatus;
import com.xxx.atrs.common.constant.OrderStatus;
import com.xxx.atrs.common.constant.SeatClass;
import com.xxx.atrs.common.exception.BusinessException;
import com.xxx.atrs.common.util.OrderNoGenerator;
import com.xxx.atrs.common.util.TicketNoGenerator;
import com.xxx.atrs.common.util.TransactionNoGenerator;
import com.xxx.atrs.dto.BookingRequestDTO;
import com.xxx.atrs.dto.PassengerDTO;
import com.xxx.atrs.entity.*;
import com.xxx.atrs.repository.*;
import com.xxx.atrs.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingOrderRepository orderRepository;
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingOrderRepository orderRepository, FlightRepository flightRepository,
                              FlightSeatRepository flightSeatRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.flightRepository = flightRepository;
        this.flightSeatRepository = flightSeatRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BookingOrder createOrder(Long userId, BookingRequestDTO dto) {
        Flight flight = flightRepository.findById(dto.getFlightId())
                .orElseThrow(() -> new BusinessException("航班不存在"));
        if (flight.getStatus() != FlightStatus.SCHEDULED) {
            throw new BusinessException("该航班已取消或延误，无法预定");
        }

        SeatClass seatClass = SeatClass.valueOf(dto.getSeatClass());
        FlightSeat flightSeat = flightSeatRepository.findByFlightIdAndSeatClass(dto.getFlightId(), seatClass)
                .orElseThrow(() -> new BusinessException("该舱位不存在"));

        int passengerCount = dto.getPassengers().size();
        if (flightSeat.getAvailableSeats() < passengerCount) {
            throw new BusinessException("座位不足，当前仅剩 " + flightSeat.getAvailableSeats() + " 座");
        }

        flightSeat.setBookedCount(flightSeat.getBookedCount() + passengerCount);
        flightSeatRepository.save(flightSeat);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        BookingOrder order = new BookingOrder();
        order.setOrderNo(OrderNoGenerator.generate());
        order.setUser(user);
        order.setFlight(flight);
        order.setSeatClass(seatClass);
        order.setTotalPrice(flightSeat.getPrice().multiply(BigDecimal.valueOf(passengerCount)));
        order.setPassengerCount(passengerCount);
        order.setStatus(OrderStatus.PENDING);
        order.setContactName(dto.getContactName());
        order.setContactPhone(dto.getContactPhone());
        order.setContactEmail(dto.getContactEmail());
        order.setRemark(dto.getRemark());

        for (int i = 0; i < dto.getPassengers().size(); i++) {
            PassengerDTO pdto = dto.getPassengers().get(i);
            OrderPassenger passenger = new OrderPassenger();
            passenger.setOrder(order);
            passenger.setName(pdto.getName());
            passenger.setIdCard(pdto.getIdCard());
            passenger.setPhone(pdto.getPhone());
            order.getPassengers().add(passenger);
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public BookingOrder pay(Long userId, String orderNo) {
        BookingOrder order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessException("无权操作该订单");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("订单状态异常，无法支付");
        }

        PaymentRecord payment = new PaymentRecord();
        payment.setOrder(order);
        payment.setTransactionNo(TransactionNoGenerator.generate());
        payment.setAmount(order.getTotalPrice());
        payment.setPaymentMethod("SIMULATED");
        payment.setStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now());

        order.setPaymentRecord(payment);
        order.setStatus(OrderStatus.PAID);

        for (int i = 0; i < order.getPassengers().size(); i++) {
            OrderPassenger passenger = order.getPassengers().get(i);
            passenger.setTicketNo(TicketNoGenerator.generate(order.getOrderNo(), i + 1));
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancel(Long userId, String orderNo) {
        BookingOrder order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessException("无权操作该订单");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("订单已取消");
        }

        releaseSeats(order);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public BookingOrder getByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    @Override
    public List<BookingOrder> listByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Page<BookingOrder> listAll(Pageable pageable) {
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    @Transactional
    public void adminCancel(Long orderId) {
        BookingOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("订单已取消");
        }

        releaseSeats(order);
        if (order.getStatus() == OrderStatus.PAID) {
            order.setStatus(OrderStatus.REFUNDED);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }
        orderRepository.save(order);
    }

    private void releaseSeats(BookingOrder order) {
        FlightSeat flightSeat = flightSeatRepository
                .findByFlightIdAndSeatClass(order.getFlight().getId(), order.getSeatClass())
                .orElse(null);
        if (flightSeat != null) {
            flightSeat.setBookedCount(Math.max(0, flightSeat.getBookedCount() - order.getPassengerCount()));
            flightSeatRepository.save(flightSeat);
        }
    }
}
