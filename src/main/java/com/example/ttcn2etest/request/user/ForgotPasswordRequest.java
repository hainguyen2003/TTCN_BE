package com.example.ttcn2etest.request.user;

import com.example.ttcn2etest.validator.EmailAnnotation;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @EmailAnnotation
    private String email;
}
