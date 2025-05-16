package com.peersmarket.marketplace.user.application.port.in;

import com.peersmarket.marketplace.user.domain.model.Address;

public interface AddressService {
    Address createAddressFromCoordinates(Double longitude, Double latitude);
}