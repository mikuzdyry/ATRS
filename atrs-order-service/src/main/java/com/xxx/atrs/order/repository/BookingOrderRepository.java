package com.xxx.atrs.order.repository;

import com.xxx.atrs.common.constant.OrderStatus;
import com.xxx.atrs.common.entity.BookingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingOrderRepository extends JpaRepository<BookingOrder, Long> {

    Optional<BookingOrder> findByOrderNo(String orderNo);

    List<BookingOrder> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<BookingOrder> findAllByOrderByCreatedAtDesc(Pageable pageable);

    long countByStatus(OrderStatus status);

    long countByFlightId(Long flightId);

    List<BookingOrder> findByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime time);
}
