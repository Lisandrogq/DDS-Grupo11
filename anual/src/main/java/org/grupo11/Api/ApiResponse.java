package org.grupo11.Api;

import java.util.HashMap;

public class ApiResponse {
    public int status;
    // if not provide, it will default to the message status
    public String message;
    public Object data;

    public ApiResponse(int status) {
        this.status = status;
        this.message = HttpStatus.fromCode(status).getMessage();
        this.data = new HashMap<>();
    }

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = new HashMap<>();
    }

    public ApiResponse(int status, Object data) {
        this.status = status;
        this.message = HttpStatus.fromCode(status).getMessage();
        this.data = data;
    }

    public ApiResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
