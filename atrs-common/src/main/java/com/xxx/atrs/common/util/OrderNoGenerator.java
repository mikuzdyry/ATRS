package com.xxx.atrs.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单号生成器: ATRSyyyyMMddXXXXXX
 */
public class OrderNoGenerator {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final ConcurrentHashMap<String, AtomicInteger> COUNTERS = new ConcurrentHashMap<>();

    public static synchronized String generate() {
        String today = LocalDate.now().format(DF);
        COUNTERS.computeIfAbsent(today, k -> new AtomicInteger(0));
        int seq = COUNTERS.get(today).incrementAndGet();
        return String.format("ATRS%s%06d", today, seq);
    }
}
