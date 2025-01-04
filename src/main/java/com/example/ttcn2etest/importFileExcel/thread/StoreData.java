package com.example.ttcn2etest.importFileExcel.thread;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.constant.RoleEnum;
import com.example.ttcn2etest.importFileExcel.ExcelData;
import com.example.ttcn2etest.model.etity.Role;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.user.UserRepository;
import com.example.ttcn2etest.utils.MyUtils;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
@AllArgsConstructor
public class StoreData implements Callable<ExcelData> {
    private final UserRepository userRepository;
    private final String userStr;
    private final int row;

//    public StoreData(String userStr, int row) {
//        this.userStr = userStr;
//        this.row = row;
//    }

    @Override
    public ExcelData call() throws Exception {
        ExcelData excelData = new ExcelData();
        if(!userStr.isEmpty()){
            String[] infoList = userStr.strip().split(",");
            String name = infoList[0].strip();
            String phone = infoList[1].strip();
            String email = infoList[2].strip();
            String dob = infoList[3].strip();
            String address = infoList[4].strip();
            String isVerified = infoList[5].strip();
            String username = infoList[6].strip();

            User user = User.builder()
                    .name(name)
                    .username(username)
                    .phone(phone)
                    .email(email)
                    .dateOfBirth(MyUtils.convertDateFromString(dob, DateTimeConstant.DATE_FORMAT))
                    .address(address)
                    .role(Role.builder().roleId(String.valueOf(RoleEnum.CUSTOMER)).build())
                    .isVerified(Boolean.parseBoolean(isVerified))
                    .build();

            List<ExcelData.ErrorDetail> errorDetailList = user.validateInformationDetailError(new CopyOnWriteArrayList<>());
            if (userRepository.existsAllByEmail(email)) {
                errorDetailList.add(ExcelData.ErrorDetail.builder().columnIndex(2).errMsg("Email đã tồn tại trong hệ thống!").build());
            }

            if (userRepository.existsByUsername(username)) {
                errorDetailList.add(ExcelData.ErrorDetail.builder().columnIndex(6).errMsg("Username đã tồn tại trong hệ thống!").build());
            }

            excelData.setUser(user);
            if(!errorDetailList.isEmpty()){
                excelData.setErrorDetailList(errorDetailList);
                excelData.setErrorDetailList(errorDetailList);
                excelData.setValid(false);
            }
            excelData.setRowIndex(row);

        }
        return excelData;
    }
}
