package com.peersmarket.marketplace.user.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {
    private Long id;
    private CityDto city;
    private Double longitude;
    private Double latitude;
}