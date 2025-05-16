package com.peersmarket.marketplace.user.application.port.out;

import java.util.Optional;

import com.peersmarket.marketplace.user.domain.model.City;

public interface ReverseGeocoding {
    Optional<City> findCityByCoordinates(Double longitude, Double latitude);
}