package com.xxx.atrs.order.repository;

import com.xxx.atrs.common.entity.OrderPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPassengerRepository extends JpaRepository<OrderPassenger, Long> {
}
