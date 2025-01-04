package com.example.ttcn2etest.request.displayManager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateDisplayRequest {
    @NotBlank(message = "Tên mục hiện thị không được để trống!")
    @Size(min = 6, max = 100, message = "Tên mục hiện thị phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String title;
    @NotBlank(message = "Mô tả không được để trống!")
    @Size(max = 500)
    private String description;
    @NotBlank(message = "Ảnh không được để trống!")
    @Size(max = 2000, message = "Link ảnh có độ dài từ 0-2000 ký tự!")
    private String image;
    @NotBlank(message = "Vị trí không được để trống!")
    @Size(max = 100)
    private String location;
    @NotBlank(message = "không được để trống!")
    private String type;
}
