package com.peersmarket.marketplace.user.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peersmarket.marketplace.user.application.dto.AddressDto;
import com.peersmarket.marketplace.user.application.port.in.AddressService;
import com.peersmarket.marketplace.user.domain.model.Address;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AddressMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @PostMapping("/create")
    public ResponseEntity<AddressDto> createAddress(@RequestParam final Double longitude, @RequestParam final Double latitude) {
        final Address address = addressService.createAddressFromCoordinates(longitude, latitude);
        return ResponseEntity.ok(addressMapper.toDto(address));
    }
}