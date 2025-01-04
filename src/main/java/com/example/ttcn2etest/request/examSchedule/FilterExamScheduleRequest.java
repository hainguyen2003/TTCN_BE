package com.example.ttcn2etest.request.examSchedule;

import com.example.ttcn2etest.model.etity.ExamSchedule;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilterExamScheduleRequest {
    @NotNull(message = "Start không được để trống")
    private Integer start;
    @NotNull(message = "Limit không được để trống")
    private Integer limit;
    private ExamSchedule.Area areaId;
    private String schoolId;
    private String nameExamSchool;
    private String nameArea;
//    private Date registrationTerm;    //tim xem lich thi nao con thoi gian de dki hoc
}
