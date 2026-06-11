package com.xxx.atrs.service;

import com.xxx.atrs.dto.BookingRequestDTO;
import com.xxx.atrs.entity.BookingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookingService {
    BookingOrder createOrder(Long userId, BookingRequestDTO dto);
    BookingOrder pay(Long userId, String orderNo);
    void cancel(Long userId, String orderNo);
    BookingOrder getByOrderNo(String orderNo);
    List<BookingOrder> listByUser(Long userId);
    Page<BookingOrder> listAll(Pageable pageable);
    void adminCancel(Long orderId);
}
