package com.example.ttcn2etest.request.auth;

import com.example.ttcn2etest.validator.UsernameAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống!")
    @Size(min = 6, max = 100, message = "Tên đăng nhập phải có ít nhất 6, nhiều nhất 100 kí tự!")
    @UsernameAnnotation
    private String username;
    @NotBlank(message = "Mật khẩu không được để trống!")
    @Size(min = 6, max = 20, message = "Mật khẩu phải có ít nhất 6, nhiều nhất 20 kí tự!")
    private String password;
}
