package com.peersmarket.marketplace.user.application.port.out;

import java.util.Optional;

import com.peersmarket.marketplace.user.domain.model.City;

public interface CityRepository {
    Optional<City> findByNameIgnoreCase(String name);    
    City save(City city);

}
    