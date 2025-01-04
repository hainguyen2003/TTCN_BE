package com.example.ttcn2etest.importFileExcel;

import com.example.ttcn2etest.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class FileManager {
    private final UserRepository userRepository;
    private final ExcelService excelService;

    public FileManager(UserRepository userRepository, ExcelService excelService) {
        this.userRepository = userRepository;
        this.excelService = excelService;
    }

    public void importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        userRepository.saveAllAndFlush(excelService.readUserFromExcel(file));
    }
}
