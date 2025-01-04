package com.example.ttcn2etest.response;

import lombok.Data;

import java.util.List;

@Data
public class DashBoardResponse {
    private int countService;
    private int countUser;
    private int countNew;
    private int countOrder;
    private int totalCost;
    private List<MonthlySaleResponse> listSale;
}