package com.example.bookstore.response;

import lombok.Data;

@Data
public class BaseResponse {
    private int status;
    private String message;
    private Object data;
}
