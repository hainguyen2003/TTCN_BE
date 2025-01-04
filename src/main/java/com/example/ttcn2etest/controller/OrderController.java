package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.model.dto.OrderDto;
import com.example.ttcn2etest.service.order.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
//@CrossOrigin(origins = "http://localhost:3000")
public class OrderController extends BaseController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/all")
    ResponseEntity<?> getAllOrders() {
        try {
            List<OrderDto> response = orderService.getAllOrder();
            return buildListItemResponse(response, response.size());
        }catch (Exception ex){
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        OrderDto response = orderService.getOrderById(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    ResponseEntity<?> creatService(@Validated @RequestBody OrderDto orderDto) {
        OrderDto response = orderService.addOrder(orderDto);
        return buildItemResponse(response);
    }
}
