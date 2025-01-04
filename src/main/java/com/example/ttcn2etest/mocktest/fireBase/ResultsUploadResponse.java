package com.example.ttcn2etest.mocktest.fireBase;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultsUploadResponse {
    private String downloadUrl;
}
