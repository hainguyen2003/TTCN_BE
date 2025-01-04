package com.example.ttcn2etest.mocktest.user_exam.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FilterUserResultsRequest {
//    @NotNull(message = "Id không được để trống ")
    private Long userId ;
    private String dateFrom ;
    private String dateTo ;
    private boolean highToLow  ;
    private String userResponseId ;
    private boolean isSortDateDESC;
    private String examId ;


}
