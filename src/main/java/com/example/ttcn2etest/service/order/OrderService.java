package com.example.ttcn2etest.service.order;

import com.example.ttcn2etest.model.dto.OrderDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrder();
    List<OrderDto> getPaidUsers();

    List<OrderDto> getPaidOrdersByUserId(long userId);

    // get order by id

    OrderDto getOrderById(Long id);
    OrderDto getOrderByOrderId(String id);


    // add Order
    OrderDto addOrder(OrderDto orderDto, String paymentCode, String imagePath);


    //
    String saveImage(MultipartFile file, String fileName) throws IOException;


    // update Order
    OrderDto updateOrderStatus(Long id, String status, Long amount, String paymentDate);
    void updateOrderStatus(Long orderId, String status) throws Exception;


}
