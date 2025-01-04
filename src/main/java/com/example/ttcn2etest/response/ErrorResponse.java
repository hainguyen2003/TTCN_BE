package com.example.ttcn2etest.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private int statusCode;
    private String message;
    private List<ErrorDetail> errorDetailList;

    @JsonIgnore
    public String getErrorDetailMsg() {
        if (errorDetailList != null && !errorDetailList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ErrorDetail errorDetail : errorDetailList) {
                stringBuilder.append(errorDetail.getMessage() + ", ");
            }
            return stringBuilder.toString();
        }

        return "";
    }
}
