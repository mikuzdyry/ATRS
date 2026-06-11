package com.xxx.atrs.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    private int code;
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(200, "success", data);
    }

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(200, "success", null);
    }

    public static <T> CommonResponse<T> error(int code, String message) {
        return new CommonResponse<>(code, message, null);
    }

    public static <T> CommonResponse<T> error(String message) {
        return new CommonResponse<>(500, message, null);
    }
}
