package com.shipping_service.shipping_service.service;

import com.shipping_service.shipping_service.dto.ShipmentDto;
import com.shipping_service.shipping_service.entity.Shipment;
import com.shipping_service.shipping_service.entity.ShipmentStatus;
import com.shipping_service.shipping_service.repository.ShipmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ShipmentDto createShipment(ShipmentDto shipmentDto) {

        Shipment shipment = modelMapper.map(shipmentDto, Shipment.class);

        shipment.setShipmentStatus(ShipmentStatus.CREATED);
        shipment.setCreatedAt(LocalDateTime.now());

        Shipment savedShipment = shipmentRepository.save(shipment);

        return modelMapper.map(savedShipment, ShipmentDto.class);
    }

    public ShipmentDto getShipmentByOrderId(Long orderId) {

        Shipment shipment = shipmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        return modelMapper.map(shipment, ShipmentDto.class);
    }

    @Transactional
    public ShipmentDto updateShipmentStatus(Long shipmentId, ShipmentStatus status) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        shipment.setShipmentStatus(status);

        if(status == ShipmentStatus.SHIPPED) {
            shipment.setShippedAt(LocalDateTime.now());
        }

        if(status == ShipmentStatus.DELIVERED) {
            shipment.setDeliveredAt(LocalDateTime.now());
        }

        return modelMapper.map(shipmentRepository.save(shipment), ShipmentDto.class);
    }

    @Transactional
    public ShipmentDto cancelShipment(Long orderId) {

        Shipment shipment = shipmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        shipment.setShipmentStatus(ShipmentStatus.CANCELLED);

        return modelMapper.map(shipmentRepository.save(shipment), ShipmentDto.class);
    }
}
