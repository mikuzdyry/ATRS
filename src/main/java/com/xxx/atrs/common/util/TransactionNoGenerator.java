package com.xxx.atrs.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionNoGenerator {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generate() {
        String timestamp = LocalDateTime.now().format(DF);
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "TXN" + timestamp + random;
    }
}
