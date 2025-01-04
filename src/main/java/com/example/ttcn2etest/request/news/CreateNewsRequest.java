package com.example.ttcn2etest.request.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateNewsRequest {
    @NotBlank(message = "Tên tin tức không được để trống!")
    @Size(min = 6, max = 100, message = "Tên tin tức phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String name;
    @Size(max = 5000)
    private String description;
    @NotBlank(message = "Nội dung không được để trống!")
    private String content;
    @NotBlank(message = "Ảnh không được để trống!")
    @Size(max = 2000, message = "Link ảnh có độ dài từ 0-2000 ký tự!")
    private String image;
}
