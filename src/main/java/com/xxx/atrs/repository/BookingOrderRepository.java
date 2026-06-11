package com.xxx.atrs.repository;

import com.xxx.atrs.entity.BookingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookingOrderRepository extends JpaRepository<BookingOrder, Long> {
    Optional<BookingOrder> findByOrderNo(String orderNo);
    List<BookingOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
    Page<BookingOrder> findAllByOrderByCreatedAtDesc(Pageable pageable);
    long countByStatus(String status);
    long countByFlightId(Long flightId);
}
