package com.example.ttcn2etest.importFileExcel;

import com.example.ttcn2etest.model.etity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ExcelServiceImpl {
    List<User> readUserFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
}
