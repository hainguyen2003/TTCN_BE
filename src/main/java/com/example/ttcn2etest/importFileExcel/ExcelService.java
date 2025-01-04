package com.example.ttcn2etest.importFileExcel;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.fireBase.FirebaseStorageService;
import com.example.ttcn2etest.importFileExcel.thread.ReadExcel;
import com.example.ttcn2etest.importFileExcel.thread.StoreData;
import com.example.ttcn2etest.importFileExcel.thread.WriteError;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelService implements ExcelServiceImpl {
    private final UserRepository userRepository;
    public static final int MAX_THREADS = 3;
    //cach file doc khi ko build docker
    //    public static final String ERROR_USER_FILE = "src/main/java/com/example/ttcn2etest/upload/loi_nhap_du_lieu.xlsx";

    //file doc de ngoài src
    public static final String ERROR_USER_FILE = "loi_nhap_du_lieu.xlsx";

    //cach cau hinh trong porperties dung khi call localA
//    @Value("${error.file.path}")
//    public  String ERROR_USER_FILE;
    @Value("${firebase.storage.bucket}")
    private String bucketName;

    private final FirebaseStorageService firebaseStorageService;

    @Override
    public List<User> readUserFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<String> userStrList = readUserList(file);
        List<ExcelData> excelDataList = storeData(userStrList);
        if (!isContinue(excelDataList)) {
            throw new MyCustomException(exportErrorList(excelDataList));
        }
        List<User> users = new ArrayList<>();
        excelDataList.forEach(excelData -> users.add(excelData.getUser()));
        return users;
    }

    private List<String> readUserList(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        List<String> stringList = new CopyOnWriteArrayList<>();

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        int totalRows = sheet.getPhysicalNumberOfRows();

        for (int i = 0; i < totalRows; i++) {
            Row row = sheet.getRow(i);
            if (i != 0) {
                Callable<String> callable = new ReadExcel(row);
                Future<String> future = executor.submit(callable);
                stringList.add(future.get());
            } else if (row == null) {
                throw new MyCustomException("Không tìm thấy dữ liệu!");
            }
        }
        workbook.close();
        return stringList;
    }

    private List<ExcelData> storeData(List<String> userStrList) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        List<ExcelData> excelDataList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < userStrList.size(); i++) {
            String userStr = userStrList.get(i);
            Callable<ExcelData> callable = new StoreData(userRepository, userStr, i);
            Future<ExcelData> future = executor.submit(callable);
            excelDataList.add(future.get());
        }
        return excelDataList;
    }

    private boolean isContinue(List<ExcelData> excelDataList) {
        for (ExcelData excelData : excelDataList) {
            if (!excelData.isValid()) {
                return false;
            }
        }
        return true;
    }

    private String exportErrorList(List<ExcelData> excelDataList) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("ErrorUser");

            // Tạo hàng tiêu đề
            createUserErrorHeader(sheet);

            // Tạo style cho error cell
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (ExcelData excelData : excelDataList) {
                Row row = sheet.createRow(excelData.getRowIndex() + 1);
                User user = excelData.getUser();
                Callable<Void> callable = new WriteError(row, user, cellStyle, excelData);
                Future<Void> future = executor.submit(callable);
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(ERROR_USER_FILE);
            workbook.write(fileOutputStream);

            workbook.close();
            fileOutputStream.close();

            return firebaseStorageService.uploadFileExcel(ERROR_USER_FILE, bucketName);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Lỗi dữ liệu truyền vào : {}", e.getMessage());
            throw new MyCustomException("Dữ liệu truyền vào không đúng, kiểm tra lại các trường theo template import user đã cung cấp!");
        } catch (IOException e) {
            log.error("Lỗi đường dẫn truyền file : {}", e.getMessage());
            throw new MyCustomException("Có lỗi xảy ra trong quá trình nhập/ghi file!");
        } catch (Exception e) {
            log.error("Lỗi: {}", e.getMessage());

            throw new MyCustomException("Có lỗi xảy ra trong quá trình nhập dữ liệu khách hàng!");
        }
    }

    private void createUserErrorHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Họ và tên");
        headerRow.createCell(1).setCellValue("Số điện thoại");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Ngày sinh");
        headerRow.createCell(4).setCellValue("Địa chỉ");
        headerRow.createCell(5).setCellValue("Xác thực đăng ký");
        headerRow.createCell(6).setCellValue("Tên đăng nhập");
    }
}
