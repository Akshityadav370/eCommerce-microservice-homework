package com.order_service.order_service.controller;

import com.order_service.order_service.clients.ShipmentFeignClient;
import com.order_service.order_service.dto.OrderRequestDto;
import com.order_service.order_service.service.OrdersService;
import com.shipping_service.shipping_service.dto.ShipmentDto;
import com.shipping_service.shipping_service.entity.ShipmentStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrdersController {

    @Value("${my.variable}")
    private String myVariable;

    private final OrdersService orderService;
    private final ShipmentFeignClient shipmentFeignClient;

    @GetMapping("/helloOrders")
    public String helloOrders() {

        System.out.println(shipmentFeignClient.helloShipment());
        return "Hello from Orders Service, userId is: "+myVariable;
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderRequestDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderRequestDto orderRequestDto1 = orderService.createOrder(orderRequestDto);
        ShipmentDto sample = ShipmentDto.builder()
                .orderId(orderRequestDto1.getId())
                .address("address")
                .city("city")
                .state("state")
                .zipCode("500017")
                .shipmentStatus(ShipmentStatus.CREATED)
                .build();
        ShipmentDto res = shipmentFeignClient.createShipment(sample);
        System.out.println(res);
        return ResponseEntity.ok(orderRequestDto1);
    }

    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrders(HttpServletRequest httpServletRequest) {
        log.info("Fetching all orders via controller");
        List<OrderRequestDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);  // Returns 200 OK with the list of orders
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long id) {
        log.info("Fetching order with ID: {} via controller", id);
        OrderRequestDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);  // Returns 200 OK with the order
    }

    @PutMapping("/cancel-order/{id}")
    public ResponseEntity<OrderRequestDto> cancelOrderById(@PathVariable Long id) {
        log.info("Cancelling order with ID: {} via controller", id);
        OrderRequestDto order = orderService.getOrderById(id);
        OrderRequestDto cancelledOrder = orderService.cancelOrder(order);

        return ResponseEntity.ok(cancelledOrder);
    }
}
