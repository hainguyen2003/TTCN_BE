package com.example.ttcn2etest.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
public class UploadAvatarRequest {
    @NotBlank(message = "Ảnh không để trống!")
    @Size(max = 2000, message = "Link ảnh có độ dài từ 0-2000 ký tự!")
    private String avatar;
}
