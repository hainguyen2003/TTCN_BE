package com.example.ttcn2etest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse {
    private boolean success = false;
    private ErrorResponse error;
    private int statusCode;
    private Date timestamp = new Date();

    public void setSuccess() {
        this.success = true;
        this.statusCode = 200;
    }

    public static <T> ResponseEntity<?> success(T data){
        BaseItemResponse<T> response=new BaseItemResponse<>();
        response.setSuccess(data);
        return ResponseEntity.ok(response);
    }


    public void setFailed(int code, String message) {
        this.success = false;
        error = new ErrorResponse();
        error.setMessage(message);
        error.setStatusCode(code);
    }

}
