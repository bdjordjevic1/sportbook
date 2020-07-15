package com.ronin.sportbook.common.security.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private boolean success;
    private String message;
    private String key;

    public ApiResponse(boolean success, String message, String key) {
        this.success = success;
        this.message = message;
        this.key = key;
    }
}
