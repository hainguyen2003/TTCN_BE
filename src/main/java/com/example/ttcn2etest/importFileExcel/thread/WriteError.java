package com.example.ttcn2etest.importFileExcel.thread;

import com.example.ttcn2etest.importFileExcel.ExcelData;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.utils.MyUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import java.util.concurrent.Callable;
@Data
@AllArgsConstructor
public class WriteError implements Callable<Void> {
    private Row row;
    private User user;
    private CellStyle cellStyle;
    private ExcelData excelData;
    @Override
    public Void call() throws Exception {
        row.createCell(0).setCellValue(user.getName());
        row.createCell(1).setCellValue(user.getPhone());
        row.createCell(2).setCellValue(user.getEmail());
        row.createCell(3).setCellValue(MyUtils.convertDateToString(user.getDateOfBirth())); // sua
        row.createCell(4).setCellValue(user.getAddress());
        row.createCell(5).setCellValue(user.isVerified());
        row.createCell(6).setCellValue(user.getUsername());

        excelData.getErrorDetailList().forEach(errorDetail -> {
            Cell cell = row.getCell(errorDetail.getColumnIndex());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(errorDetail.getErrMsg());
        });
        return null;
    }
}
