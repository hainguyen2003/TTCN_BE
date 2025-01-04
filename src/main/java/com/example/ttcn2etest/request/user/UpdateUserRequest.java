package com.example.ttcn2etest.request.user;

import com.example.ttcn2etest.validator.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRequest {
    @NotBlank(message = "User name nguời dùng không được để trống!")
    @Size(min = 6, max = 100, message = "User name người dùng phải có ít nhất 6, nhiều nhất 100 kí tự!")
    @UsernameAnnotation
    private String username;
    @NotBlank(message = "Họ và tên nguời dùng không được để trống!")
    @Size(min = 6, max = 100, message = "Tên người dùng phải có ít nhất 6, nhiều nhất 100 kí tự!")
    @NameAnnotation
    private String name;
//    @NotBlank(message = "Mật khẩu không được để trống!")
//    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự!")
//    @PasswordAnnotation
//    private String password;
    @NotBlank(message = "Ngày sinh không được để trống!")
    @DateValidateAnnotation(message = "Định dạng ngày tháng phải là dd/mm/yyyy")
    private String dateOfBirth;
    @NotBlank(message = "Số điện thoại không được để trống!")
    @PhoneNumber
    private String phone;
    @NotBlank(message = "Email không được để trống!")
    @EmailAnnotation
    private String email;
    @NotBlank(message = "Địa chỉ không được để trống!")
    private String address;
    //    @NotNull(message = "Không được để trống Chỉ định người dùng có là Admin không? Nhập tùy chọn(1: có, 0: không)")
    private Boolean isSuperAdmin;
    @Size(max = 2000, message = "Link ảnh có độ dài từ 0-2000 ký tự!")
    private String avatar;
    private String roleId;
    private List<Long> services;
    private boolean isVerified;
}
