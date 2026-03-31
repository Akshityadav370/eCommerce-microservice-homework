package com.shipping_service.shipping_service.repository;

import com.shipping_service.shipping_service.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<Shipment, Long> {
}