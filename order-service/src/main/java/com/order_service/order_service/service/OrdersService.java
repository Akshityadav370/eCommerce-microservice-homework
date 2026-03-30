package com.order_service.order_service.service;

import com.order_service.order_service.clients.InventoryOpenFeignClient;
import com.order_service.order_service.dto.OrderRequestDto;
import com.order_service.order_service.entity.OrderItem;
import com.order_service.order_service.entity.OrderStatus;
import com.order_service.order_service.entity.Orders;
import com.order_service.order_service.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository orderRepository;
    private final ModelMapper modelMapper;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;

    public List<OrderRequestDto> getAllOrders() {
        log.info("Fetching all orders");
        List<Orders> orders = orderRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Orders order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return modelMapper.map(order, OrderRequestDto.class);
    }

    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        Double totalPrice = inventoryOpenFeignClient.reduceStocks(orderRequestDto);

        Orders orders = modelMapper.map(orderRequestDto, Orders.class);
        for(OrderItem orderItem: orders.getItems()) {
            orderItem.setOrder(orders);
        }
        orders.setTotalPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.CONFIRMED);

        Orders savedOrder = orderRepository.save(orders);

        return modelMapper.map(savedOrder, OrderRequestDto.class);
    }

    @Transactional
    public OrderRequestDto cancelOrder(OrderRequestDto orderRequestDto) {

        // Step 1: Fetch order from DB
        Orders order = orderRepository.findById(orderRequestDto.getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Step 2: Call inventory service to restock
        inventoryOpenFeignClient.restock(orderRequestDto);

        // Step 3: Update order status
        order.setOrderStatus(OrderStatus.CANCELLED);

        Orders updatedOrder = orderRepository.save(order);

        return modelMapper.map(updatedOrder, OrderRequestDto.class);
    }
}
