package com.example.ttcn2etest.fireBase;

import com.example.ttcn2etest.controller.BaseController;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FireBaseStorageController extends BaseController {
    private final FirebaseStorageService storageService;

    public FireBaseStorageController(FirebaseStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@Valid @RequestPart("file") MultipartFile file) {
        try {
            String downloadUrl = storageService.uploadFile(file);
            FirebaseUploadResponse response = new FirebaseUploadResponse(downloadUrl);
            return buildItemResponse(response);
        } catch (IOException e) {
            return buildResponse();
        }
    }

}
