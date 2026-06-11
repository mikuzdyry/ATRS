package com.xxx.atrs.repository;

import com.xxx.atrs.entity.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Optional<PaymentRecord> findByOrderId(Long orderId);
}
