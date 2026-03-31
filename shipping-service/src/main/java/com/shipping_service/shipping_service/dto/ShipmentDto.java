package com.shipping_service.shipping_service.dto;

import com.shipping_service.shipping_service.entity.ShipmentStatus;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDto {

    private Long id;

    private Long orderId;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private ShipmentStatus shipmentStatus;
}
