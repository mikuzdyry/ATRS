package com.xxx.atrs.common.util;

/**
 * 票号生成器: {orderNo}-{index}
 */
public class TicketNoGenerator {

    public static String generate(String orderNo, int index) {
        return String.format("%s-%03d", orderNo, index + 1);
    }
}
