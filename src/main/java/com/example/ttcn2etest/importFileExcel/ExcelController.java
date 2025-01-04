package com.example.ttcn2etest.importFileExcel;

import com.example.ttcn2etest.controller.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/excel")
public class ExcelController extends BaseController {
    private final FileManager fileManager;

    public ExcelController(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @PostMapping("/import")
    public ResponseEntity<?> importUserList(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        fileManager.importFromExcel(file);
        return buildItemResponse("Nhập dữ liệu thành công!");
    }
}
