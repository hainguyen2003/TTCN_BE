package com.example.ttcn2etest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    Long id;
    String orderId;
    Long userId;
    Long serviceManagerId;
    String amount;
    String paymentMethod;
    String status;
    Instant paymentDate;
    Instant updatedAt;
    String  image;
    String email;
    String fullName;
    String phone;
    String address;
}