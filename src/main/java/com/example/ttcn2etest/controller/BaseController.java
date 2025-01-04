package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.constant.ErrorCodeDefs;
import com.example.ttcn2etest.response.BaseItemResponse;
import com.example.ttcn2etest.response.BaseListItemResponse;
import com.example.ttcn2etest.response.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BaseController {
    protected <T> ResponseEntity<?> buildItemResponse(T data) {
        BaseItemResponse<T> response = new BaseItemResponse<>();
        response.setData(data);
        response.setSuccess(true);
        response.setStatusCode(200);
        return ResponseEntity.ok(response);
    }

    protected <T> ResponseEntity<?> buildListItemResponse(List<T> data, long total) {
        BaseListItemResponse<T> response = new BaseListItemResponse<>();
        response.setResult(data, total);
        response.setSuccess(true);
        response.setStatusCode(200);
        return ResponseEntity.ok(response);
    }

    protected ResponseEntity<?> buildResponse() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSuccess(false);
        baseResponse.setFailed(ErrorCodeDefs.SERVER_ERROR, ErrorCodeDefs.getErrMsg(ErrorCodeDefs.SERVER_ERROR));
        baseResponse.setStatusCode(500);
        return ResponseEntity.ok(baseResponse);
    }
    // Trong lớp BaseController
    protected ResponseEntity<?> buildErrorResponse(String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSuccess(false);
        baseResponse.setFailed(ErrorCodeDefs.SERVER_ERROR, message);  // Bạn có thể thay ErrorCodeDefs.SERVER_ERROR bằng mã lỗi khác nếu cần
        baseResponse.setStatusCode(500);
        return ResponseEntity.ok(baseResponse);
    }

}
