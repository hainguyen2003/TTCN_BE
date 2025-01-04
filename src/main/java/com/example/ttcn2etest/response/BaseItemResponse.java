package com.example.ttcn2etest.response;

import lombok.Data;

import java.util.Date;

@Data

public class BaseItemResponse<T> extends BaseResponse {
    private T data;
    private int statusCode;
    private Date timestamp = new Date();

    public BaseItemResponse() {
    }

    public BaseItemResponse(T data) {
        this.data = data;
    }

    public void setSuccess(T data) {
        super.setSuccess();
        this.data = data;
        this.statusCode = 200;
    }
}
