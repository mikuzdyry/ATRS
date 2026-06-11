package com.xxx.atrs.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderNoGenerator {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Map<String, AtomicInteger> SEQUENCE_MAP = new ConcurrentHashMap<>();

    public static synchronized String generate() {
        String today = LocalDate.now().format(DF);
        AtomicInteger seq = SEQUENCE_MAP.computeIfAbsent(today, k -> new AtomicInteger(0));
        return "ATRS" + today + String.format("%06d", seq.incrementAndGet());
    }
}
