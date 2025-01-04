package com.example.ttcn2etest.service.order;

import com.example.ttcn2etest.model.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrder();

    OrderDto getOrderById(long orderId);

    OrderDto addOrder(OrderDto orderDto);

}
