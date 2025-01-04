package com.example.ttcn2etest.fireBase;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseUploadResponse {
    private String downloadUrl;
}
