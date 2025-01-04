package com.example.ttcn2etest.request.auth;

import com.example.ttcn2etest.validator.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Họ và tên không được để trống!")
    @Size(min = 6, max = 100, message = "Họ và tên phải có ít nhất 6, nhiều nhất 100 kí tự!")
    @NameAnnotation
    private String name;
    @NotBlank(message = "Tên tài khoản không được để trống!")
    @Size(min = 6, max = 100, message = "Tên đăng ký phải có ít nhất 6, nhiều nhất 100 kí tự!")
    @UsernameAnnotation
    private String username;
    @NotBlank(message = "Mật khẩu không được để trống!")
    @Size(min = 6, max = 20, message = "Mật khẩu phải có ít nhất 6, nhiều nhất 20 kí tự!")
   // @PasswordAnnotation
    private String password;
    @NotBlank(message = "Số điện thoại không được để trống!")
    //@PhoneNumber
    private String phone;
    @NotBlank(message = "Email không được để trống!")
    @EmailAnnotation
    private String email;
    private Boolean isSuperAdmin = false;
    private String link;
}
