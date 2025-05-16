package com.peersmarket.marketplace.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.user.application.port.in.AddressService;
import com.peersmarket.marketplace.user.application.port.out.AddressRepository;
import com.peersmarket.marketplace.user.application.port.out.CityRepository;
import com.peersmarket.marketplace.user.application.port.out.ReverseGeocoding;
import com.peersmarket.marketplace.user.domain.model.Address;
import com.peersmarket.marketplace.user.domain.model.City;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final ReverseGeocoding reverseGeocoding;

    @Override
    public Address createAddressFromCoordinates(final Double longitude, final Double latitude) {
        final City city = reverseGeocoding.findCityByCoordinates(longitude, latitude)
                .orElseThrow(() -> new RuntimeException("City not found for given coordinates"));

        final City persistedCity = cityRepository.findByNameIgnoreCase(city.getName())
                .orElseGet(() -> cityRepository.save(city));

        final Address address = new Address();
        address.setCoordinates(longitude, latitude);
        address.setCity(persistedCity);

        return addressRepository.save(address);
    }
}