package com.example.ttcn2etest.vnpay.controller;

import com.example.ttcn2etest.model.dto.OrderDto;
import com.example.ttcn2etest.service.order.OrderService;
import com.example.ttcn2etest.vnpay.dto.PaymentDTO;
import com.example.ttcn2etest.vnpay.response.ResponseObject;
import com.example.ttcn2etest.vnpay.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/paymentVNPAY")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/vn-pay")
    public ResponseEntity<?> pay(HttpServletRequest request, @RequestBody OrderDto orderDto) {
        try {
            // Tạo thông tin thanh toán VNPay
            PaymentDTO.VNPayResponse vnPayResponse = paymentService.createVnPayPayment(request, orderDto);
            return ResponseEntity.ok(new ResponseObject<>(HttpStatus.OK, "Success", vnPayResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }



    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String status = request.getParameter("vnp_ResponseCode");
        String orderId = request.getParameter("vnp_TxnRef");
        String frontendUrl; // URL frontend để điều hướng
        if ("00".equals(status)) { // Thanh toán thành công
            orderService.updateOrderStatus(Long.parseLong(orderId), "PAID", null, null);
            frontendUrl = "http://localhost:3000/payment-success?orderId=" + orderId; // URL frontend khi thành công
        } else { // Thanh toán thất bại
            orderService.updateOrderStatus(Long.parseLong(orderId), "FAILED", null, null);
            frontendUrl = "http://localhost:3000/payment-failed?orderId=" + orderId; // URL frontend khi thất bại
        }

        // Điều hướng đến frontend
        response.sendRedirect(frontendUrl);
    }


}
