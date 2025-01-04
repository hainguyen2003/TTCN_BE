package com.example.ttcn2etest.service.order;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.OrderDto;
import com.example.ttcn2etest.model.etity.Order;
import com.example.ttcn2etest.repository.order.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<OrderDto> getAllOrder() {
        return orderRepository.findAll().stream().map(
                order -> modelMapper.map(order, OrderDto.class)
        ).toList();
    }

    @Override
    public OrderDto getOrderById(long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()){
            return modelMapper.map(order.get(), OrderDto.class);
        }else {
            throw new MyCustomException("ID của dịch vụ không tồn tại trong hệ thống!");
        }
    }

    @Override
    public OrderDto addOrder(OrderDto orderDto) {
        try{
            Order order = Order.builder()
                    .id(orderDto.getId())
                    .user(orderDto.getUser())
                    .amount(orderDto.getAmount())
                    .serviceManager(orderDto.getServiceManager())
                    .paymentDate(Instant.now())
                    .status(orderDto.getStatus())
                    .paymentMethod(orderDto.getPaymentMethod())
                    .updatedAt(orderDto.getUpdatedAt())
                    .build();

            order = orderRepository.saveAndFlush(order);
            return  modelMapper.map(order,OrderDto.class);
        }catch (Exception ex){
            throw new MyCustomException("Có lỗi xảy ra trong quá trình thanh toán ");
        }
    }
}
