package com.example.bookstore.response;

public class BaseResponse {


    private int status;
    private String message;
    private Object data;


    public BaseResponse() {

    }

    public BaseResponse(int value, String message, Object o) {
        this.status = value;
        this.message = message;
        this.data = o;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
