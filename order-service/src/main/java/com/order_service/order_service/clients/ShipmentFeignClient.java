package com.order_service.order_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.shipping_service.shipping_service.dto.ShipmentDto;
import com.shipping_service.shipping_service.entity.ShipmentStatus;

@FeignClient(name = "shipping-service", path = "/api/v1/shipping")
public interface ShipmentFeignClient {
    @GetMapping
    String helloShipment();

    @PostMapping("/create")
    ShipmentDto createShipment(@RequestBody ShipmentDto shipmentDto);

    @GetMapping("/order/{orderId}")
    ShipmentDto getShipmentByOrderId(@PathVariable("orderId") Long orderId);

    @PutMapping("/update-status/{shipmentId}")
    ShipmentDto updateShipmentStatus(
            @PathVariable("shipmentId") Long shipmentId,
            @RequestParam("status") ShipmentStatus status);

    @PutMapping("/cancel/{orderId}")
    ShipmentDto cancelShipment(@PathVariable("orderId") Long orderId);
}
