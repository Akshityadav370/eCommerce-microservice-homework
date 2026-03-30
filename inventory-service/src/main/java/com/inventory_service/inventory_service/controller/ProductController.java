package com.inventory_service.inventory_service.controller;

import com.inventory_service.inventory_service.clients.OrdersFeignClient;
import com.inventory_service.inventory_service.dto.OrderRequestDto;
import com.inventory_service.inventory_service.dto.ProductDto;
import com.inventory_service.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.ServiceInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;

    private final OrdersFeignClient ordersFeignClient;

    @GetMapping("/fetchOrders")
    public String fetchFromOrdersService() {
        ServiceInstance orderService = discoveryClient.getInstances("order-service").getFirst();

//        return restClient.get()
//                .uri(orderService.getUri()+"/api/v1/orders/helloOrders")
//                .retrieve()
//                .body(String.class);
        return ordersFeignClient.helloOrders();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllInventory() {
        List<ProductDto> inventories = productService.getAllInventory();
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getInventoryById(@PathVariable Long id) {
        ProductDto inventory = productService.getProductById(id);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/reduce-stocks")
    public ResponseEntity<Double> reduceStocks(@RequestBody OrderRequestDto orderRequestDto) {
        System.out.println("Hi reduce stocks");
        Double totalPrice = productService.reduceStocks(orderRequestDto);
        return ResponseEntity.ok(totalPrice);
    }

    @PutMapping("/restock")
    public ResponseEntity<String> restock(@RequestBody OrderRequestDto orderRequestDto) {
        productService.restock(orderRequestDto);
        return ResponseEntity.ok("Stock successfully updated");
    }

    // write the end point, restock("/restock")
    // dto will be the same as OrderRequestDto
    // ex: {
    //  "items": [
    //    {
    //      "productId": 1,
    //      "quantity": 1
    //    },
    //    {
    //      "productId": 3,
    //      "quantity": 2
    //    }
    //  ]
    //}
}
