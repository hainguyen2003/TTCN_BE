package com.example.ttcn2etest.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResponse {
    String id;
    String objectName;
    String bucket;
    String urlImage;
}
