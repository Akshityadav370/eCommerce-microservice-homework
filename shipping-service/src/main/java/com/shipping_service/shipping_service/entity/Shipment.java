package com.shipping_service.shipping_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

    private LocalDateTime createdAt;

    private LocalDateTime shippedAt;

    private LocalDateTime deliveredAt;
}
