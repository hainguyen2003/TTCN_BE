package com.example.ttcn2etest.request.user;

import com.example.ttcn2etest.validator.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateUserRequest {
    @NotBlank(message = "User name nguời dùng không được để trống!")
    @Size(min = 6, max = 100, message = "User name người dùng phải có ít nhất 6, nhiều nhất 100 kí tự!")
    @UsernameAnnotation
    private String username;
    @NotBlank(message = "Tên nguời dùng không được để trống!")
    @Size(min = 6, max = 100, message = "Tên người dùng phải có ít nhất 6, nhiều nhất 100 kí tự!")
    @NameAnnotation
    private String name;
    @NotBlank(message = "Mật khẩu không được để trống!")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự!")
    @PasswordAnnotation
    private String password;
    @NotBlank(message = "Ngày sinh không được để trống!")
    @DateValidateAnnotation(message = "Định dạng ngày tháng phải là dd/mm/yyyy")
    private String dateOfBirth;
    @NotBlank(message = "Số điện thoại không được để trống!")
    @PhoneNumber
    private String phone;

    //    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email không hợp lệ!")
    @NotBlank(message = "Email không được để trống!")
    @EmailAnnotation
    private String email;
    @NotBlank(message = "Địa chỉ không được để trống!")
    private String address;
    //    @NotNull(message = "Không được để trống Chỉ định người dùng có là Admin không? Nhập tùy chọn(1: có, 0: không)")
    private Boolean isSuperAdmin;
    //    @NotBlank(message = "Avatar không được để trống!")
    @Size(max = 2000, message = "Link ảnh có độ dài từ 0-2000 ký tự!")
    private String avatar;
    @NotBlank(message = "Vai trò không được để trống!")
    private String roleId;
    //    @NotEmpty(message = "Dịch vụ không để trống!")
    private List<Long> services; //"":[1,2,3]
    private boolean isVerified;

}
