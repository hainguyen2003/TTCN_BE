package com.example.ttcn2etest.service.order;

import com.example.ttcn2etest.controller.OrderController;
import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.OrderDto;
import com.example.ttcn2etest.model.etity.Order;
import com.example.ttcn2etest.repository.order.OrderRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final String uploadDir = "uploads/";

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Override
    public List<OrderDto> getAllOrder() {
        return orderRepository.findAll().stream().map(
                order -> modelMapper.map(order, OrderDto.class)
        ).toList();
    }

    @Override
    public List<OrderDto> getPaidUsers() {
        List<Order> paidOrders = orderRepository.findByStatus("PAID");
        return paidOrders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getPaidOrdersByUserId(long userId) {
        return orderRepository.findPaidOrdersByUserId(userId).stream()
                .map(order -> {
                    // Sử dụng getter để chắc chắn các đối tượng liên kết được tải
                    order.getUserId();
                    order.getServiceManagerId();
                    OrderDto orderDto = modelMapper.map(order, OrderDto.class);
                    return orderDto;
                }).toList();
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()) {
            return modelMapper.map(order.get(), OrderDto.class);
        }else {
            throw new MyCustomException("ID của dịch vụ không tồn tại trong hệ thống!");
        }
    }

    @Override
    public OrderDto getOrderByOrderId(String id) {
        Optional<Order> order = orderRepository.findOrderByOrderId(id);
        if(order.isPresent()) {
            return modelMapper.map(order.get(), OrderDto.class);
        }else {
            throw new MyCustomException("ID của dịch vụ không tồn tại trong hệ thống!");
        }
    }


    @Override
    public OrderDto addOrder(OrderDto orderDto, String paymentCode, String imagePath) {
        String paymentMethod;
        String finalImagePath;

        // Xác định phương thức thanh toán
        if ("DIRECT_PAYMENT".equals(paymentCode)) {
            paymentMethod = "DIRECT_PAYMENT";
            finalImagePath = "Không có"; // Nếu là thanh toán trực tiếp, không có hình ảnh
        } else if ("ONLINE_PAYMENT".equals(paymentCode)) {
            paymentMethod = "ONLINE_PAYMENT";
            finalImagePath = (imagePath != null) ? imagePath : "Không có hình ảnh"; // Nếu không có ảnh, đặt giá trị mặc định
        }else if ("VNPAY_PAYMENT".equals(paymentCode)) {
            paymentMethod = "VNPAY_PAYMENT";
            finalImagePath = "Không có"; // Nếu là thanh toán trực tiếp, không có hình ảnh
        } else {
            throw new IllegalArgumentException("Invalid payment method code: " + paymentCode);
        }

        // Tạo đối tượng Order từ DTO
        Order order = Order.builder()
                .userId(orderDto.getUserId())
                .amount(orderDto.getAmount())
                .orderId(orderDto.getOrderId())
                .serviceManagerId(orderDto.getServiceManagerId())
                .paymentMethod(paymentMethod)
                .status("PENDING")
                .paymentDate(Instant.now())
                .image(finalImagePath) // Đường dẫn hình ảnh
                .email(orderDto.getEmail())
                .fullName(orderDto.getFullName())
                .phone(orderDto.getPhone())
                .address(orderDto.getAddress())
                .build();

        // Lưu vào cơ sở dữ liệu
        order = orderRepository.save(order);

        // Chuyển đổi sang DTO để trả về
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public String saveImage(MultipartFile file, String fileName) throws IOException {
        // Kiểm tra MIME type của file
        String mimeType = Files.probeContentType(Paths.get(file.getOriginalFilename()));
        if (!mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("Tệp tải lên không phải là hình ảnh hợp lệ.");
        }

        String uploadDir = "src/main/resources/static";
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/" + fileName; // Trả về đường dẫn tương đối để lưu vào cơ sở dữ liệu
    }

    @Override
    public OrderDto updateOrderStatus(Long id, String status, Long amount, String paymentDate) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status); // Cập nhật trạng thái
            order.setPaymentDate(Instant.now()); // Thời gian thanh toán
            orderRepository.save(order); // Lưu lại thay đổi
            return modelMapper.map(order, OrderDto.class);
        } else {
            throw new MyCustomException("Không tìm thấy đơn hàng với ID: " + id);
        }
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Không tìm thấy đơn hàng với ID: " + orderId));

        if (!status.equals("PAID") && !status.equals("FAILED")) {
            throw new Exception("Trạng thái không hợp lệ! Chỉ chấp nhận: PAID, FAILED");
        }

        if (!order.getStatus().equals("PENDING")) {
            throw new Exception("Chỉ có thể cập nhật trạng thái cho đơn hàng đang chờ xử lý (PENDING)");
        }

        order.setStatus(status);
        orderRepository.save(order);
    }
}
