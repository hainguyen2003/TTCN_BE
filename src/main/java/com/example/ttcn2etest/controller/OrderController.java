package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.model.dto.OrderDto;
import com.example.ttcn2etest.service.order.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "http://localhost:3000")

public class OrderController extends BaseController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


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
    @GetMapping("/orderId/{id}")
    ResponseEntity<?> getByOrderId(@PathVariable String id) {
        OrderDto response = orderService.getOrderByOrderId(id);
        return buildItemResponse(response);
    }

    @GetMapping("/paid/{userId}")
    public ResponseEntity<List<OrderDto>> getPaidOrdersByUserId(@PathVariable Long userId) {
        List<OrderDto> orders = orderService.getPaidOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/paid")
    public ResponseEntity<List<OrderDto>> getPaidUsers() {
        List<OrderDto> paidUsers = orderService.getPaidUsers();
        return ResponseEntity.ok(paidUsers);
    }

    @PostMapping("/direct-payment")
    public ResponseEntity<?> directPayment(@Validated @RequestBody OrderDto orderDto) {
        try {
            // Đường dẫn hình ảnh mặc định cho DIRECT_PAYMENT
            String defaultImagePath = "Không có";
            System.out.println(orderDto);

            // Lưu thông tin đơn hàng vào database với paymentCode = "1" (DIRECT_PAYMENT)
            OrderDto response = orderService.addOrder(orderDto, "DIRECT_PAYMENT", defaultImagePath);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", ex.getMessage(),
                    "statusCode", HttpStatus.BAD_REQUEST.value(),
                    "timestamp", Instant.now()
            ));
        }
    }
    @PostMapping("/{orderId}/update-status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        try {
            orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok("Cập nhật trạng thái đơn hàng thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
        }
    }
}
