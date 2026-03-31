package com.shipping_service.shipping_service.controller;

import com.shipping_service.shipping_service.dto.ShipmentDto;
import com.shipping_service.shipping_service.entity.ShipmentStatus;
import com.shipping_service.shipping_service.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping
    public String helloShipment() {
        System.out.println("Hello shipment");
        return "Hello Shipment";
    }

    @PostMapping("/create")
    public ResponseEntity<ShipmentDto> createShipment(@RequestBody ShipmentDto shipmentDto) {
        System.out.println("Create Shipping");
        return ResponseEntity.ok(shipmentService.createShipment(shipmentDto));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShipmentDto> getShipment(@PathVariable Long orderId) {
        return ResponseEntity.ok(shipmentService.getShipmentByOrderId(orderId));
    }

    @PutMapping("/update-status/{shipmentId}")
    public ResponseEntity<ShipmentDto> updateStatus(
            @PathVariable Long shipmentId,
            @RequestParam ShipmentStatus status) {

        return ResponseEntity.ok(shipmentService.updateShipmentStatus(shipmentId, status));
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<ShipmentDto> cancelShipment(@PathVariable Long orderId) {
        return ResponseEntity.ok(shipmentService.cancelShipment(orderId));
    }
}
