package com.example.ttcn2etest.mocktest.fireBase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
@Service
@Component
public class UploadFileService {

    public String uploadFile(MultipartFile file) throws IOException {


        InputStream serviceAccount = getClass().getResourceAsStream("/results-mocktest-user-firebase-adminsdk-ukjc2-53821df3ad.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        StorageOptions storageOptions = StorageOptions.newBuilder().setCredentials(credentials).build();
        Storage storage = storageOptions.getService();

        String originalFilename = file.getOriginalFilename();

        String filename = originalFilename;

        Blob blob = storage.create(BlobInfo.newBuilder("results-mocktest-user.appspot.com", filename)
                .setContentType(file.getContentType())
                .build(), file.getInputStream());

        // Ví dụ: Thời gian hết hạn là 100 năm
//        long expirationTime = EXPIRATION_TIME;
        String downloadUrl = blob.signUrl(System.currentTimeMillis() + 1L * 365 * 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS).toString();

        return downloadUrl;
    }

}
