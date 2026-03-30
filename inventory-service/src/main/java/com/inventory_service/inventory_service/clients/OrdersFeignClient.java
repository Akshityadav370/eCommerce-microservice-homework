package com.inventory_service.inventory_service.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "order-service", path = "/api/v1/orders")
public interface OrdersFeignClient {

    @GetMapping("/helloOrders")
    String helloOrders();

}
