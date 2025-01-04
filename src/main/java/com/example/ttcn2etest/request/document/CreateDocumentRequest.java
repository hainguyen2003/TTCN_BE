package com.example.ttcn2etest.request.document;

import com.example.ttcn2etest.model.etity.Document;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateDocumentRequest {
    @NotBlank(message = "Tên tài liệu không được để trống!")
    @Size(min = 6, max = 100, message = "Tên tài liệu phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String name;
    @NotBlank(message = "Nội dung không được để trống!")
    @Size(max = 5000)
    private String content;
    @NotBlank(message = "File không được để trống!")
    @Size(max = 2000)
    private String file;
    @NotBlank(message = "Ảnh không được để trống!")
    @Size(max = 2000, message = "Link ảnh có độ dài từ 0-2000 ký tự!")
    private String image;
    @NotNull(message = "Trạng thái không được để trống!")
    @Enumerated(EnumType.STRING)
    private Document.Status status;
}
