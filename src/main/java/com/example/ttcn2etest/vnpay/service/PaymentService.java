package com.example.ttcn2etest.vnpay.service;

import com.example.ttcn2etest.model.dto.OrderDto;
import com.example.ttcn2etest.service.order.OrderService;
import com.example.ttcn2etest.vnpay.config.VNPayConfig;
import com.example.ttcn2etest.vnpay.dto.PaymentDTO;
import com.example.ttcn2etest.vnpay.until.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private VNPayConfig vnPayConfig;

    @Autowired
    private OrderService orderService;

    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request, OrderDto orderDto) {
        try {
            // Lưu thông tin đơn hàng vào database
            OrderDto savedOrder = orderService.addOrder(orderDto, "VNPAY_PAYMENT", "Không có");

            // Tạo thông tin thanh toán VNPAY
            long amount = Long.parseLong(orderDto.getAmount()) * 100; // Số tiền nhân 100 theo chuẩn VNPAY
            String bankCode = request.getParameter("bankCode");

            // Lấy cấu hình VNPAY
            Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
            vnpParamsMap.put("vnp_TxnRef", String.valueOf(savedOrder.getId())); // ID đơn hàng trong hệ thống
            vnpParamsMap.put("vnp_Amount", String.valueOf(amount)); // Số tiền
            if (bankCode != null && !bankCode.isEmpty()) {
                vnpParamsMap.put("vnp_BankCode", bankCode); // Mã ngân hàng (nếu có)
            }
            vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request)); // Địa chỉ IP của người dùng

            // Xây dựng URL thanh toán
            String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
            String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
            String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
            queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

            // Tạo URL thanh toán hoàn chỉnh
            String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

            // Trả về thông tin phản hồi
            return PaymentDTO.VNPayResponse.builder()
                    .code("00")
                    .message("Payment URL created successfully")
                    .paymentUrl(paymentUrl)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error creating VNPay payment: " + e.getMessage());
        }
    }
}

