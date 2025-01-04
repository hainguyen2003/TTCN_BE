package com.example.ttcn2etest.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlySaleResponse {
    private int totalCost;
    private String month;
}