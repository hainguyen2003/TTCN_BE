package com.example.ttcn2etest.importFileExcel.thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
public class ReadExcel implements Callable<String> {
    private Row row;

    @Override
    public String call() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        //giới hạn xem muốn truyền bao nhiêu trường đọc vào từ excel
        for (int i = 0; i < 8; i++) {
            Cell cell = row.getCell(i);
            String data = "";
            if(cell != null){
                cell.setCellType(CellType.STRING);
                data = cell.getStringCellValue();
            }
            stringBuilder.append(data);
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }
}
