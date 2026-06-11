package com.xxx.atrs.common.util;

public class TicketNoGenerator {

    public static String generate(String orderNo, int index) {
        return orderNo + "-" + String.format("%03d", index);
    }
}
