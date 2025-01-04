package com.example.ttcn2etest.model.dto;

import com.example.ttcn2etest.model.etity.Order;
import com.example.ttcn2etest.model.etity.Service;
import com.example.ttcn2etest.model.etity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link com.example.ttcn2etest.model.etity.Order}
 */
@Value
public class OrderDto extends @NotNull Order implements Serializable {
    Long id;
    @NotNull
    User user;
    @NotNull
    Service serviceManager;
    @NotNull
    BigDecimal amount;
    @NotNull
    @Size(max = 50)
    String paymentMethod;
    String status;
    Instant paymentDate;
    Instant updatedAt;
}