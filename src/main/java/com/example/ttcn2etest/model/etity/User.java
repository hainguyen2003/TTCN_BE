package com.example.ttcn2etest.model.etity;

import com.example.ttcn2etest.constant.RoleEnum;
import com.example.ttcn2etest.importFileExcel.ExcelData;
import com.example.ttcn2etest.validator.UserValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;
    @Size(max = 100)
    private String name;
    @Size(max = 100)
    private String password;
    private String phone;
    private String email;
    private boolean isVerified; // Trạng thái xác thực

    //    @Column(name = "password_no_encode", length = 100)
//    private String passwordNoEncode;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    private String address;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;
    @Column(name = "is_super_admin")
    private Boolean isSuperAdmin = false;
    @Size(max = 2000)
    private String avatar;

    @ManyToMany
    @JoinTable(name = "user_service", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"))
    private Collection<Service> services;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (role != null) {
            role.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermissionId()))); // lay den quyen
            authorities.add(new SimpleGrantedAuthority(role.getRoleId())); //lay den vai tro
        }
//        if (isSuperAdmin) {
//            authorities.add(new SimpleGrantedAuthority(RoleEnum.ADMIN.name()));
//        }
//        if (!isSuperAdmin && authorities.isEmpty()) {
//            authorities.add(new SimpleGrantedAuthority(RoleEnum.CUSTOMER.name()));
//        }
        return authorities;
    }

    public List<ExcelData.ErrorDetail> validateInformationDetailError(List<ExcelData.ErrorDetail> errorDetailList){
        if(!UserValidator.validateName(name)){
            errorDetailList.add(ExcelData.ErrorDetail.builder().columnIndex(0).errMsg("Họ và tên không hợp lệ!").build());
        }
        if(!UserValidator.validatePhone(phone)){
            errorDetailList.add(ExcelData.ErrorDetail.builder().columnIndex(1).errMsg("Số điện thoại không hợp lêk!").build());
        }
        if(!UserValidator.isValidEmail(email)){
            errorDetailList.add(ExcelData.ErrorDetail.builder().columnIndex(2).errMsg("Email không hợp lệ!").build());
        }
        if(!UserValidator.validateBod(dateOfBirth)){
            errorDetailList.add(ExcelData.ErrorDetail.builder().columnIndex(3).errMsg("Ngày sinh không hợp lệ!").build());
        }
        if(!UserValidator.validateAddress(address)){
            errorDetailList.add(ExcelData.ErrorDetail.builder().columnIndex(4).errMsg("Địa chỉ không hợp lệ!").build());
        }
        if(!UserValidator.isVerifiedValid(isVerified)){
            errorDetailList.add(ExcelData.ErrorDetail.builder().columnIndex(5).errMsg("Xác thực đăng nhập không hợp lệ!").build());
        }
        if(!UserValidator.validateUsername(username)){
            errorDetailList.add(ExcelData.ErrorDetail.builder().columnIndex(6).errMsg("Tên đăng nhập không hợp lệ!").build());
        }
        return errorDetailList;
    }

}
