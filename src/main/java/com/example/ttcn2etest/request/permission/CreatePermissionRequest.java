package com.example.ttcn2etest.request.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePermissionRequest {
    @NotBlank(message = "Id quyền không được để trống!")
    private String permissionId;
    @NotBlank(message = "Tên quyền không được để trống!")
    @Size(min = 6, max = 100, message = "Tên quyền phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String name;
    @NotBlank(message = "Mô tả quyền không được để trống!")
    private String description;

}
